package com.alessiodp.oreannouncer.bukkit.messaging.bungee;

import com.alessiodp.core.bukkit.messaging.bungee.BukkitBungeecordDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitOABungeecordDispatcher extends BukkitBungeecordDispatcher {
	public BukkitOABungeecordDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, true, false, false);
	}
}
