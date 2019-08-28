package com.alessiodp.oreannouncer.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class JoinLeaveListener {
	protected final OreAnnouncerPlugin plugin;
	
	protected void onPlayerJoin(User user) {
		// Make it async
		plugin.getScheduler().runAsync(() -> plugin.getLoginAlertsManager().sendAlerts(user));
	}
	
	protected void onPlayerQuit(User user) {
		// Make it async
		plugin.getScheduler().runAsync(() -> plugin.getPlayerManager().unloadPlayer(user.getUUID()));
	}
}
