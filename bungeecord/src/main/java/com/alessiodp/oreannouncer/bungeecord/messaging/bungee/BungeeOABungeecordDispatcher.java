package com.alessiodp.oreannouncer.bungeecord.messaging.bungee;

import com.alessiodp.core.bungeecord.messaging.bungee.BungeeBungeecordDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class BungeeOABungeecordDispatcher extends BungeeBungeecordDispatcher {
	public BungeeOABungeecordDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
}
