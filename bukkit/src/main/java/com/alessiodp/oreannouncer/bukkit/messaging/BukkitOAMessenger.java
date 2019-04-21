package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

public class BukkitOAMessenger extends ADPMessenger {
	public BukkitOAMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new BukkitMessageDispatcher(plugin);
	}
	
	@Override
	public void reload() {
		if (((OreAnnouncerPlugin) plugin).isBungeeCordEnabled()) {
			messageDispatcher.register();
		} else {
			disable();
		}
	}
}