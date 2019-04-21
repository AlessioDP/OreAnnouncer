package com.alessiodp.oreannouncer.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BungeeConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bungee/config.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUNGEE_CONFIG;
	
	// Blocks settings
	public static List<String> BLOCKS_LISTALLOWED;
	
	public BungeeConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Blocks settings
		BLOCKS_LISTALLOWED = new ArrayList<>();
		BLOCKS_LISTALLOWED.add("*");
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Blocks settings
		BLOCKS_LISTALLOWED = confAdapter.getStringList("blocks.list-allowed", BLOCKS_LISTALLOWED);
	}
}
