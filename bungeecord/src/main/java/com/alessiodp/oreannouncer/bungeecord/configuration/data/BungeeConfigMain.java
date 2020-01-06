package com.alessiodp.oreannouncer.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.List;

public class BungeeConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bungee/config.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUNGEE_CONFIG;
	
	// Blocks settings
	@ConfigOption(path = "blocks.list-allowed")
	public static List<String> BLOCKS_LISTALLOWED;
	
	public BungeeConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}
