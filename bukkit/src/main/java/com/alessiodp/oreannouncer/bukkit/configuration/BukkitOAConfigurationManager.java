package com.alessiodp.oreannouncer.bukkit.configuration;

import com.alessiodp.core.bukkit.configuration.adapter.BukkitConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;

import java.nio.file.Path;

public class BukkitOAConfigurationManager extends OAConfigurationManager {
	
	public BukkitOAConfigurationManager(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BukkitConfigMain(plugin));
		getConfigs().add(new BukkitMessages(plugin));
	}

	@Override
	protected ConfigurationAdapter initializeConfigurationAdapter(Path configurationFile) {
		return new BukkitConfigurationAdapter(configurationFile);
	}
	
	@Override
	protected void performChanges() {
		super.performChanges();
		OAPlaceholder.setupFormats((OreAnnouncerPlugin) plugin);
	}
}
