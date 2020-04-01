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
	
	// Storage settings
	@ConfigOption(path = "storage.database-storage-type")
	public static String		STORAGE_TYPE_DATABASE;
	@ConfigOption(path = "storage.storage-settings.general-sql-settings.prefix")
	public static String		STORAGE_SETTINGS_GENERAL_SQL_PREFIX;
	@ConfigOption(path = "storage.storage-settings.general-sql-settings.charset")
	public static String		STORAGE_SETTINGS_GENERAL_SQL_CHARSET;
	@ConfigOption(path = "storage.storage-settings.sqlite.database-file")
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	@ConfigOption(path = "storage.storage-settings.mysql.address")
	public static String		STORAGE_SETTINGS_MYSQL_ADDRESS;
	@ConfigOption(path = "storage.storage-settings.mysql.port")
	public static String		STORAGE_SETTINGS_MYSQL_PORT;
	@ConfigOption(path = "storage.storage-settings.mysql.database")
	public static String		STORAGE_SETTINGS_MYSQL_DATABASE;
	@ConfigOption(path = "storage.storage-settings.mysql.username")
	public static String		STORAGE_SETTINGS_MYSQL_USERNAME;
	@ConfigOption(path = "storage.storage-settings.mysql.password")
	public static String		STORAGE_SETTINGS_MYSQL_PASSWORD;
	@ConfigOption(path = "storage.storage-settings.mysql.pool-size")
	public static int			STORAGE_SETTINGS_MYSQL_POOLSIZE;
	@ConfigOption(path = "storage.storage-settings.mysql.connection-lifetime")
	public static int			STORAGE_SETTINGS_MYSQL_CONNLIFETIME;
	@ConfigOption(path = "storage.storage-settings.mysql.use-ssl")
	public static boolean		STORAGE_SETTINGS_MYSQL_USESSL;
	
	
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
	@ConfigOption(path = "stats.blacklist-blocks.log", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_LOG;
	@ConfigOption(path = "stats.blacklist-blocks.stats", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_STATS;
	@ConfigOption(path = "stats.blacklist-blocks.top", nullable = true)
	public static List<String>	STATS_BLACKLIST_BLOCKS_TOP;
	@ConfigOption(path = "stats.advanced-count.enable")
	public static boolean		STATS_ADVANCED_COUNT_ENABLE;
	@ConfigOption(path = "stats.advanced-count.default-time-format")
	public static String		STATS_ADVANCED_COUNT_TIME_FORMAT;
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
	@ConfigOption(path = "stats.top.number-of-players")
	public static int			STATS_TOP_NUMPLAYERS;
	
	
	// Commands settings
	@ConfigOption(path = "commands.tab-support")
	public static boolean		COMMANDS_TABSUPPORT;
	@ConfigOption(path = "commands.oa-description")
	public static String		COMMANDS_DESCRIPTION_OA;
	
	@ConfigOption(path = "commands.main-commands.help")
	public static String		COMMANDS_CMD_HELP;
	@ConfigOption(path = "commands.main-commands.oa")
	public static String		COMMANDS_CMD_OA;
	@ConfigOption(path = "commands.main-commands.alerts")
	public static String		COMMANDS_CMD_ALERTS;
	@ConfigOption(path = "commands.main-commands.log")
	public static String		COMMANDS_CMD_LOG;
	@ConfigOption(path = "commands.main-commands.reload")
	public static String		COMMANDS_CMD_RELOAD;
	@ConfigOption(path = "commands.main-commands.stats")
	public static String		COMMANDS_CMD_STATS;
	@ConfigOption(path = "commands.main-commands.top")
	public static String		COMMANDS_CMD_TOP;
	@ConfigOption(path = "commands.main-commands.version")
	public static String		COMMANDS_CMD_VERSION;
	
	@ConfigOption(path = "commands.sub-commands.block")
	public static String		COMMANDS_SUB_BLOCK;
	@ConfigOption(path = "commands.sub-commands.player")
	public static String		COMMANDS_SUB_PLAYER;
	@ConfigOption(path = "commands.sub-commands.word-on")
	public static String		COMMANDS_SUB_ON;
	@ConfigOption(path = "commands.sub-commands.word-off")
	public static String		COMMANDS_SUB_OFF;
	
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
