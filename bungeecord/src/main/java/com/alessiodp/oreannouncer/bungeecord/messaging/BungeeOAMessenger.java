package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;

public class BungeeOAMessenger extends ADPMessenger {
	public BungeeOAMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new BungeeOAMessageDispatcher(plugin);
		messageListener = new BungeeOAMessageListener(plugin);
	}
	
	@Override
	public void reload() {
		messageListener.register();
	}
}
