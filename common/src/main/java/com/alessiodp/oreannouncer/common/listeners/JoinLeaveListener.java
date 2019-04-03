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
	private final OreAnnouncerPlugin plugin;
	
	protected void onPlayerJoin(User user) {
		// Make it async
		plugin.getScheduler().runAsync(() -> {
			OAPlayerImpl player = plugin.getPlayerManager().loadPlayer(user.getUUID());
			
			if (ConfigMain.OREANNOUNCER_UPDATES_WARN && user.hasPermission(OreAnnouncerPermission.ADMIN_UPDATES.toString())) {
				if (!plugin.getAdpUpdater().getFoundVersion().isEmpty()) {
					player.sendMessage(Messages.OREANNOUNCER_UPDATEAVAILABLE
							.replace("%version%", plugin.getAdpUpdater().getFoundVersion())
							.replace("%thisversion%", plugin.getVersion()));
				}
			}
		});
	}
	
	protected void onPlayerQuit(User user) {
		// Make it async
		plugin.getScheduler().runAsync(() -> plugin.getPlayerManager().unloadPlayer(user.getUUID()));
	}
}
