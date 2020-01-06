package com.alessiodp.oreannouncer.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Getter;

public class BukkitConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bukkit/config.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUKKIT_CONFIG;
	
	// OreAnnouncer settings
	@ConfigOption(path = "oreannouncer.bungeecord.enable")
	public static boolean		OREANNOUNCER_BUNGEECORD_ENABLE;
	
	public BukkitConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}
