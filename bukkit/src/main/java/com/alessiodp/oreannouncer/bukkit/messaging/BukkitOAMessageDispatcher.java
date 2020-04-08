package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import lombok.NonNull;

public class BukkitOAMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitOAMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean sendPacket(ADPPacket packet) {
		return super.sendPacket(((OAPacket) packet)
				.setServerName(BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_NAME)
				.setServerId(BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_ID));
	}
}
