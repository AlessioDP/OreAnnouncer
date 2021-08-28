package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.listeners.BlockListener;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsApi;
import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import com.github.codedoctorde.itemmods.api.events.CustomBlockBreakEvent;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ItemModsHandler implements Listener {
	private final OreAnnouncerPlugin plugin;
	private static final String ADDON_NAME = "ItemMods";
	private static boolean active;
	private static boolean eventRegistered = false;
	private static ItemModsApi itemMods;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.BLOCKS_ITEMMODS_ENABLE
				&& Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			itemMods = ItemMods.getPlugin().getApi();
			if (itemMods != null) {
				active = true;
				if (!eventRegistered) {
					eventRegistered = true;
					((Plugin) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(new ItemModsBlockListener(plugin), ((Plugin) plugin.getBootstrap()));
				}
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isPluginBlock(Block block) {
		return active && itemMods.getCustomBlockManager().getCustomBlock(block) != null;
	}
	
	public static boolean isPluginItemStack(ItemStack itemStack) {
		return active && itemMods.getCustomItemManager().getItems().stream().anyMatch((itemConfig -> itemConfig.giveItemStack().equals(itemStack)));
	}
	
	public static String getNameByBlock(Block block) {
		if (active) {
			CustomBlock customBlock = itemMods.getCustomBlockManager().getCustomBlock(block);
			if (customBlock != null
					&& customBlock.getConfig().getReferenceItemConfig() != null)
				return "ITEMMODS_" + CommonUtils.toUpperCase(customBlock.getConfig().getReferenceItemConfig().getName());
		}
		return "";
	}
	
	public static String getNameByItemStack(ItemStack itemStack) {
		if (active) {
			Optional<ItemConfig> opt = itemMods.getCustomItemManager().getItems().stream().filter((itemConfig -> itemConfig.giveItemStack().equals(itemStack))).findAny();
			if (opt.isPresent())
				return "ITEMMODS_" + CommonUtils.toUpperCase(opt.get().getName());
		}
		return "";
	}
	
	public static ItemStack getItemStackByName(String materialName) {
		String name = materialName.substring(9);
		Optional<ItemConfig> opt = itemMods.getCustomItemManager().getItems().stream().filter(itemConfig -> name.equalsIgnoreCase(itemConfig.getName())).findAny();
		if (opt.isPresent()) {
			return opt.get().giveItemStack();
		}
		return null;
	}
	
	public static class ItemModsBlockListener extends BlockListener implements Listener {
		
		public ItemModsBlockListener(OreAnnouncerPlugin plugin) {
			super(plugin);
		}
		
		@SuppressWarnings({"ConstantConditions", "RedundantCast"})
		@EventHandler(priority= EventPriority.MONITOR)
		public void onBlockBreak(CustomBlockBreakEvent event) {
			if (active && !event.isCancelled()) {
				super.onBlockBreak(
						new BukkitUser(plugin, event.getPlayer()),
						"ITEMMODS_" + CommonUtils.toUpperCase(event.getCustomBlock().getConfig().getName()),
						event.getPlayer().getLastTwoTargetBlocks((Set<Material>) null,1).get(0).getLightLevel(),
						event.getDropType() == CustomBlock.BlockDropType.SILK_TOUCH,
						new ADPLocation(
								event.getCustomBlock().getLocation().getWorld().getName(),
								event.getCustomBlock().getLocation().getX(),
								event.getCustomBlock().getLocation().getY(),
								event.getCustomBlock().getLocation().getZ(),
								event.getCustomBlock().getLocation().getYaw(),
								event.getCustomBlock().getLocation().getPitch()
						)
				);
			}
		}
	}
}
