package com.alessiodp.oreannouncer.bungeecord.listeners;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.listeners.JoinLeaveListener;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeJoinLeaveListener extends JoinLeaveListener implements Listener {
	
	public BungeeJoinLeaveListener(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PostLoginEvent event) {
		super.onPlayerJoin(new BungeeUser(plugin, event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerDisconnectEvent event) {
		super.onPlayerQuit(new BungeeUser(plugin, event.getPlayer()));
	}
}
