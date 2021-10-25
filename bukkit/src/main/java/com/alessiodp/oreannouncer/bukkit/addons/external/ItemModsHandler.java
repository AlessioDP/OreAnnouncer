package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.listeners.BlockListener;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.api.block.CustomBlock;
import dev.linwood.itemmods.api.events.CustomBlockBreakEvent;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Set;


@RequiredArgsConstructor
public class ItemModsHandler implements Listener {
	private final OreAnnouncerPlugin plugin;
	private static final String ADDON_NAME = "ItemMods";
	private static boolean active;
	private static boolean eventRegistered = false;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.BLOCKS_ITEMMODS_ENABLE
				&& Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			try {
				Class.forName("dev.linwood.itemmods.ItemMods");
				if (!eventRegistered) {
					eventRegistered = true;
					((Plugin) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(new ItemModsBlockListener(plugin), ((Plugin) plugin.getBootstrap()));
				}
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
				active = true;
			} catch (ClassNotFoundException ignored) {
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isPluginBlock(Block block) {
		if (active) {
			CustomBlock cb = ItemMods.getCustomBlockManager().fromBlock(block);
			return cb != null && cb.getPackObject() != null;
		}
		return false;
	}
	
	public static boolean isPluginItemStack(ItemStack itemStack) {
		/* Since 2.0 its hard to get ItemStack -> Block ID so its deprecated the add menu for ItemMods
		if (active) {
			CustomItem ci = ItemMods.getCustomItemManager().fromItemStack(itemStack);
			return ci != null && ci.getPackObject() != null;
		}*/
		return false;
	}
	
	public static String getNameByBlock(Block block) {
		if (active) {
			CustomBlock cb = ItemMods.getCustomBlockManager().fromBlock(block);
			if (cb != null && cb.getPackObject() != null) {
				return "ITEMMODS_" + CommonUtils.toUpperCase(cb.getPackObject().toString());
			}
		}
		return "";
	}
	
	public static String getNameByItemStack(ItemStack itemStack) {
		/* Since 2.0 its hard to get ItemStack -> Block ID so its deprecated the add menu for ItemMods
		if (active) {
			for (ItemModsPack pack : ItemMods.getPackManager().getPacks()) {
				for (BlockAsset ba : pack.getBlocks()) {
					if (ba.getModelTexture().equals(itemStack)) {
						return "ITEMMODS_" + CommonUtils.toUpperCase(ba.toString());
					}
				}
			}
		}*/
		return "";
	}
	
	public static ItemStack getItemStackByName(String materialName) {
		if (active) {
			String name = CommonUtils.toLowerCase(materialName.substring(9));
			BlockAsset ba = ItemMods.getCustomBlockManager().getAssetByKey(name);
			if (ba != null && ba.getModelTexture() != null) {
				return ba.getModelTexture();
			}
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
				// Get enchantments
				int enchantmentLevel;
				try {
					enchantmentLevel = event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH);
				} catch (NoSuchMethodError ex) {
					// MC 1.8
					enchantmentLevel = event.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH);
				}
				
				super.onBlockBreak(
						new BukkitUser(plugin, event.getPlayer()),
						"ITEMMODS_" + CommonUtils.toUpperCase(event.getCustomBlock().getPackObject().toString()),
						event.getPlayer().getLastTwoTargetBlocks((Set<Material>) null,1).get(0).getLightLevel(),
						enchantmentLevel > 0,
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
