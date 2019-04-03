package com.alessiodp.oreannouncer.bukkit.configuration.data;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Getter;

public class BukkitConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bukkit/config.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUKKIT_CONFIG;
	
	public BukkitConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}
