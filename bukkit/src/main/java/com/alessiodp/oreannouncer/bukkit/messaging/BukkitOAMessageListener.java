package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bukkit.messaging.bungee.BukkitOABungeecordListener;
import lombok.NonNull;

public class BukkitOAMessageListener extends BukkitMessageListener {
	
	public BukkitOAMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin, new BukkitOABungeecordListener(plugin));
	}
}
