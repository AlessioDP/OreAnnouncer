package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blocks extends ConfigurationFile {
	@Getter private final String fileName = "blocks.yml";
	@Getter private final String resourceName = "blocks.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BLOCKS;
	
	// Blocks
	public static Map<String, OABlockImpl> LIST = new HashMap<>();
	public static Map<String, OABlockImpl> VARIANTS = new HashMap<>();
	
	public Blocks(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	public void loadDefaults() {
		OABlockImpl blockDiamond = new OABlockImpl(plugin, "DIAMOND_ORE");
		blockDiamond.setAccessible(true);
		blockDiamond.setEnabled(true);
		blockDiamond.setDisplayName("Diamond Ore");
		blockDiamond.setDisplayColor("&b");
		blockDiamond.setAlertingUsers(true);
		blockDiamond.setAlertingAdmins(true);
		blockDiamond.setSingularName("diamond");
		blockDiamond.setPluralName("diamonds");
		blockDiamond.setCountNumber(20);
		blockDiamond.setCountTime(600);
		blockDiamond.setSound("ENTITY_PLAYER_LEVELUP");
		blockDiamond.setCountingOnDestroy(true);
		blockDiamond.setPriority(90);
		blockDiamond.setAccessible(false);
		
		OABlockImpl blockEmerald = new OABlockImpl(plugin, "EMERALD_ORE");
		blockEmerald.setAccessible(true);
		blockEmerald.setEnabled(true);
		blockEmerald.setDisplayName("Emerald Ore");
		blockEmerald.setDisplayColor("&a");
		blockEmerald.setAlertingUsers(true);
		blockEmerald.setAlertingAdmins(true);
		blockEmerald.setSingularName("emerald");
		blockEmerald.setPluralName("emeralds");
		blockEmerald.setSound("ENTITY_PLAYER_LEVELUP");
		blockEmerald.setCountingOnDestroy(true);
		blockEmerald.setPriority(100);
		blockEmerald.setAccessible(false);
		
		OABlockImpl blockGold = new OABlockImpl(plugin, "GOLD_ORE");
		blockGold.setAccessible(true);
		blockGold.setEnabled(true);
		blockGold.setDisplayName("Gold Ore");
		blockGold.setDisplayColor("&6");
		blockGold.setAlertingUsers(false);
		blockGold.setAlertingAdmins(false);
		blockGold.setSingularName("gold");
		blockGold.setPluralName("golds");
		blockGold.setPriority(80);
		blockGold.setAccessible(false);
		
		OABlockImpl blockRedstone = new OABlockImpl(plugin, "REDSTONE_ORE");
		blockRedstone.setAccessible(true);
		blockRedstone.setEnabled(false);
		blockRedstone.setDisplayName("Redstone Ore");
		blockRedstone.setDisplayColor("&c");
		blockRedstone.setAlertingUsers(true);
		blockRedstone.setAlertingAdmins(true);
		blockRedstone.setSingularName("redstone");
		blockRedstone.setPluralName("redstones");
		blockRedstone.setPriority(-1);
		blockRedstone.setAccessible(false);
		
		OABlockImpl blockIron = new OABlockImpl(plugin, "IRON_ORE");
		blockIron.setAccessible(true);
		blockIron.setEnabled(false);
		blockIron.setDisplayName("Iron Ore");
		blockIron.setDisplayColor("&f");
		blockIron.setAlertingUsers(false);
		blockIron.setAlertingAdmins(false);
		blockIron.setSingularName("iron");
		blockIron.setPluralName("irons");
		blockIron.setPriority(-1);
		blockIron.setAccessible(false);
		
		OABlockImpl blockQuartz = new OABlockImpl(plugin, "NETHER_QUARTZ_ORE");
		blockQuartz.setAccessible(true);
		blockQuartz.setEnabled(false);
		blockQuartz.setDisplayName("Quartz Ore");
		blockQuartz.setDisplayColor("&4");
		blockQuartz.setAlertingUsers(true);
		blockQuartz.setAlertingAdmins(true);
		blockQuartz.setSingularName("quartz");
		blockQuartz.setPluralName("quartzes");
		blockQuartz.setPriority(60);
		blockQuartz.setAccessible(false);
		
		LIST = new HashMap<>();
		Blocks.addBlock(blockDiamond);
		Blocks.addBlock(blockEmerald);
		Blocks.addBlock(blockGold);
		Blocks.addBlock(blockRedstone);
		Blocks.addBlock(blockIron);
		Blocks.addBlock(blockQuartz);
		
		VARIANTS = new HashMap<>();
	}
	
	@Override
	public void loadConfiguration() {
		HashMap<String, OABlockImpl> list = new HashMap<>();
		HashMap<String, OABlockImpl> variants = new HashMap<>();
		
		ConfigurationSection csBlocks = configuration.getConfigurationSection("blocks");
		if (csBlocks != null) {
			for (String key : csBlocks.getKeys(false)) {
				if (((OreAnnouncerPlugin) plugin).getBlockManager().existsMaterial(key)) {
					// Material exists
					final OABlockImpl block = new OABlockImpl(plugin, key);
					block.setAccessible(true);
					block.setEnabled(csBlocks.getBoolean(key + ".enabled", true));
					List<?> vars = csBlocks.getList(key + ".variants", Collections.emptyList());
					vars.forEach(v -> {
						if (v instanceof String && ((OreAnnouncerPlugin) plugin).getBlockManager().existsMaterial((String) v)) {
							block.addVariant((String) v);
						} else {
							plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_CFG_WRONG_VARIANT, v));
						}
					});
					block.setDisplayName(csBlocks.getString(key + ".display-name", key));
					block.setDisplayColor(csBlocks.getString(key + ".display-color", ""));
					block.setAlertingUsers(csBlocks.getBoolean(key + ".alerts.user", false));
					block.setAlertingAdmins(csBlocks.getBoolean(key + ".alerts.admin", false));
					block.setSingularName(csBlocks.getString(key + ".name.singular", null));
					block.setPluralName(csBlocks.getString(key + ".name.plural", null));
					block.setCountNumber(csBlocks.getInt(key + ".count.number", 0));
					block.setCountTime(csBlocks.getInt(key + ".count.time", 0));
					block.setMessageUser(csBlocks.getString(key + ".messages.user", null));
					block.setMessageAdmin(csBlocks.getString(key + ".messages.admin", null));
					block.setMessageConsole(csBlocks.getString(key + ".messages.console", null));
					block.setCountMessageUser(csBlocks.getString(key + ".messages.user-count", null));
					block.setCountMessageAdmin(csBlocks.getString(key + ".messages.admin-count", null));
					block.setCountMessageConsole(csBlocks.getString(key + ".messages.console-count", null));
					block.setSound(csBlocks.getString(key + ".sound", ""));
					block.setLightLevel(csBlocks.getInt(key + ".light-level", 15));
					block.setHeightLevel(csBlocks.getInt(key + ".height-level", 0));
					block.setCountingOnDestroy(csBlocks.getBoolean(key + ".count-on-destroy", false));
					block.setTNTEnabled(csBlocks.getBoolean(key + ".tnt", true));
					block.setPriority(csBlocks.getInt(key + ".priority", 0));
					
					block.setAccessible(false);
					list.put(CommonUtils.toUpperCase(key), block);
					block.getVariants().forEach(v -> variants.put(CommonUtils.toUpperCase(v), block));
				} else {
					// Material doesn't exist
					plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_CFG_WRONG_BLOCK, key));
				}
			}
		}
		LIST = list;
		VARIANTS = variants;
	}
	
	public static void addBlock(@NonNull OABlockImpl block) {
		addBlock(block.getMaterialName(), block);
	}
	
	public static void addBlock(@NonNull String materialName, @NonNull OABlockImpl block) {
		LIST.put(CommonUtils.toUpperCase(materialName), block);
	}
	
	public static OABlockImpl searchBlock(@NonNull String materialName) {
		OABlockImpl ret = LIST.get(CommonUtils.toUpperCase(materialName));
		if (ret == null)
			ret = VARIANTS.get(CommonUtils.toUpperCase(materialName));
		return ret;
	}
	
	public static boolean existsBlock(@NonNull String materialName) {
		return LIST.containsKey(CommonUtils.toUpperCase(materialName)) || VARIANTS.containsKey(CommonUtils.toUpperCase(materialName));
	}
	
	public void updateBlock(@NonNull OABlockImpl block) {
		configuration.set("blocks." + block.getMaterialName() + ".enabled", block.isEnabled());
		if (block.getVariants().size() > 0)
			configuration.set("blocks." + block.getMaterialName() + ".variants", block.getVariants());
		else
			configuration.set("blocks." + block.getMaterialName() + ".variants", null);
		configuration.set("blocks." + block.getMaterialName() + ".display-name", block.getDisplayName());
		configuration.set("blocks." + block.getMaterialName() + ".display-color", block.getDisplayColor());
		configuration.set("blocks." + block.getMaterialName() + ".alerts.user", block.isAlertingUsers());
		configuration.set("blocks." + block.getMaterialName() + ".alerts.admin", block.isAlertingAdmins());
		if (block.getSingularName() != null || block.getPluralName() != null) {
			configuration.set("blocks." + block.getMaterialName() + ".name.singular", block.getSingularName());
			configuration.set("blocks." + block.getMaterialName() + ".name.plural", block.getPluralName());
		} else {
			configuration.set("blocks." + block.getMaterialName() + ".name", null);
		}
		if (block.getCountNumber() != 0
				|| block.getCountTime() != 0) {
			configuration.set("blocks." + block.getMaterialName() + ".count.number", block.getCountNumber());
			configuration.set("blocks." + block.getMaterialName() + ".count.time", block.getCountTime());
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
		configuration.set("blocks." + block.getMaterialName() + ".height-level", block.getHeightLevel() > 0 ? block.getHeightLevel() : null);
		configuration.set("blocks." + block.getMaterialName() + ".count-on-destroy", block.isCountingOnDestroy());
		configuration.set("blocks." + block.getMaterialName() + ".tnt", block.isTNTEnabled());
		
		try {
			configuration.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!existsBlock(block.getMaterialName())) {
			LIST.put(CommonUtils.toUpperCase(block.getMaterialName()), block);
		}
		
		// Remove old variants & add new ones
		VARIANTS.entrySet().removeIf(e -> e.getValue().getMaterialName().equals(block.getMaterialName()));
		block.getVariants().forEach(v -> VARIANTS.put(CommonUtils.toUpperCase(v), block));
	}
	
	public void removeBlock(@NonNull OABlockImpl block) {
		configuration.set("blocks." + block.getMaterialName(), null);
		
		try {
			configuration.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LIST.remove(CommonUtils.toUpperCase(block.getMaterialName()));
		VARIANTS.entrySet().removeIf(e -> e.getValue().getMaterialName().equals(block.getMaterialName()));
	}
}
