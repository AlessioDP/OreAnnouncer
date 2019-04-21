package com.alessiodp.oreannouncer.bungeecord.bootstrap;

import com.alessiodp.core.bungeecord.bootstrap.ADPBungeeBootstrap;
import com.alessiodp.oreannouncer.bungeecord.BungeeOreAnnouncerPlugin;

public class BungeeOreAnnouncerBootstrap extends ADPBungeeBootstrap {
	public BungeeOreAnnouncerBootstrap() {
		plugin = new BungeeOreAnnouncerPlugin(this);
	}
}
