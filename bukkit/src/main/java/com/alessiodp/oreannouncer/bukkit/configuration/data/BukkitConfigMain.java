package com.alessiodp.oreannouncer.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Getter;

public class BukkitConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bukkit/config.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUKKIT_CONFIG;
	
	// OreAnnouncer settings
	public static boolean		OREANNOUNCER_BUNGEECORD_ENABLE;
	
	public BukkitConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// LastLoginAPI settings
		OREANNOUNCER_BUNGEECORD_ENABLE = false;
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// LastLoginAPI settings
		OREANNOUNCER_BUNGEECORD_ENABLE = confAdapter.getBoolean("oreannouncer.bungeecord.enable", OREANNOUNCER_BUNGEECORD_ENABLE);
	}
}
