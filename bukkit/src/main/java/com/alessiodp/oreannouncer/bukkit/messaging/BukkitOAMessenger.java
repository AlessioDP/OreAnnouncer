package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

public class BukkitOAMessenger extends ADPMessenger {
	public BukkitOAMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new BukkitOAMessageDispatcher(plugin);
		messageListener = new BukkitOAMessageListener(plugin);
	}
	
	@Override
	public void reload() {
		if (((OreAnnouncerPlugin) plugin).isBungeeCordEnabled()) {
			messageDispatcher.register();
			messageListener.register();
		} else {
			disable();
		}
	}
}