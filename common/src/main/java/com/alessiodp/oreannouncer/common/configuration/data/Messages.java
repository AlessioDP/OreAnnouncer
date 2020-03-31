package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

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
	
	@ConfigOption(path = "oreannouncer.syntax.wrong-message")
	public static String OREANNOUNCER_SYNTAX_WRONGMESSAGE;
	@ConfigOption(path = "oreannouncer.syntax.block")
	public static String OREANNOUNCER_SYNTAX_BLOCK;
	@ConfigOption(path = "oreannouncer.syntax.order")
	public static String OREANNOUNCER_SYNTAX_ORDER;
	@ConfigOption(path = "oreannouncer.syntax.player")
	public static String OREANNOUNCER_SYNTAX_PLAYER;
	@ConfigOption(path = "oreannouncer.syntax.page")
	public static String OREANNOUNCER_SYNTAX_PAGE;
	
	// Event messages
	@ConfigOption(path = "alerts.user", nullable = true)
	public static String ALERTS_USER;
	@ConfigOption(path = "alerts.admin", nullable = true)
	public static String ALERTS_ADMIN;
	@ConfigOption(path = "alerts.console", nullable = true)
	public static String ALERTS_CONSOLE;
	@ConfigOption(path = "alerts.count.user", nullable = true)
	public static String ALERTS_COUNT_USER;
	@ConfigOption(path = "alerts.count.admin", nullable = true)
	public static String ALERTS_COUNT_ADMIN;
	@ConfigOption(path = "alerts.count.console", nullable = true)
	public static String ALERTS_COUNT_CONSOLE;
	@ConfigOption(path = "alerts.tnt.user", nullable = true)
	public static String ALERTS_TNT_USER;
	@ConfigOption(path = "alerts.tnt.admin", nullable = true)
	public static String ALERTS_TNT_ADMIN;
	@ConfigOption(path = "alerts.tnt.console", nullable = true)
	public static String ALERTS_TNT_CONSOLE;
	@ConfigOption(path = "alerts.tnt.player-user", nullable = true)
	public static String ALERTS_TNT_PLAYER_USER;
	@ConfigOption(path = "alerts.tnt.player-admin", nullable = true)
	public static String ALERTS_TNT_PLAYER_ADMIN;
	@ConfigOption(path = "alerts.tnt.player-console", nullable = true)
	public static String ALERTS_TNT_PLAYER_CONSOLE;
	
	
	// Command messages
	@ConfigOption(path = "commands.alerts.toggle-on")
	public static String CMD_ALERTS_TOGGLEON;
	@ConfigOption(path = "commands.alerts.toggle-off")
	public static String CMD_ALERTS_TOGGLEOFF;
	
	@ConfigOption(path = "commands.log.header-player")
	public static String CMD_LOG_HEADER_PLAYER;
	@ConfigOption(path = "commands.log.footer-player")
	public static String CMD_LOG_FOOTER_PLAYER;
	@ConfigOption(path = "commands.log.header-block")
	public static String CMD_LOG_HEADER_BLOCK;
	@ConfigOption(path = "commands.log.footer-block")
	public static String CMD_LOG_FOOTER_BLOCK;
	@ConfigOption(path = "commands.log.header-block-general")
	public static String CMD_LOG_HEADER_BLOCK_GENERAL;
	@ConfigOption(path = "commands.log.footer-block-general")
	public static String CMD_LOG_FOOTER_BLOCK_GENERAL;
	@ConfigOption(path = "commands.log.format-player-block")
	public static String CMD_LOG_FORMAT_PLAYER_BLOCK;
	@ConfigOption(path = "commands.log.format-general-block")
	public static String CMD_LOG_FORMAT_GENERAL_BLOCK;
	@ConfigOption(path = "commands.log.nothing")
	public static String CMD_LOG_NOTHING;
	@ConfigOption(path = "commands.log.player-not-found")
	public static String CMD_LOG_PLAYERNOTFOUND;
	@ConfigOption(path = "commands.log.invalid-block")
	public static String CMD_LOG_INVALID_BLOCK;
	
	@ConfigOption(path = "commands.stats.header")
	public static String CMD_STATS_HEADER;
	@ConfigOption(path = "commands.stats.footer")
	public static String CMD_STATS_FOOTER;
	@ConfigOption(path = "commands.stats.nothing")
	public static String CMD_STATS_NOTHING;
	@ConfigOption(path = "commands.stats.format-block")
	public static String CMD_STATS_FORMATPLAYER;
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
	@ConfigOption(path = "commands.top.word-destroy")
	public static String CMD_TOP_WORD_DESTROY;
	@ConfigOption(path = "commands.top.word-found")
	public static String CMD_TOP_WORD_FOUND;
	@ConfigOption(path = "commands.top.invalid-order")
	public static String CMD_TOP_INVALID_ORDER;
	@ConfigOption(path = "commands.top.invalid-block")
	public static String CMD_TOP_INVALID_BLOCK;
	
	@ConfigOption(path = "commands.version.updated")
	public static String CMD_VERSION_UPDATED;
	@ConfigOption(path = "commands.version.outdated")
	public static String CMD_VERSION_OUTDATED;
	
	
	// Help messages
	@ConfigOption(path = "help.header")
	public static String HELP_HEADER;
	@ConfigOption(path = "help.footer")
	public static String HELP_FOOTER;
	@ConfigOption(path = "help.perform-command")
	public static String HELP_PERFORM_COMMAND;
	@ConfigOption(path = "help.console-help.header")
	public static String HELP_CONSOLEHELP_HEADER;
	@ConfigOption(path = "help.console-help.command")
	public static String HELP_CONSOLEHELP_COMMAND;
	
	@ConfigOption(path = "help.commands.help")
	public static String HELP_CMD_HELP;
	@ConfigOption(path = "help.commands.alerts")
	public static String HELP_CMD_ALERTS;
	@ConfigOption(path = "help.commands.log")
	public static String HELP_CMD_LOG;
	@ConfigOption(path = "help.commands.reload")
	public static String HELP_CMD_RELOAD;
	@ConfigOption(path = "help.commands.stats")
	public static String HELP_CMD_STATS;
	@ConfigOption(path = "help.commands.top")
	public static String HELP_CMD_TOP;
	@ConfigOption(path = "help.commands.version")
	public static String HELP_CMD_VERSION;
	
	@ConfigOption(path = "help.command-descriptions.help")
	public static String HELP_CMD_DESCRIPTIONS_HELP;
	@ConfigOption(path = "help.command-descriptions.alerts")
	public static String HELP_CMD_DESCRIPTIONS_ALERTS;
	@ConfigOption(path = "help.command-descriptions.log")
	public static String HELP_CMD_DESCRIPTIONS_LOG;
	@ConfigOption(path = "help.command-descriptions.reload")
	public static String HELP_CMD_DESCRIPTIONS_RELOAD;
	@ConfigOption(path = "help.command-descriptions.stats")
	public static String HELP_CMD_DESCRIPTIONS_STATS;
	@ConfigOption(path = "help.command-descriptions.top")
	public static String HELP_CMD_DESCRIPTIONS_TOP;
	@ConfigOption(path = "help.command-descriptions.version")
	public static String HELP_CMD_DESCRIPTIONS_VERSION;
	
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
