package com.alessiodp.oreannouncer.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BlockListener {
	private final OreAnnouncerPlugin plugin;
	
	protected void onBlockBreak(User user, String blockType, int lightLevel, boolean blockHasMetadata, boolean hasSilkTouch, ADPLocation blockLocation) {
		if (!(ConfigMain.BLOCKS_BYPASS_SILKTOUCH && hasSilkTouch) && (ConfigMain.STATS_ENABLE || ConfigMain.ALERTS_ENABLE)) {
			OABlockImpl block = plugin.getBlockManager().getListBlocks().get(blockType);
			if (block != null) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_EVENT_BLOCK_BREAK
						.replace("{player}", user.getName())
						.replace("{block}", blockType), true);
				
				// Store information into database
				if (ConfigMain.STATS_ENABLE) {
					store(user, block, lightLevel);
				}
				
				if (!blockHasMetadata && ConfigMain.ALERTS_ENABLE) {
					// Handle alerts
					alert(user, block, blockLocation, lightLevel);
				}
			}
		}
		
		// Unmark broken block to prevent metadata ghosting
		plugin.getBlockManager().unmarkBlock(blockLocation);
	}
	
	protected void onBlockPlace(User user, String blockType, boolean blockHasMetadata, ADPLocation blockLocation) {
		if (!blockHasMetadata && ConfigMain.ALERTS_ENABLE) {
			OABlockImpl block = plugin.getBlockManager().getListBlocks().get(blockType);
			if (block != null) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_EVENT_BLOCK_PLACE
						.replace("{player}", user.getName())
						.replace("{block}", blockType), true);
				
				// Mark block so it won't be counted
				plugin.getBlockManager().markBlock(blockLocation, blockType);
			}
		}
	}
	
	private void alert(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		int numberOfBlocks = plugin.getBlockManager().countNearBlocks(blockLocation, block.getMaterialName());
		
		if (numberOfBlocks > 0) {
			// Make it async
			plugin.getScheduler().runAsync(() -> {
				OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
				boolean alertUsers = block.isAlertingUsers();
				boolean alertAdmins = block.isAlertingAdmins();
				
				// Light level system
				if (ConfigMain.BLOCKS_LIGHT_ENABLE && lightLevel < block.getLightLevel()) {
					if (ConfigMain.BLOCKS_LIGHT_ALERTIFLOWER) {
						alertUsers = false;
						alertAdmins = false;
					}
				}
				
				if (alertUsers || alertAdmins)
					plugin.getBlockManager().alertPlayers(alertUsers, alertAdmins, player, block, blockLocation, numberOfBlocks);
			});
		}
	}
	
	private void store(User user, OABlockImpl block, int lightLevel) {
		if (block.isCountingOnDestroy() && (!ConfigMain.BLOCKS_LIGHT_COUNTIFLOWER || lightLevel <= block.getLightLevel())) {
			plugin.getScheduler().runAsync(() -> {
				OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
				plugin.getBlockManager().countBlockDestroy(block, player, 1);
			});
		}
	}
}
