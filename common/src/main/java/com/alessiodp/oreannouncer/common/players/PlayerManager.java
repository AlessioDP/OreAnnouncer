package com.alessiodp.oreannouncer.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerManager {
	protected final OreAnnouncerPlugin plugin;
	
	@Getter private final HashMap<UUID, OAPlayerImpl> cachePlayers;
	
	public PlayerManager(@NonNull OreAnnouncerPlugin plugin) {
		this.plugin = plugin;
		cachePlayers = new HashMap<>();
	}
	
	public void reload() {
		cachePlayers.clear();
		
		for (User user : plugin.getOnlinePlayers()) {
			loadPlayer(user.getUUID());
		}
	}
	
	public abstract OAPlayerImpl initializePlayer(UUID playerUUID);
	
	public OAPlayerImpl loadPlayer(UUID uuid) {
		OAPlayerImpl ret = getPlayer(uuid);
		getCachePlayers().put(uuid, ret);
		return ret;
	}
	
	public boolean reloadPlayer(UUID uuid) {
		// Reload the player from database
		// Used by packet UPDATE_PLAYER
		if (getCachePlayers().containsKey(uuid)) {
			unloadPlayer(uuid);
			loadPlayer(uuid);
			
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_PLAYER_RELOADED, uuid.toString()), true);
			return true;
		}
		return false;
	}
	
	public void unloadPlayer(UUID uuid) {
		getCachePlayers().remove(uuid);
	}
	
	public OAPlayerImpl getPlayer(UUID uuid) {
		OAPlayerImpl ret = null;
		if (uuid != null) {
			ret = getCachePlayers().get(uuid);
			if (ret != null) {
				// Get player from online list
				plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_PLAYER_GET_LIST, ret.getName(), ret.getPlayerUUID()), true);
			} else {
				// Get player from database
				ret = plugin.getDatabaseManager().getPlayer(uuid);
				
				// Load new player
				if (ret == null) {
					ret = initializePlayer(uuid);
					plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_PLAYER_GET_NEW, ret.getName(), ret.getPlayerUUID()), true);
				} else {
					plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_PLAYER_GET_DATABASE, ret.getName(), ret.getPlayerUUID()), true);
				}
			}
		}
		return ret;
	}
	
	public int getTotalBlocksDestroy(OAPlayerImpl player, OABlockImpl block) {
		int ret = 0;
		if (player != null) {
			Map<OABlockImpl, Integer> blocks = plugin.getDatabaseManager().getStatsPlayer(OADatabaseManager.ValueType.DESTROY, player.getPlayerUUID());
			for (Map.Entry<OABlockImpl, Integer> e : blocks.entrySet()) {
				if (block == null || block.equals(e.getKey()))
					ret = ret + e.getValue();
			}
		}
		return ret;
	}
	
	public int getTotalBlocksFound(OAPlayerImpl player, OABlockImpl block, long sinceTimestamp) {
		int ret = 0;
		if (player != null) {
			BlocksFoundResult res = plugin.getDatabaseManager().getBlockFound(player.getPlayerUUID(), block, sinceTimestamp);
			if (res != null)
				ret = res.getTotal();
		}
		return ret;
	}
}
