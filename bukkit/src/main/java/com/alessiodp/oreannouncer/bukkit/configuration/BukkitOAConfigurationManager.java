package com.alessiodp.oreannouncer.bukkit.configuration;

import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;

public class BukkitOAConfigurationManager extends OAConfigurationManager {
	
	public BukkitOAConfigurationManager(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BukkitConfigMain(plugin));
		getConfigs().add(new BukkitMessages(plugin));
		getConfigs().add(new Blocks(plugin));
	}
	
	@Override
	protected void performChanges() {
		super.performChanges();
		OAPlaceholder.setupFormats((OreAnnouncerPlugin) plugin);
	}
}
