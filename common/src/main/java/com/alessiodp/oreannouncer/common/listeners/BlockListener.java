package com.alessiodp.oreannouncer.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BlockListener {
	protected final OreAnnouncerPlugin plugin;
	
	protected void onBlockBreak(User user, String blockType, int lightLevel, boolean hasSilkTouch, ADPLocation blockLocation) {
		if (!(ConfigMain.BLOCKS_BYPASS_SILKTOUCH && hasSilkTouch)
				&& (ConfigMain.STATS_ENABLE || ConfigMain.ALERTS_ENABLE)
				&& (!ConfigMain.BLOCKS_BYPASS_PLAYERBLOCKS || !plugin.getBlockManager().isBlockMarked(blockLocation, BlockManager.MarkType.STORE))) {
			OABlockImpl block = Blocks.searchBlock(blockType);
			if (block != null && block.isEnabled()) {
				plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_EVENT_BLOCK_BREAK, user.getName(), blockType), true);
				
				// Store information into database
				if (ConfigMain.STATS_ENABLE) {
					if (!user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY))
						store(user, block, blockLocation, lightLevel);
					
					if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND))
						found(user, block, blockLocation, lightLevel);
				}
				
				if (ConfigMain.ALERTS_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS)) {
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
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, BlockManager.MarkType.ALERT)
				|| !plugin.getBlockManager().isBlockMarked(blockLocation, BlockManager.MarkType.FOUND)) {
			OABlockImpl block = Blocks.searchBlock(blockType);
			if (block != null && block.isEnabled()) {
				plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_EVENT_BLOCK_PLACE, user.getName(), blockType), true);
				
				// Mark block so it won't be counted
				plugin.getBlockManager().markBlock(blockLocation, block, BlockManager.MarkType.ALERT);
				plugin.getBlockManager().markBlock(blockLocation, block, BlockManager.MarkType.FOUND);
				plugin.getBlockManager().markBlock(blockLocation, block, BlockManager.MarkType.STORE);
			}
		}
	}
	
	protected void onTNTExplode(@Nullable User user, ArrayList<String> blocksType, ADPLocation blockLocation) {
		if (ConfigMain.STATS_ENABLE || ConfigMain.ALERTS_ENABLE) {
			HashMap<OABlockImpl, Integer> blocks = new HashMap<>();
			int total = 0;
			for (String blockType : blocksType) {
				OABlockImpl block = Blocks.searchBlock(blockType);
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
				plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_EVENT_BLOCK_TNT, user != null ? user.getName() : "unknown", total), true);
				
				for (Map.Entry<OABlockImpl, Integer> e : blocks.entrySet()) {
					if (user != null
							&& ConfigMain.STATS_ENABLE
							&& ConfigMain.BLOCKS_TNT_MINING_COUNT_DESTROY
							&& e.getKey().isCountingOnDestroy()) {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						if (!user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY)
								&& (player == null || !(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_DESTROY) || !player.isWhitelisted()))
							plugin.getBlockManager().handleBlockDestroy(new BlockData(player, e.getKey(), e.getValue())
									.setLightLevel(15)
									.setLocation(blockLocation));
						
						if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE && !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND)
								&& (player == null || !(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_FOUND) || !player.isWhitelisted()))
							plugin.getBlockManager().handleBlockFound(new BlockData(player, e.getKey(), e.getValue())
									.setLightLevel(15)
									.setLocation(blockLocation));
					}
					
					if (ConfigMain.ALERTS_ENABLE && (user == null || !user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS))) {
						OAPlayerImpl player = user != null ? plugin.getPlayerManager().getPlayer(user.getUUID()) : null;
						if (player == null || !(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_DESTROY) || !player.isWhitelisted())
							plugin.getBlockManager().handleTNTDestroy(new BlockData(player, e.getKey(), e.getValue()).setLocation(blockLocation));
					}
				}
			}
		}
	}
	
	private void alert(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, BlockManager.MarkType.ALERT)) {
			boolean alertUsers = block.isAlertingUsers();
			boolean alertAdmins = block.isAlertingAdmins();
			
			if ((ConfigMain.BLOCKS_LIGHT_ENABLE && ConfigMain.BLOCKS_LIGHT_ALERTIFLOWER && lightLevel < block.getLightLevel())
					|| (ConfigMain.BLOCKS_HEIGHT_ENABLE && ConfigMain.BLOCKS_HEIGHT_ALERTIFLOWER && block.getHeightLevel() > 0 && blockLocation.getY() > block.getHeightLevel())) {
				alertUsers = false;
				alertAdmins = false;
			}
			if (alertUsers || alertAdmins) {
				int numberOfBlocks = plugin.getBlockManager().countNearBlocks(blockLocation, block, BlockManager.MarkType.ALERT);
				
				if (numberOfBlocks > 0) {
					// Make it async
					final boolean fAlertUsers = alertUsers;
					final boolean fAlertAdmins = alertAdmins;
					plugin.getScheduler().runAsync(() -> {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						if (!(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_ALERTS) || !player.isWhitelisted())
							plugin.getBlockManager().handleAlerts(new BlockData(player, block, numberOfBlocks)
									.setAlertUsers(fAlertUsers)
									.setAlertAdmins(fAlertAdmins)
									.setLocation(blockLocation)
									.setLightLevel(lightLevel)
							);
					});
				}
			}
		}
	}
	
	private void store(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		if (block.isCountingOnDestroy()
				&& (!ConfigMain.BLOCKS_LIGHT_ENABLE || !ConfigMain.BLOCKS_LIGHT_COUNTIFLOWER || lightLevel <= block.getLightLevel())
				&& (!ConfigMain.BLOCKS_HEIGHT_ENABLE || !ConfigMain.BLOCKS_HEIGHT_COUNTIFLOWER || block.getHeightLevel() <= 0 || blockLocation.getY() <= block.getHeightLevel())) {
			plugin.getScheduler().runAsync(() -> {
				OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
				if (!(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_DESTROY) || !player.isWhitelisted())
					plugin.getBlockManager().handleBlockDestroy(new BlockData(player, block, 1)
							.setLocation(blockLocation));
			});
		}
	}
	
	private void found(User user, OABlockImpl block, ADPLocation blockLocation, int lightLevel) {
		if (!plugin.getBlockManager().isBlockMarked(blockLocation, BlockManager.MarkType.FOUND)) {
			if ((!ConfigMain.BLOCKS_LIGHT_ENABLE || !ConfigMain.BLOCKS_LIGHT_COUNTIFLOWER || lightLevel <= block.getLightLevel())
					&& (!ConfigMain.BLOCKS_HEIGHT_ENABLE || !ConfigMain.BLOCKS_HEIGHT_COUNTIFLOWER || block.getHeightLevel() <= 0 || blockLocation.getY() <= block.getHeightLevel())) {
				int numberOfBlocks = plugin.getBlockManager().countNearBlocks(blockLocation, block, BlockManager.MarkType.FOUND);
				if (numberOfBlocks > 0) {
					plugin.getScheduler().runAsync(() -> {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
						if (!(ConfigMain.WHITELIST_ENABLE && ConfigMain.WHITELIST_BYPASS_FOUND) || !player.isWhitelisted())
							plugin.getBlockManager().handleBlockFound(new BlockData(player, block, numberOfBlocks)
									.setLocation(blockLocation)
									.setLightLevel(lightLevel)
							);
					});
				}
			}
		}
	}
}
