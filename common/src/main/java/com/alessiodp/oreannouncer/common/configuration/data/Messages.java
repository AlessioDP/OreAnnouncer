package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

import java.util.List;

public abstract class Messages extends ConfigurationFile {
	// OreAnnouncer messages
	@ConfigOption(path = "oreannouncer.update-available")
	public static String OREANNOUNCER_UPDATEAVAILABLE;
	@ConfigOption(path = "oreannouncer.configuration-outdated")
	public static String OREANNOUNCER_CONFIGURATION_OUTDATED;
	@ConfigOption(path = "oreannouncer.no-permission")
	public static String OREANNOUNCER_NOPERMISSION;
	
	@ConfigOption(path = "oreannouncer.common-messages.invalid-command")
	public static String OREANNOUNCER_COMMON_INVALIDCMD;
	@ConfigOption(path = "oreannouncer.common-messages.configuration-reloaded")
	public static String OREANNOUNCER_COMMON_CONFIGRELOAD;
	
	// Event messages
	@ConfigOption(path = "alerts.user")
	public static String ALERTS_USER;
	@ConfigOption(path = "alerts.admin")
	public static String ALERTS_ADMIN;
	@ConfigOption(path = "alerts.console")
	public static String ALERTS_CONSOLE;
	@ConfigOption(path = "alerts.count.user")
	public static String ALERTS_COUNT_USER;
	@ConfigOption(path = "alerts.count.admin")
	public static String ALERTS_COUNT_ADMIN;
	@ConfigOption(path = "alerts.count.console")
	public static String ALERTS_COUNT_CONSOLE;
	@ConfigOption(path = "alerts.tnt.user")
	public static String ALERTS_TNT_USER;
	@ConfigOption(path = "alerts.tnt.admin")
	public static String ALERTS_TNT_ADMIN;
	@ConfigOption(path = "alerts.tnt.console")
	public static String ALERTS_TNT_CONSOLE;
	@ConfigOption(path = "alerts.tnt.player-user")
	public static String ALERTS_TNT_PLAYER_USER;
	@ConfigOption(path = "alerts.tnt.player-admin")
	public static String ALERTS_TNT_PLAYER_ADMIN;
	@ConfigOption(path = "alerts.tnt.player-console")
	public static String ALERTS_TNT_PLAYER_CONSOLE;
	
	
	// Command messages
	@ConfigOption(path = "commands.alerts.toggle-on")
	public static String CMD_ALERTS_TOGGLEON;
	@ConfigOption(path = "commands.alerts.toggle-off")
	public static String CMD_ALERTS_TOGGLEOFF;
	@ConfigOption(path = "commands.alerts.wrong-command")
	public static String CMD_ALERTS_WRONGCMD;
	
	@ConfigOption(path = "commands.stats.content")
	public static List<String> CMD_STATS_CONTENT;
	@ConfigOption(path = "commands.stats.player-not-found")
	public static String CMD_STATS_PLAYERNOTFOUND;
	
	@ConfigOption(path = "commands.top.header")
	public static String CMD_TOP_HEADER;
	@ConfigOption(path = "commands.top.footer")
	public static String CMD_TOP_FOOTER;
	@ConfigOption(path = "commands.top.no-one")
	public static String CMD_TOP_NOONE;
	@ConfigOption(path = "commands.top.format-player")
	public static String CMD_TOP_FORMATPLAYER;
	@ConfigOption(path = "commands.top.wrong-command")
	public static String CMD_TOP_WRONGCMD;
	
	@ConfigOption(path = "commands.version.updated")
	public static String CMD_VERSION_UPDATED;
	@ConfigOption(path = "commands.version.outdated")
	public static String CMD_VERSION_OUTDATED;
	
	
	// Help messages
	@ConfigOption(path = "help.header")
	public static String HELP_HEADER;
	@ConfigOption(path = "help.footer")
	public static String HELP_FOOTER;
	@ConfigOption(path = "help.console-help")
	public static List<String> HELP_CONSOLEHELP;
	
	@ConfigOption(path = "help.commands.help")
	public static String HELP_CMD_HELP;
	@ConfigOption(path = "help.commands.alerts")
	public static String HELP_CMD_ALERTS;
	@ConfigOption(path = "help.commands.reload")
	public static String HELP_CMD_RELOAD;
	@ConfigOption(path = "help.commands.stats")
	public static String HELP_CMD_STATS;
	@ConfigOption(path = "help.commands.top")
	public static String HELP_CMD_TOP;
	@ConfigOption(path = "help.commands.version")
	public static String HELP_CMD_VERSION;
	
	protected Messages(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	public void loadDefaults() {
		loadDefaultConfigOptions();
	}
	
	@Override
	public void loadConfiguration() {
		loadConfigOptions();
	}
}
