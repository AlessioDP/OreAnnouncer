package com.alessiodp.oreannouncer.bukkit.bootstrap;

import com.alessiodp.core.bukkit.bootstrap.ADPBukkitBootstrap;
import com.alessiodp.oreannouncer.bukkit.BukkitOreAnnouncerPlugin;

public class BukkitOreAnnouncerBootstrap extends ADPBukkitBootstrap {
	public BukkitOreAnnouncerBootstrap() {
		plugin = new BukkitOreAnnouncerPlugin(this);
	}
}

