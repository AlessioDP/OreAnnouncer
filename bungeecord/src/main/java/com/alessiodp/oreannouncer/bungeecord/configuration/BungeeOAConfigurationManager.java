package com.alessiodp.oreannouncer.bungeecord.configuration;

import com.alessiodp.core.bungeecord.configuration.adapter.BungeeConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;

import java.nio.file.Path;

public class BungeeOAConfigurationManager extends OAConfigurationManager {
	
	public BungeeOAConfigurationManager(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BungeeConfigMain(plugin));
		getConfigs().add(new BungeeMessages(plugin));
	}
	
	@Override
	protected ConfigurationAdapter initializeConfigurationAdapter(Path configurationFile) {
		return new BungeeConfigurationAdapter(configurationFile);
	}
	
	@Override
	protected void performChanges() {
		super.performChanges();
		OAPlaceholder.setupFormats((OreAnnouncerPlugin) plugin);
	}
}
