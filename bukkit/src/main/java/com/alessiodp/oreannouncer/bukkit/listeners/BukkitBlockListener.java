package com.alessiodp.oreannouncer.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.bukkit.blocks.BukkitBlockManager;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.listeners.BlockListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Set;

public class BukkitBlockListener extends BlockListener implements Listener {
	
	public BukkitBlockListener(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@SuppressWarnings({"deprecation", "ConstantConditions", "RedundantCast"})
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.isCancelled()) {
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
					((BukkitBlockManager) plugin.getBlockManager()).getBlockType(event.getBlock()),
					event.getPlayer().getLastTwoTargetBlocks((Set<Material>) null,1).get(0).getLightLevel(),
					enchantmentLevel > 0,
					new ADPLocation(
							event.getBlock().getLocation().getWorld().getName(),
							event.getBlock().getLocation().getX(),
							event.getBlock().getLocation().getY(),
							event.getBlock().getLocation().getZ(),
							event.getBlock().getLocation().getYaw(),
							event.getBlock().getLocation().getPitch()
					)
			);
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.isCancelled() && ConfigMain.BLOCKS_BYPASS_PLAYERBLOCKS)
			super.onBlockPlace(
					new BukkitUser(plugin, event.getPlayer()),
					((BukkitBlockManager) plugin.getBlockManager()).getBlockType(event.getBlock()),
					new ADPLocation(
							event.getBlock().getLocation().getWorld().getName(),
							event.getBlock().getLocation().getX(),
							event.getBlock().getLocation().getY(),
							event.getBlock().getLocation().getZ(),
							event.getBlock().getLocation().getYaw(),
							event.getBlock().getLocation().getPitch()
					));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onTNTExplode(EntityExplodeEvent event) {
		if (event.getEntity() instanceof TNTPrimed
				&& !event.isCancelled()
				&& ConfigMain.BLOCKS_TNT_MINING_ALERT_ON) {
			final BukkitUser user = (
					ConfigMain.BLOCKS_TNT_MINING_CATCH_PLAYER
						&& ((TNTPrimed) event.getEntity()).getSource() != null
						&& ((TNTPrimed) event.getEntity()).getSource() instanceof Player
					) ? new BukkitUser(plugin, ((TNTPrimed) event.getEntity()).getSource()) : null;
			ArrayList<String> blocks = new ArrayList<>();
			for (Block block : event.blockList()) {
				blocks.add(((BukkitBlockManager) plugin.getBlockManager()).getBlockType(block));
			}
			plugin.getScheduler().runAsync(() -> super.onTNTExplode(
					user,
					blocks,
					new ADPLocation(
							event.getLocation().getWorld() != null ? event.getLocation().getWorld().getName() : "",
							event.getLocation().getX(),
							event.getLocation().getY(),
							event.getLocation().getZ(),
							event.getLocation().getYaw(),
							event.getLocation().getPitch()
					)));
		}
	}
}
