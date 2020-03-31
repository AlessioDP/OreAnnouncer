package com.alessiodp.oreannouncer.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BlockListener {
	protected final OreAnnouncerPlugin plugin;
	
	protected void onBlockBreak(User user, String blockType, int lightLevel, boolean hasSilkTouch, ADPLocation blockLocation) {
		if (!(ConfigMain.BLOCKS_BYPASS_SILKTOUCH && hasSilkTouch)
				&& (ConfigMain.STATS_ENABLE || ConfigMain.ALERTS_ENABLE)
				&& (!ConfigMain.BLOCKS_BYPASS_PLAYERBLOCKS || !plugin.getBlockManager().isBlockMarked(blockLocation, blockType, BlockManager.MarkType.STORE))) {
			OABlockImpl block = Blocks.LIST.get(blockType);
			if (block != null && block.isEnabled()) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_EVENT_BLOCK_BREAK
						.replace("{player}", user.getName())
						.replace("{block}", blockType), true);
				
				// Store information into database
				if (ConfigMain.STATS_ENABLE) {
					if (!user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY.toString()))
					store(user, block, lightLevel);
					
					if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND.toString()))
						found(user, block, blockLocation, lightLevel);
				}
				
				if (ConfigMain.ALERTS_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS.toString())) {
					// Handle alerts
					alert(user, block, blockLocation, lightLevel);
				}
			}
		}
		
		// Unmark broken block to prevent metadata ghosting
		plugin.getBlockManager().unmarkBlock(blockLocation, BlockManager.MarkType.ALERT);
		plugin.getBlockManager().unmarkBlock(blockLocation, BlockManager.MarkType.FOUND);
		plugin.getBlockManager().unmarkBlock(blockLocation, BlockManager.MarkType.STORE);
	}
	
	protected void onBlockPlace(User user, String blockType, ADPLocation blockLocation) {
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, blockType, BlockManager.MarkType.ALERT)
				|| !plugin.getBlockManager().isBlockMarked(blockLocation, blockType, BlockManager.MarkType.FOUND)) {
			OABlockImpl block = Blocks.LIST.get(blockType);
			if (block != null && block.isEnabled()) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_EVENT_BLOCK_PLACE
						.replace("{player}", user.getName())
						.replace("{block}", blockType), true);
				
				// Mark block so it won't be counted
				plugin.getBlockManager().markBlock(blockLocation, blockType, BlockManager.MarkType.ALERT);
				plugin.getBlockManager().markBlock(blockLocation, blockType, BlockManager.MarkType.FOUND);
				plugin.getBlockManager().markBlock(blockLocation, blockType, BlockManager.MarkType.STORE);
			}
		}
	}
	
	protected void onTNTExplode(@Nullable User user, ArrayList<String> blocksType, ADPLocation blockLocation) {
		if (ConfigMain.STATS_ENABLE || ConfigMain.ALERTS_ENABLE) {
			HashMap<OABlockImpl, Integer> blocks = new HashMap<>();
			int total = 0;
			for (String blockType : blocksType) {
				OABlockImpl block = Blocks.LIST.get(blockType);
				if (block != null && block.isEnabled() && block.isTNTEnabled()) {
					Integer temp = blocks.get(block);
					if (temp == null)
						temp = 1;
					else
						temp += 1;
					
					total++;
					blocks.put(block, temp);
				}
			}
			
			if (total > 0) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_EVENT_BLOCK_TNT
						.replace("{player}", user != null ? user.getName() : "unknown")
						.replace("{number}", Integer.toString(total)), true);
				
				for (Map.Entry<OABlockImpl, Integer> e : blocks.entrySet()) {
					if (user != null
							&& ConfigMain.STATS_ENABLE
							&& ConfigMain.BLOCKS_TNT_MINING_COUNT_DESTROY
							&& e.getKey().isCountingOnDestroy()) {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						if (!user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY.toString()))
							plugin.getBlockManager().handleBlockDestroy(e.getKey(), player, e.getValue());
						
						if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND.toString())) {
							plugin.getBlockManager().handleBlockFound(e.getKey(), player, blockLocation, e.getValue());
						}
					}
					
					if (ConfigMain.ALERTS_ENABLE && (user == null || !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS.toString()))) {
						OAPlayerImpl player = user != null ? plugin.getPlayerManager().getPlayer(user.getUUID()) : null;
						plugin.getBlockManager().handleTNTDestroy(player, e.getKey(), blockLocation, e.getValue());
					}
				}
			}
		}
	}
	
	private void alert(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, block.getMaterialName(), BlockManager.MarkType.ALERT)) {
			boolean alertUsers = block.isAlertingUsers();
			boolean alertAdmins = block.isAlertingAdmins();
			
			// Light level system
			if (ConfigMain.BLOCKS_LIGHT_ENABLE
					&& lightLevel < block.getLightLevel()
					&& ConfigMain.BLOCKS_LIGHT_ALERTIFLOWER) {
				alertUsers = false;
				alertAdmins = false;
			}
			if (alertUsers || alertAdmins) {
				int numberOfBlocks = plugin.getBlockManager().countNearBlocks(blockLocation, block.getMaterialName(), BlockManager.MarkType.ALERT);
				
				if (numberOfBlocks > 0) {
					// Make it async
					final boolean fAlertUsers = alertUsers;
					final boolean fAlertAdmins = alertAdmins;
					plugin.getScheduler().runAsync(() -> {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						plugin.getBlockManager().handleAlerts(fAlertUsers, fAlertAdmins, player, block, blockLocation, numberOfBlocks);
					});
				}
			}
		}
	}
	
	private void store(User user, OABlockImpl block, int lightLevel) {
		if (block.isCountingOnDestroy() && (!ConfigMain.BLOCKS_LIGHT_ENABLE || !ConfigMain.BLOCKS_LIGHT_COUNTIFLOWER || lightLevel <= block.getLightLevel())) {
			plugin.getScheduler().runAsync(() -> {
				OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
				plugin.getBlockManager().handleBlockDestroy(block, player, 1);
			});
		}
	}
	
	private void found(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, block.getMaterialName(), BlockManager.MarkType.FOUND)) {
			if (!ConfigMain.BLOCKS_LIGHT_ENABLE || !ConfigMain.BLOCKS_LIGHT_COUNTIFLOWER || lightLevel <= block.getLightLevel()) {
				int numberOfBlocks = plugin.getBlockManager().countNearBlocks(blockLocation, block.getMaterialName(), BlockManager.MarkType.FOUND);
				if (numberOfBlocks > 0) {
					plugin.getScheduler().runAsync(() -> {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						plugin.getBlockManager().handleBlockFound(block, player, blockLocation, numberOfBlocks);
					});
				}
			}
		}
	}
}
