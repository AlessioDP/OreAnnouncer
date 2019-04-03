package com.alessiodp.oreannouncer.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.listeners.BlockListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BukkitBlockListener extends BlockListener implements Listener {
	
	public BukkitBlockListener(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.isCancelled()) {
			super.onBlockBreak(
					new BukkitUser(event.getPlayer()),
					event.getBlock().getType().name(),
					event.getPlayer().getLastTwoTargetBlocks(null,1).get(0).getLightLevel(),
					event.getBlock().hasMetadata(OAConstants.BLOCK_METADATA),
					event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0,
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
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.isCancelled() && ConfigMain.BLOCKS_BYPASS_PLAYERBLOCKS)
			super.onBlockPlace(
					new BukkitUser(event.getPlayer()),
					event.getBlock().getType().name(),
					event.getBlock().hasMetadata(OAConstants.BLOCK_METADATA),
					new ADPLocation(
							event.getBlock().getLocation().getWorld().getName(),
							event.getBlock().getLocation().getX(),
							event.getBlock().getLocation().getY(),
							event.getBlock().getLocation().getZ(),
							event.getBlock().getLocation().getYaw(),
							event.getBlock().getLocation().getPitch()
					));
	}
}
