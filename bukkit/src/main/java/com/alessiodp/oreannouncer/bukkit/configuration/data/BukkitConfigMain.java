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
	@ConfigOption(path = "oreannouncer.bungeecord.server-name")
	public static String		OREANNOUNCER_BUNGEECORD_SERVER_NAME;
	@ConfigOption(path = "oreannouncer.bungeecord.server-id")
	public static String		OREANNOUNCER_BUNGEECORD_SERVER_ID;
	
	// Alerts settings
	@ConfigOption(path = "alerts.discordsrv.enable")
	public static boolean		ALERTS_DISCORDSRV_ENABLE;
	@ConfigOption(path = "alerts.discordsrv.message-format")
	public static String		ALERTS_DISCORDSRV_MESSAGE_FORMAT;
	@ConfigOption(path = "alerts.discordsrv.embed-avatars")
	public static boolean		ALERTS_DISCORDSRV_EMBED_AVATARS;
	@ConfigOption(path = "alerts.discordsrv.channels.user")
	public static String		ALERTS_DISCORDSRV_CHANNELS_USER;
	@ConfigOption(path = "alerts.discordsrv.channels.admin")
	public static String		ALERTS_DISCORDSRV_CHANNELS_ADMIN;
	
	public BukkitConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}
