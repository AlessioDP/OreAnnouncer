package com.alessiodp.oreannouncer.bungeecord.players;

import com.alessiodp.oreannouncer.bungeecord.players.objects.BungeeOAPlayerImpl;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

import java.util.UUID;

public class BungeePlayerManager extends PlayerManager {
	
	public BungeePlayerManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public OAPlayerImpl initializePlayer(UUID playerUUID) {
		return new BungeeOAPlayerImpl(plugin, playerUUID);
	}
}
