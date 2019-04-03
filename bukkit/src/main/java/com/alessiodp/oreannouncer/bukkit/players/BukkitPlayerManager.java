package com.alessiodp.oreannouncer.bukkit.players;

import com.alessiodp.oreannouncer.bukkit.players.objects.BukkitOAPlayerImpl;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

import java.util.UUID;

public class BukkitPlayerManager extends PlayerManager {
	
	public BukkitPlayerManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public OAPlayerImpl initializePlayer(UUID playerUUID) {
		return new BukkitOAPlayerImpl(plugin,playerUUID);
	}
}
