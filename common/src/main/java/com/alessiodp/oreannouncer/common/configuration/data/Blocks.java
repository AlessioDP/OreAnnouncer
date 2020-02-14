package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Blocks extends ConfigurationFile {
	@Getter private final String fileName = "blocks.yml";
	@Getter private final String resourceName = "blocks.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BLOCKS;
	
	// Blocks
	public static Map<String, OABlockImpl> LIST;
	
	public Blocks(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	public void loadDefaults() {
		OABlockImpl blockDiamond = new OABlockImpl(plugin, "DIAMOND_ORE");
		blockDiamond.setAccessible(true);
		blockDiamond.setEnabled(true);
		blockDiamond.setAlertingUsers(true);
		blockDiamond.setAlertingAdmins(true);
		blockDiamond.setSingularName("diamond");
		blockDiamond.setPluralName("diamonds");
		blockDiamond.setCountNumber(20);
		blockDiamond.setCountTime(600);
		blockDiamond.setSound("ENTITY_PLAYER_LEVELUP");
		blockDiamond.setCountingOnDestroy(true);
		blockDiamond.setAccessible(false);
		
		OABlockImpl blockEmerald = new OABlockImpl(plugin, "EMERALD_ORE");
		blockEmerald.setAccessible(true);
		blockEmerald.setEnabled(true);
		blockEmerald.setAlertingUsers(true);
		blockEmerald.setAlertingAdmins(true);
		blockEmerald.setSingularName("emerald");
		blockEmerald.setPluralName("emeralds");
		blockEmerald.setSound("ENTITY_PLAYER_LEVELUP");
		blockEmerald.setCountingOnDestroy(true);
		blockEmerald.setAccessible(false);
		
		OABlockImpl blockGold = new OABlockImpl(plugin, "GOLD_ORE");
		blockEmerald.setAccessible(true);
		blockEmerald.setEnabled(true);
		blockEmerald.setAlertingUsers(false);
		blockEmerald.setAlertingAdmins(false);
		blockEmerald.setSingularName("gold");
		blockEmerald.setPluralName("golds");
		blockEmerald.setAccessible(false);
		
		OABlockImpl blockRedstone = new OABlockImpl(plugin, "REDSTONE_ORE");
		blockRedstone.setAccessible(true);
		blockRedstone.setEnabled(false);
		blockRedstone.setAlertingUsers(true);
		blockRedstone.setAlertingAdmins(true);
		blockRedstone.setSingularName("redstone");
		blockRedstone.setPluralName("redstones");
		blockRedstone.setAccessible(false);
		
		OABlockImpl blockIron = new OABlockImpl(plugin, "REDSTONE_ORE");
		blockIron.setAccessible(true);
		blockIron.setEnabled(false);
		blockIron.setAlertingUsers(false);
		blockIron.setAlertingAdmins(false);
		blockIron.setSingularName("iron");
		blockIron.setPluralName("irons");
		blockIron.setAccessible(false);
		
		OABlockImpl blockQuartz = new OABlockImpl(plugin, "NETHER_QUARTZ_ORE");
		blockQuartz.setAccessible(true);
		blockQuartz.setEnabled(false);
		blockQuartz.setAlertingUsers(true);
		blockQuartz.setAlertingAdmins(true);
		blockQuartz.setSingularName("quartz");
		blockQuartz.setPluralName("quartzes");
		blockQuartz.setAccessible(false);
		
		LIST = new HashMap<>();
		LIST.put(blockDiamond.getMaterialName(), blockDiamond);
		LIST.put(blockEmerald.getMaterialName(), blockEmerald);
		LIST.put(blockEmerald.getMaterialName(), blockGold);
		LIST.put(blockEmerald.getMaterialName(), blockRedstone);
		LIST.put(blockEmerald.getMaterialName(), blockIron);
		LIST.put(blockEmerald.getMaterialName(), blockQuartz);
	}
	
	@Override
	public void loadConfiguration() {
		HashMap<String, OABlockImpl> blocks = new HashMap<>();
		OABlockImpl block;
		
		ConfigurationSection csBlocks = configuration.getConfigurationSection("blocks");
		if (csBlocks != null) {
			for (String key : csBlocks.getKeys(false)) {
				if (((OreAnnouncerPlugin) plugin).getBlockManager().existsMaterial(key)) {
					// Material exists
					block = new OABlockImpl(plugin, key);
					block.setAccessible(true);
					block.setEnabled(csBlocks.getBoolean(key + ".enabled", true));
					block.setAlertingUsers(csBlocks.getBoolean(key + ".alerts.user", false));
					block.setAlertingAdmins(csBlocks.getBoolean(key + ".alerts.admin", false));
					block.setSingularName(csBlocks.getString(key + ".name.singular", key));
					block.setPluralName(csBlocks.getString(key + ".name.plural", key));
					block.setCountNumber(csBlocks.getInt(key + ".count.number", 0));
					block.setCountTime(csBlocks.getInt(key + ".count.time", 0));
					block.setCountTimeFormat(csBlocks.getString(key + ".count.time-format", null));
					block.setMessageUser(csBlocks.getString(key + ".messages.user", null));
					block.setMessageAdmin(csBlocks.getString(key + ".messages.admin", null));
					block.setMessageConsole(csBlocks.getString(key + ".messages.console", null));
					block.setCountMessageUser(csBlocks.getString(key + ".messages.user-count", null));
					block.setCountMessageAdmin(csBlocks.getString(key + ".messages.admin-count", null));
					block.setCountMessageConsole(csBlocks.getString(key + ".messages.console-count", null));
					block.setSound(csBlocks.getString(key + ".sound", ""));
					block.setLightLevel(csBlocks.getInt(key + ".light-level", 15));
					block.setCountingOnDestroy(csBlocks.getBoolean(key + ".count-on-destroy", false));
					block.setTNTEnabled(csBlocks.getBoolean(key + ".tnt", true));
					block.setAccessible(false);
					blocks.put(key.toUpperCase(), block);
				} else {
					// Material doesn't exist
					plugin.getLoggerManager().printError(OAConstants.DEBUG_CFG_WRONGBLOCK
							.replace("{block}", key));
				}
			}
		}
		LIST = blocks;
	}
	
	public boolean existsBlock(@NonNull String materialName) {
		return LIST.containsKey(materialName.toUpperCase());
	}
	
	public void updateBlock(@NonNull OABlockImpl block) {
		configuration.set("blocks." + block.getMaterialName() + ".enabled", block.isEnabled());
		configuration.set("blocks." + block.getMaterialName() + ".alerts.user", block.isAlertingUsers());
		configuration.set("blocks." + block.getMaterialName() + ".alerts.admin", block.isAlertingAdmins());
		configuration.set("blocks." + block.getMaterialName() + ".name.singular", block.getSingularName());
		configuration.set("blocks." + block.getMaterialName() + ".name.plural", block.getPluralName());
		if (block.getCountNumber() != 0
				|| block.getCountTime() != 0
				|| block.getCountTimeFormat() != null) {
			configuration.set("blocks." + block.getMaterialName() + ".count.number", block.getCountNumber());
			configuration.set("blocks." + block.getMaterialName() + ".count.time", block.getCountTime());
			configuration.set("blocks." + block.getMaterialName() + ".count.time-format", block.getCountTimeFormat());
		} else {
			configuration.set("blocks." + block.getMaterialName() + ".count", null);
		}
		if (block.getMessageUser() != null
				|| block.getMessageAdmin() != null
				|| block.getMessageConsole() != null
				|| block.getCountMessageUser() != null
				|| block.getCountMessageAdmin() != null
				|| block.getCountMessageConsole() != null) {
			configuration.set("blocks." + block.getMaterialName() + ".messages.user", block.getMessageUser());
			configuration.set("blocks." + block.getMaterialName() + ".messages.admin", block.getMessageAdmin());
			configuration.set("blocks." + block.getMaterialName() + ".messages.console", block.getMessageConsole());
			configuration.set("blocks." + block.getMaterialName() + ".messages.user-count", block.getCountMessageUser());
			configuration.set("blocks." + block.getMaterialName() + ".messages.admin-count", block.getCountMessageAdmin());
			configuration.set("blocks." + block.getMaterialName() + ".messages.console-count", block.getCountMessageConsole());
		} else {
			configuration.set("blocks." + block.getMaterialName() + ".messages", null);
		}
		configuration.set("blocks." + block.getMaterialName() + ".sound", block.getSound());
		configuration.set("blocks." + block.getMaterialName() + ".light-level", block.getLightLevel());
		configuration.set("blocks." + block.getMaterialName() + ".count-on-destroy", block.isCountingOnDestroy());
		configuration.set("blocks." + block.getMaterialName() + ".tnt", block.isTNTEnabled());
		
		try {
			configuration.saveWithComments();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!existsBlock(block.getMaterialName())) {
			LIST.put(block.getMaterialName().toUpperCase(), block);
		}
	}
	
	public void removeBlock(@NonNull OABlockImpl block) {
		configuration.set("blocks." + block.getMaterialName(), null);
		
		try {
			configuration.saveWithComments();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LIST.remove(block.getMaterialName().toUpperCase());
	}
}
