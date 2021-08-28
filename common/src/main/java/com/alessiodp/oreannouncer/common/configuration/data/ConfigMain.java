package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

import java.util.List;

public abstract class ConfigMain extends ConfigurationFile {
	// OreAnnouncer settings
	@ConfigOption(path = "oreannouncer.updates.check")
	public static boolean		OREANNOUNCER_UPDATES_CHECK;
	@ConfigOption(path = "oreannouncer.updates.warn")
	public static boolean		OREANNOUNCER_UPDATES_WARN;
	@ConfigOption(path = "oreannouncer.logging.debug")
	public static boolean		OREANNOUNCER_LOGGING_DEBUG;
	@ConfigOption(path = "oreannouncer.logging.save-file.enable")
	public static boolean		OREANNOUNCER_LOGGING_SAVE_ENABLE;
	@ConfigOption(path = "oreannouncer.logging.save-file.format")
	public static String		OREANNOUNCER_LOGGING_SAVE_FORMAT;
	@ConfigOption(path = "oreannouncer.logging.save-file.file")
	public static String		OREANNOUNCER_LOGGING_SAVE_FILE;
	@ConfigOption(path = "oreannouncer.debug-command")
	public static boolean		OREANNOUNCER_DEBUG_COMMAND;
	
	// Storage settings
	@ConfigOption(path = "storage.database-storage-type")
	public static String		STORAGE_TYPE_DATABASE;
	@ConfigOption(path = "storage.storage-settings.general-sql-settings.prefix")
	public static String		STORAGE_SETTINGS_GENERAL_SQL_PREFIX;
	@ConfigOption(path = "storage.storage-settings.sqlite.database-file")
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	@ConfigOption(path = "storage.storage-settings.h2.database-file")
	public static String		STORAGE_SETTINGS_H2_DBFILE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.address")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_ADDRESS;
	@ConfigOption(path = "storage.storage-settings.remote-sql.port")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_PORT;
	@ConfigOption(path = "storage.storage-settings.remote-sql.database")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_DATABASE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.username")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_USERNAME;
	@ConfigOption(path = "storage.storage-settings.remote-sql.password")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_PASSWORD;
	@ConfigOption(path = "storage.storage-settings.remote-sql.pool-size")
	public static int			STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.connection-lifetime")
	public static int			STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME;
	@ConfigOption(path = "storage.storage-settings.remote-sql.use-ssl")
	public static boolean		STORAGE_SETTINGS_REMOTE_SQL_USESSL;
	@ConfigOption(path = "storage.storage-settings.remote-sql.charset")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_CHARSET;
	
	
	// Alerts settings
	@ConfigOption(path = "alerts.enable")
	public static boolean		ALERTS_ENABLE;
	@ConfigOption(path = "alerts.console")
	public static boolean		ALERTS_CONSOLE;
	@ConfigOption(path = "alerts.sound.volume", nullable = true)
	public static double		ALERTS_SOUND_VOLUME;
	@ConfigOption(path = "alerts.sound.pitch", nullable = true)
	public static double		ALERTS_SOUND_PITCH;
	@ConfigOption(path = "alerts.sound.default", nullable = true)
	public static String		ALERTS_SOUND_DEFAULT;
	@ConfigOption(path = "alerts.coordinates.enable", nullable = true)
	public static boolean		ALERTS_COORDINATES_ENABLE;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.enable", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_ENABLE;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.count", nullable = true)
	public static int			ALERTS_COORDINATES_HIDE_COUNT;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hidden-for.user", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_USER;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hidden-for.admin", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hidden-for.console", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hide.x", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_X;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hide.y", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_Y;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.hide.z", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_Z;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.format.text", nullable = true)
	public static String		ALERTS_COORDINATES_HIDE_FORMAT_TEXT;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.format.json", nullable = true)
	public static String		ALERTS_COORDINATES_HIDE_FORMAT_JSON;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.obfuscation.enable", nullable = true)
	public static boolean		ALERTS_COORDINATES_HIDE_OBFUSCATION_ENABLE;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.obfuscation.character", nullable = true)
	public static String		ALERTS_COORDINATES_HIDE_OBFUSCATION_CHARACTER;
	@ConfigOption(path = "alerts.coordinates.hide-real-coordinates.obfuscation.fixed-length", nullable = true)
	public static int			ALERTS_COORDINATES_HIDE_OBFUSCATION_FIXEDLENGTH;
	
	
	// Blocks settings
	@ConfigOption(path = "blocks.bypass-player-blocks", nullable = true)
	public static boolean		BLOCKS_BYPASS_PLAYERBLOCKS;
	@ConfigOption(path = "blocks.bypass-silk-touch", nullable = true)
	public static boolean		BLOCKS_BYPASS_SILKTOUCH;
	@ConfigOption(path = "blocks.bypass-secure-counter", nullable = true)
	public static boolean		BLOCKS_BYPASS_SECURE_COUNTER;
	@ConfigOption(path = "blocks.light-level.enable", nullable = true)
	public static boolean		BLOCKS_LIGHT_ENABLE;
	@ConfigOption(path = "blocks.light-level.alert-only-if-lower", nullable = true)
	public static boolean		BLOCKS_LIGHT_ALERTIFLOWER;
	@ConfigOption(path = "blocks.light-level.count-only-if-lower", nullable = true)
	public static boolean		BLOCKS_LIGHT_COUNTIFLOWER;
	@ConfigOption(path = "blocks.height-level.enable", nullable = true)
	public static boolean		BLOCKS_HEIGHT_ENABLE;
	@ConfigOption(path = "blocks.height-level.alert-only-if-lower", nullable = true)
	public static boolean		BLOCKS_HEIGHT_ALERTIFLOWER;
	@ConfigOption(path = "blocks.height-level.count-only-if-lower", nullable = true)
	public static boolean		BLOCKS_HEIGHT_COUNTIFLOWER;
	@ConfigOption(path = "blocks.tnt-mining.alert-on-tnt-mining", nullable = true)
	public static boolean		BLOCKS_TNT_MINING_ALERT_ON;
	@ConfigOption(path = "blocks.tnt-mining.try-to-catch-player", nullable = true)
	public static boolean		BLOCKS_TNT_MINING_CATCH_PLAYER;
	@ConfigOption(path = "blocks.tnt-mining.count-on-destroy", nullable = true)
	public static boolean		BLOCKS_TNT_MINING_COUNT_DESTROY;
	
	
	// Stats settings
	@ConfigOption(path = "stats.enable")
	public static boolean		STATS_ENABLE;
	@ConfigOption(path = "stats.order-by")
	public static String		STATS_ORDER_BY;
	@ConfigOption(path = "stats.values")
	public static String		STATS_VALUES;
	@ConfigOption(path = "stats.change-values")
	public static boolean		STATS_CHANGE_VALUES;
	@ConfigOption(path = "stats.blacklist-blocks.log", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_LOG;
	@ConfigOption(path = "stats.blacklist-blocks.stats", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_STATS;
	@ConfigOption(path = "stats.blacklist-blocks.top", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_TOP;
	@ConfigOption(path = "stats.advanced-count.enable")
	public static boolean		STATS_ADVANCED_COUNT_ENABLE;
	@ConfigOption(path = "stats.advanced-count.default-time-format-large")
	public static String		STATS_ADVANCED_COUNT_TIME_FORMAT_LARGE;
	@ConfigOption(path = "stats.advanced-count.default-time-format-medium")
	public static String		STATS_ADVANCED_COUNT_TIME_FORMAT_MEDIUM;
	@ConfigOption(path = "stats.advanced-count.default-time-format-small")
	public static String		STATS_ADVANCED_COUNT_TIME_FORMAT_SMALL;
	@ConfigOption(path = "stats.advanced-count.log-command.enable")
	public static boolean		STATS_ADVANCED_COUNT_LOG_ENABLE;
	@ConfigOption(path = "stats.advanced-count.log-command.format-date")
	public static String		STATS_ADVANCED_COUNT_LOG_FORMAT_DATE;
	@ConfigOption(path = "stats.advanced-count.log-command.format-date-elapsed-large")
	public static String		STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_LARGE;
	@ConfigOption(path = "stats.advanced-count.log-command.format-date-elapsed-medium")
	public static String		STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_MEDIUM;
	@ConfigOption(path = "stats.advanced-count.log-command.format-date-elapsed-small")
	public static String		STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALL;
	@ConfigOption(path = "stats.advanced-count.log-command.format-date-elapsed-smallest")
	public static String		STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALLEST;
	@ConfigOption(path = "stats.advanced-count.log-command.number-of-blocks")
	public static int			STATS_ADVANCED_COUNT_LOG_NUMBLOCKS;
	@ConfigOption(path = "stats.top.enable")
	public static boolean		STATS_TOP_ENABLE;
	@ConfigOption(path = "stats.top.order-by")
	public static String		STATS_TOP_ORDER_BY;
	@ConfigOption(path = "stats.top.change-order")
	public static boolean		STATS_TOP_CHANGE_ORDER;
	@ConfigOption(path = "stats.top.change-block")
	public static boolean		STATS_TOP_CHANGE_BLOCK;
	@ConfigOption(path = "stats.top.number-of-players")
	public static int			STATS_TOP_NUMPLAYERS;
	
	
	@ConfigOption(path = "whitelist.enable")
	public static boolean		WHITELIST_ENABLE;
	@ConfigOption(path = "whitelist.bypass.alerts")
	public static boolean		WHITELIST_BYPASS_ALERTS;
	@ConfigOption(path = "whitelist.bypass.destroy")
	public static boolean		WHITELIST_BYPASS_DESTROY;
	@ConfigOption(path = "whitelist.bypass.found")
	public static boolean		WHITELIST_BYPASS_FOUND;
	
	
	@ConfigOption(path = "execute-commands.enable", nullable = true)
	public static boolean		EXECUTE_COMMANDS_ENABLE;
	@ConfigOption(path = "execute-commands.run-as", nullable = true)
	public static String		EXECUTE_COMMANDS_RUN_AS;
	@ConfigOption(path = "execute-commands.on-destroy", nullable = true)
	public static List<String>	EXECUTE_COMMANDS_ON_DESTROY;
	@ConfigOption(path = "execute-commands.on-found", nullable = true)
	public static List<String>	EXECUTE_COMMANDS_ON_FOUND;
	
	
	// Commands settings
	@ConfigOption(path = "commands.tab-support")
	public static boolean		COMMANDS_TABSUPPORT;
	
	@ConfigOption(path = "commands.main-commands.oa.command")
	public static String		COMMANDS_MAIN_OA_COMMAND;
	@ConfigOption(path = "commands.main-commands.oa.aliases")
	public static List<String>	COMMANDS_MAIN_OA_ALIASES;
	
	@ConfigOption(path = "commands.sub-commands.help")
	public static String		COMMANDS_SUB_HELP;
	@ConfigOption(path = "commands.sub-commands.alerts")
	public static String		COMMANDS_SUB_ALERTS;
	@ConfigOption(path = "commands.sub-commands.debug")
	public static String		COMMANDS_SUB_DEBUG;
	@ConfigOption(path = "commands.sub-commands.log")
	public static String		COMMANDS_SUB_LOG;
	@ConfigOption(path = "commands.sub-commands.reload")
	public static String		COMMANDS_SUB_RELOAD;
	@ConfigOption(path = "commands.sub-commands.stats")
	public static String		COMMANDS_SUB_STATS;
	@ConfigOption(path = "commands.sub-commands.top")
	public static String		COMMANDS_SUB_TOP;
	@ConfigOption(path = "commands.sub-commands.version")
	public static String		COMMANDS_SUB_VERSION;
	@ConfigOption(path = "commands.sub-commands.whitelist")
	public static String		COMMANDS_SUB_WHITELIST;
	
	@ConfigOption(path = "commands.misc-commands.add")
	public static String		COMMANDS_MISC_ADD;
	@ConfigOption(path = "commands.misc-commands.alert")
	public static String		COMMANDS_MISC_ALERT;
	@ConfigOption(path = "commands.misc-commands.all")
	public static String		COMMANDS_MISC_ALL;
	@ConfigOption(path = "commands.misc-commands.block")
	public static String		COMMANDS_MISC_BLOCK;
	@ConfigOption(path = "commands.misc-commands.config")
	public static String		COMMANDS_MISC_CONFIG;
	@ConfigOption(path = "commands.misc-commands.player")
	public static String		COMMANDS_MISC_PLAYER;
	@ConfigOption(path = "commands.misc-commands.remove")
	public static String		COMMANDS_MISC_REMOVE;
	@ConfigOption(path = "commands.misc-commands.word-on")
	public static String		COMMANDS_MISC_ON;
	@ConfigOption(path = "commands.misc-commands.word-off")
	public static String		COMMANDS_MISC_OFF;
	
	@ConfigOption(path = "commands.order")
	public static List<String>	COMMANDS_ORDER;
	
	
	protected ConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		loadDefaultConfigOptions();
	}
	
	@Override
	public void loadConfiguration() {
		loadConfigOptions();
	}
}
