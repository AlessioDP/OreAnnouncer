package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import lombok.RequiredArgsConstructor;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.block.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@RequiredArgsConstructor
public class MMOItemsHandler {
	private final OreAnnouncerPlugin plugin;
	private static final String ADDON_NAME = "MMOItems";
	private static boolean active;
	private static MMOItems mmoItems;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.BLOCKS_MMOITEMS_ENABLE
				&& Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			mmoItems = MMOItems.plugin;
			if (mmoItems != null) {
				active = true;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isPluginBlock(Block block) {
		return active && mmoItems.getCustomBlocks().getFromBlock(block.getBlockData()).isPresent();
	}
	
	public static boolean isPluginItemStack(ItemStack itemStack) {
		return active && mmoItems.getCustomBlocks().getAll().stream().anyMatch(customBlock -> customBlock.getItem() != null && customBlock.getItem().equals(itemStack));
	}
		
	public static String getNameByBlock(Block block) {
		if (active) {
			Optional<CustomBlock> opt = mmoItems.getCustomBlocks().getFromBlock(block.getBlockData());
			if (opt.isPresent())
				return "MMOITEMS_" + opt.get().getId();
		}
		return "";
	}
	
	public static String getNameByItemStack(ItemStack itemStack) {
		if (active) {
			Optional<CustomBlock> opt = mmoItems.getCustomBlocks().getAll().stream().filter(customBlock -> customBlock.getItem() != null && customBlock.getItem().equals(itemStack)).findAny();
			if (opt.isPresent())
				return "MMOITEMS_" + opt.get().getId();
		}
		return "";
	}
	
	public static ItemStack getItemStackByName(String materialName) {
		try {
			int id = Integer.parseInt(materialName.substring(9));
			return mmoItems.getCustomBlocks().getBlock(id).getItem();
		} catch (NumberFormatException | NullPointerException ignored) {}
		return null;
	}
}
