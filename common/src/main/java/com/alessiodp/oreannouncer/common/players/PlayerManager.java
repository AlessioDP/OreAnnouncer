package com.alessiodp.oreannouncer.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerManager {
	protected final OreAnnouncerPlugin plugin;
	
	@Getter private final HashMap<UUID, OAPlayerImpl> listPlayers;
	
	public PlayerManager(@NonNull OreAnnouncerPlugin plugin) {
		this.plugin = plugin;
		listPlayers = new HashMap<>();
	}
	
	public void reload() {
		listPlayers.clear();
		
		for (User user : plugin.getOnlinePlayers()) {
			loadPlayer(user.getUUID());
		}
	}
	
	public abstract OAPlayerImpl initializePlayer(UUID playerUUID);
	
	public OAPlayerImpl loadPlayer(UUID uuid) {
		OAPlayerImpl ret = getPlayer(uuid);
		getListPlayers().put(uuid, ret);
		return ret;
	}
	
	public void unloadPlayer(UUID uuid) {
		getListPlayers().remove(uuid);
	}
	
	public OAPlayerImpl getPlayer(UUID uuid) {
		OAPlayerImpl ret;
		if (getListPlayers().containsKey(uuid)) {
			// Get player from online list
			ret = getListPlayers().get(uuid);
		} else {
			// Get player from database
			ret = plugin.getDatabaseManager().getPlayer(uuid);
			
			// Load new player
			if (ret == null)
				ret = initializePlayer(uuid);
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
			ret = plugin
					.getDatabaseManager().getBlockFound(player.getPlayerUUID(), block, sinceTimestamp)
					.getTotal();
		}
		return ret;
	}
}
