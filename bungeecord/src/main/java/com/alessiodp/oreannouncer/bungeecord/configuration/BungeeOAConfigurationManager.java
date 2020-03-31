package com.alessiodp.oreannouncer.bungeecord.configuration;

import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;

public class BungeeOAConfigurationManager extends OAConfigurationManager {
	
	public BungeeOAConfigurationManager(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BungeeConfigMain(plugin));
		getConfigs().add(new BungeeMessages(plugin));
	}
}
