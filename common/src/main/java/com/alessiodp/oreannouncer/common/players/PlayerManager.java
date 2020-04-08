package com.alessiodp.oreannouncer.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Set;
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
			if (block != null) {
				BlockDestroy bd = plugin.getDatabaseManager().getBlockDestroy(player.getPlayerUUID(), block);
				if (bd != null)
					ret = bd.getDestroyCount();
			} else {
				Set<BlockDestroy> blocks = plugin.getDatabaseManager().getAllBlockDestroy(player.getPlayerUUID());
				for (BlockDestroy bd : blocks)
					ret = ret + bd.getDestroyCount();
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
