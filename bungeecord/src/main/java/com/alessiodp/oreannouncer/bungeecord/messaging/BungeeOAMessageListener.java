package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bungeecord.messaging.bungee.BungeeOABungeecordListener;
import org.jetbrains.annotations.NotNull;

public class BungeeOAMessageListener extends BungeeMessageListener {
	
	public BungeeOAMessageListener(@NotNull ADPPlugin plugin) {
		super(
				plugin,
				new BungeeOABungeecordListener(plugin),
				null
		);
	}
}
