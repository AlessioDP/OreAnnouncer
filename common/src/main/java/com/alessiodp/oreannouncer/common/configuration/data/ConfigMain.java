package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationSectionAdapter;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;

import java.util.HashSet;
import java.util.Set;

public abstract class ConfigMain extends ConfigurationFile {
	// OreAnnouncer settings
	public static boolean		OREANNOUNCER_UPDATES_CHECK;
	public static boolean		OREANNOUNCER_UPDATES_WARN;
	public static boolean		OREANNOUNCER_LOGGING_DEBUG;
	public static boolean		OREANNOUNCER_LOGGING_SAVE_ENABLE;
	public static String		OREANNOUNCER_LOGGING_SAVE_FORMAT;
	public static String		OREANNOUNCER_LOGGING_SAVE_FILE;
	
	// Storage settings
	public static String		STORAGE_TYPE_DATABASE;
	public static int			STORAGE_SETTINGS_GENERAL_SQL_VARCHARSIZE;
	public static boolean		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_SAVEOLD;
	public static String		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_OLDSUFFIX;
	public static String		STORAGE_SETTINGS_GENERAL_SQL_TABLES_PLAYERS;
	public static String		STORAGE_SETTINGS_GENERAL_SQL_TABLES_BLOCKS;
	public static String		STORAGE_SETTINGS_GENERAL_SQL_TABLES_VERSIONS;
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	public static String		STORAGE_SETTINGS_MYSQL_ADDRESS;
	public static String		STORAGE_SETTINGS_MYSQL_PORT;
	public static String		STORAGE_SETTINGS_MYSQL_DATABASE;
	public static String		STORAGE_SETTINGS_MYSQL_USERNAME;
	public static String		STORAGE_SETTINGS_MYSQL_PASSWORD;
	public static int			STORAGE_SETTINGS_MYSQL_POOLSIZE;
	public static int			STORAGE_SETTINGS_MYSQL_CONNLIFETIME;
	public static boolean		STORAGE_SETTINGS_MYSQL_USESSL;
	public static String		STORAGE_SETTINGS_MYSQL_CHARSET;
	
	
	// Alerts settings
	public static boolean		ALERTS_ENABLE;
	public static boolean		ALERTS_CONSOLE;
	public static boolean		ALERTS_COORDINATES_ENABLE;
	public static String		ALERTS_COORDINATES_FORMAT;
	public static boolean		ALERTS_COORDINATES_HIDE_ENABLE;
	public static int			ALERTS_COORDINATES_HIDE_COUNT;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_USER;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_X;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_Y;
	public static boolean		ALERTS_COORDINATES_HIDE_HIDE_Z;
	public static String		ALERTS_COORDINATES_HIDE_FORMAT;
	public static boolean		ALERTS_COORDINATES_HIDE_OBFUSCATE;
	public static boolean		ALERTS_COORDINATES_HIDE_FIXEDLENGTH;
	
	
	// Blocks settings
	public static boolean			BLOCKS_BYPASS_PLAYERBLOCKS;
	public static boolean			BLOCKS_BYPASS_SILKTOUCH;
	private static boolean			BLOCKS_WARNONWRONGBLOCKS;
	public static boolean			BLOCKS_LIGHT_ENABLE;
	public static boolean			BLOCKS_LIGHT_ALERTIFLOWER;
	public static boolean			BLOCKS_LIGHT_COUNTIFLOWER;
	public static Set<OABlockImpl>	BLOCKS_LIST;
	
	
	// Stats settings
	public static boolean		STATS_ENABLE;
	public static boolean		STATS_TOP_ENABLE;
	public static int			STATS_TOP_NUMPLAYERS;
	public static int			STATS_TOP_PAGESIZE;
	
	
	// Commands settings
	public static boolean		COMMANDS_TABSUPPORT;
	public static String		COMMANDS_DESCRIPTION_OA;
	
	public static String		COMMANDS_CMD_HELP;
	public static String		COMMANDS_CMD_OA;
	public static String		COMMANDS_CMD_ALERTS;
	public static String		COMMANDS_CMD_RELOAD;
	public static String		COMMANDS_CMD_STATS;
	public static String		COMMANDS_CMD_TOP;
	public static String		COMMANDS_CMD_VERSION;
	
	public static String		COMMANDS_SUB_ON;
	public static String		COMMANDS_SUB_OFF;
	
	
	protected ConfigMain(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		// General settings
		OREANNOUNCER_UPDATES_CHECK = true;
		OREANNOUNCER_UPDATES_WARN = true;
		OREANNOUNCER_LOGGING_DEBUG = false;
		OREANNOUNCER_LOGGING_SAVE_ENABLE = false;
		OREANNOUNCER_LOGGING_SAVE_FORMAT = "%date% [%time%] %message%\n";
		OREANNOUNCER_LOGGING_SAVE_FILE = "log.txt";
		
		
		// Storage settings
		STORAGE_TYPE_DATABASE = "sqlite";
		STORAGE_SETTINGS_GENERAL_SQL_VARCHARSIZE = 255;
		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_SAVEOLD = true;
		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_OLDSUFFIX = "_backup";
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_PLAYERS = "oreannouncer_players";
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_BLOCKS = "oreannouncer_blocks";
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_VERSIONS = "oreannouncer_versions";
		STORAGE_SETTINGS_SQLITE_DBFILE = "database.db";
		STORAGE_SETTINGS_MYSQL_ADDRESS = "localhost";
		STORAGE_SETTINGS_MYSQL_PORT = "3306";
		STORAGE_SETTINGS_MYSQL_DATABASE = "database";
		STORAGE_SETTINGS_MYSQL_USERNAME = "username";
		STORAGE_SETTINGS_MYSQL_PASSWORD = "password";
		STORAGE_SETTINGS_MYSQL_POOLSIZE = 10;
		STORAGE_SETTINGS_MYSQL_CONNLIFETIME = 1800000;
		STORAGE_SETTINGS_MYSQL_USESSL = false;
		STORAGE_SETTINGS_MYSQL_CHARSET = "utf8";
		
		
		// Alerts settings
		ALERTS_ENABLE = true;
		ALERTS_CONSOLE = true;
		ALERTS_COORDINATES_ENABLE = false;
		ALERTS_COORDINATES_FORMAT = "&7[x: %x%&7, z: %z%&7]";
		ALERTS_COORDINATES_HIDE_ENABLE = false;
		ALERTS_COORDINATES_HIDE_COUNT = 1;
		ALERTS_COORDINATES_HIDE_HIDDENFOR_USER = true;
		ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN = false;
		ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE = false;
		ALERTS_COORDINATES_HIDE_HIDE_X = true;
		ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		ALERTS_COORDINATES_HIDE_FORMAT = "&k%coordinate%";
		ALERTS_COORDINATES_HIDE_OBFUSCATE = true;
		ALERTS_COORDINATES_HIDE_FIXEDLENGTH = false;
		
		
		// Blocks settings
		BLOCKS_BYPASS_PLAYERBLOCKS = true;
		BLOCKS_BYPASS_SILKTOUCH = true;
		BLOCKS_WARNONWRONGBLOCKS = true;
		BLOCKS_LIGHT_ENABLE = false;
		BLOCKS_LIGHT_ALERTIFLOWER = true;
		BLOCKS_LIGHT_COUNTIFLOWER = false;
		BLOCKS_LIST = new HashSet<>();
		OABlockImpl temp = new OABlockImpl("DIAMOND_ORE");
		temp.setAlertingUsers(true);
		temp.setAlertingAdmins(true);
		temp.setSingularName("diamond");
		temp.setPluralName("diamonds");
		temp.setLightLevel(15);
		temp.setCountingOnDestroy(true);
		BLOCKS_LIST.add(temp);
		temp = new OABlockImpl("EMERALD_ORE");
		temp.setAlertingUsers(false);
		temp.setAlertingAdmins(true);
		temp.setSingularName("emerald");
		temp.setPluralName("emeralds");
		temp.setLightLevel(6);
		temp.setCountingOnDestroy(true);
		BLOCKS_LIST.add(temp);
		
		
		// Stats settings
		STATS_ENABLE = false;
		STATS_TOP_ENABLE = true;
		STATS_TOP_NUMPLAYERS = 5;
		STATS_TOP_PAGESIZE = 5;
		
		
		// Commands settings
		COMMANDS_TABSUPPORT = true;
		COMMANDS_DESCRIPTION_OA = "OreAnnouncer help page";
		COMMANDS_CMD_HELP = "help";
		COMMANDS_CMD_OA = "oa";
		COMMANDS_CMD_ALERTS = "alerts";
		COMMANDS_CMD_RELOAD = "reload";
		COMMANDS_CMD_STATS = "stats";
		COMMANDS_CMD_TOP = "top";
		COMMANDS_CMD_VERSION = "version";
		
		COMMANDS_SUB_ON = "on";
		COMMANDS_SUB_OFF = "off";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		// OreAnnouncer settings
		OREANNOUNCER_UPDATES_CHECK = confAdapter.getBoolean("oreannouncer.updates.check", OREANNOUNCER_UPDATES_CHECK);
		OREANNOUNCER_UPDATES_WARN = confAdapter.getBoolean("oreannouncer.updates.warn", OREANNOUNCER_UPDATES_WARN);
		OREANNOUNCER_LOGGING_DEBUG = confAdapter.getBoolean("oreannouncer.logging.debug", OREANNOUNCER_LOGGING_DEBUG);
		OREANNOUNCER_LOGGING_SAVE_ENABLE = confAdapter.getBoolean("oreannouncer.logging.save-file.enable", OREANNOUNCER_LOGGING_SAVE_ENABLE);
		OREANNOUNCER_LOGGING_SAVE_FORMAT = confAdapter.getString("oreannouncer.logging.save-file.format", OREANNOUNCER_LOGGING_SAVE_FORMAT);
		OREANNOUNCER_LOGGING_SAVE_FILE = confAdapter.getString("oreannouncer.logging.save-file.file", OREANNOUNCER_LOGGING_SAVE_FILE);
		
		// Storage settings
		STORAGE_TYPE_DATABASE = confAdapter.getString("storage.database-storage-type", STORAGE_TYPE_DATABASE);
		STORAGE_SETTINGS_GENERAL_SQL_VARCHARSIZE = confAdapter.getInt("storage.storage-settings.general-sql-settings.varchar-size", STORAGE_SETTINGS_GENERAL_SQL_VARCHARSIZE);
		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_SAVEOLD = confAdapter.getBoolean("storage.storage-settings.general-sql-settings.upgrade.save-old-table", STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_SAVEOLD);
		STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_OLDSUFFIX = confAdapter.getString("storage.storage-settings.general-sql-settings.upgrade.old-table-suffix", STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_OLDSUFFIX);
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_PLAYERS = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.players", STORAGE_SETTINGS_GENERAL_SQL_TABLES_PLAYERS);
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_BLOCKS = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.blocks", STORAGE_SETTINGS_GENERAL_SQL_TABLES_BLOCKS);
		STORAGE_SETTINGS_GENERAL_SQL_TABLES_VERSIONS = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.versions", STORAGE_SETTINGS_GENERAL_SQL_TABLES_VERSIONS);
		STORAGE_SETTINGS_SQLITE_DBFILE = confAdapter.getString("storage.storage-settings.sqlite.database-file", STORAGE_SETTINGS_SQLITE_DBFILE);
		STORAGE_SETTINGS_MYSQL_ADDRESS = confAdapter.getString("storage.storage-settings.mysql.address", STORAGE_SETTINGS_MYSQL_ADDRESS);
		STORAGE_SETTINGS_MYSQL_PORT = confAdapter.getString("storage.storage-settings.mysql.port", STORAGE_SETTINGS_MYSQL_PORT);
		STORAGE_SETTINGS_MYSQL_DATABASE = confAdapter.getString("storage.storage-settings.mysql.database", STORAGE_SETTINGS_MYSQL_DATABASE);
		STORAGE_SETTINGS_MYSQL_USERNAME = confAdapter.getString("storage.storage-settings.mysql.username", STORAGE_SETTINGS_MYSQL_USERNAME);
		STORAGE_SETTINGS_MYSQL_PASSWORD = confAdapter.getString("storage.storage-settings.mysql.password", STORAGE_SETTINGS_MYSQL_PASSWORD);
		STORAGE_SETTINGS_MYSQL_POOLSIZE = confAdapter.getInt("storage.storage-settings.mysql.pool-size", STORAGE_SETTINGS_MYSQL_POOLSIZE);
		STORAGE_SETTINGS_MYSQL_CONNLIFETIME = confAdapter.getInt("storage.storage-settings.mysql.connection-lifetime", STORAGE_SETTINGS_MYSQL_CONNLIFETIME);
		STORAGE_SETTINGS_MYSQL_USESSL = confAdapter.getBoolean("storage.storage-settings.mysql.use-ssl", STORAGE_SETTINGS_MYSQL_USESSL);
		STORAGE_SETTINGS_MYSQL_CHARSET = confAdapter.getString("storage.storage-settings.mysql.charset", STORAGE_SETTINGS_MYSQL_CHARSET);
		
		
		// Alerts settings
		ALERTS_ENABLE = confAdapter.getBoolean("alerts.enable", ALERTS_ENABLE);
		ALERTS_CONSOLE = confAdapter.getBoolean("alerts.console", ALERTS_CONSOLE);
		ALERTS_COORDINATES_ENABLE = confAdapter.getBoolean("alerts.coordinates.enable", ALERTS_COORDINATES_ENABLE);
		ALERTS_COORDINATES_FORMAT  = confAdapter.getString("alerts.coordinates.format", ALERTS_COORDINATES_FORMAT);
		ALERTS_COORDINATES_HIDE_ENABLE = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.enable", ALERTS_COORDINATES_HIDE_ENABLE);
		ALERTS_COORDINATES_HIDE_COUNT = confAdapter.getInt("alerts.coordinates.hide-real-coordinates.count", ALERTS_COORDINATES_HIDE_COUNT);
		ALERTS_COORDINATES_HIDE_HIDDENFOR_USER = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hidden-for.user", ALERTS_COORDINATES_HIDE_HIDDENFOR_USER);
		ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hidden-for.admin", ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN);
		ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hidden-for.console", ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE);
		ALERTS_COORDINATES_HIDE_HIDE_X = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hide.x", ALERTS_COORDINATES_HIDE_HIDE_X);
		ALERTS_COORDINATES_HIDE_HIDE_Y = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hide.y", ALERTS_COORDINATES_HIDE_HIDE_Y);
		ALERTS_COORDINATES_HIDE_HIDE_Z = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.hide.z", ALERTS_COORDINATES_HIDE_HIDE_Z);
		ALERTS_COORDINATES_HIDE_FIXEDLENGTH = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.fixed-length", ALERTS_COORDINATES_HIDE_FIXEDLENGTH);
		ALERTS_COORDINATES_HIDE_FORMAT = confAdapter.getString("alerts.coordinates.hide-real-coordinates.format", ALERTS_COORDINATES_HIDE_FORMAT);
		ALERTS_COORDINATES_HIDE_OBFUSCATE = confAdapter.getBoolean("alerts.coordinates.hide-real-coordinates.obfuscate", ALERTS_COORDINATES_HIDE_OBFUSCATE);
		
		
		// Blocks settings
		BLOCKS_BYPASS_PLAYERBLOCKS = confAdapter.getBoolean("blocks.bypass-player-blocks", BLOCKS_BYPASS_PLAYERBLOCKS);
		BLOCKS_BYPASS_SILKTOUCH = confAdapter.getBoolean("blocks.bypass-silk-touch", BLOCKS_BYPASS_SILKTOUCH);
		BLOCKS_WARNONWRONGBLOCKS = confAdapter.getBoolean("blocks.warn-on-wrong-blocks", BLOCKS_WARNONWRONGBLOCKS);
		BLOCKS_LIGHT_ENABLE = confAdapter.getBoolean("blocks.light-level.enable", BLOCKS_LIGHT_ENABLE);
		BLOCKS_LIGHT_ALERTIFLOWER = confAdapter.getBoolean("blocks.light-level.alert-only-if-lower", BLOCKS_LIGHT_ALERTIFLOWER);
		BLOCKS_LIGHT_COUNTIFLOWER = confAdapter.getBoolean("blocks.light-level.count-only-if-lower", BLOCKS_LIGHT_COUNTIFLOWER);
		handleBlocks(confAdapter);
		
		
		// Stats settings
		STATS_ENABLE = confAdapter.getBoolean("stats.enable", STATS_ENABLE);
		STATS_TOP_ENABLE = confAdapter.getBoolean("stats.top.enable", STATS_TOP_ENABLE);
		STATS_TOP_NUMPLAYERS = confAdapter.getInt("stats.top.number-of-players", STATS_TOP_NUMPLAYERS);
		STATS_TOP_PAGESIZE = confAdapter.getInt("stats.top.page-size", STATS_TOP_PAGESIZE);
		
		
		// Commands settings
		COMMANDS_TABSUPPORT = confAdapter.getBoolean("commands.tab-support", COMMANDS_TABSUPPORT);
		COMMANDS_DESCRIPTION_OA = confAdapter.getString("commands.oa-description", COMMANDS_DESCRIPTION_OA);
		
		COMMANDS_CMD_HELP = confAdapter.getString("commands.main-commands.help", COMMANDS_CMD_HELP);
		COMMANDS_CMD_OA = confAdapter.getString("commands.main-commands.oa", COMMANDS_CMD_OA);
		COMMANDS_CMD_ALERTS = confAdapter.getString("commands.main-commands.alerts", COMMANDS_CMD_ALERTS);
		COMMANDS_CMD_RELOAD = confAdapter.getString("commands.main-commands.reload", COMMANDS_CMD_RELOAD);
		COMMANDS_CMD_STATS = confAdapter.getString("commands.main-commands.stats", COMMANDS_CMD_STATS);
		COMMANDS_CMD_TOP = confAdapter.getString("commands.main-commands.top", COMMANDS_CMD_TOP);
		COMMANDS_CMD_VERSION = confAdapter.getString("commands.main-commands.version", COMMANDS_CMD_VERSION);
		
		COMMANDS_SUB_ON = confAdapter.getString("commands.sub-commands.on", COMMANDS_SUB_ON);
		COMMANDS_SUB_OFF = confAdapter.getString("commands.sub-commands.off", COMMANDS_SUB_OFF);
	}
	
	private void handleBlocks(ConfigurationAdapter confAdapter) {
		Set<OABlockImpl> blocks = new HashSet<>();
		OABlockImpl block;
		
		ConfigurationSectionAdapter csBlocks = confAdapter.getConfigurationSection("blocks.list");
		if (csBlocks != null) {
			for (String key : csBlocks.getKeys()) {
				if (((OreAnnouncerPlugin) plugin).getBlockManager().existsMaterial(key)) {
					// Material exists
					block = new OABlockImpl(key);
					block.setAlertingUsers(csBlocks.getBoolean(key + ".alerts.user", false));
					block.setAlertingAdmins(csBlocks.getBoolean(key + ".alerts.admin", false));
					block.setSingularName(csBlocks.getString(key + ".name.singular", key));
					block.setPluralName(csBlocks.getString(key + ".name.plural", key));
					block.setLightLevel(csBlocks.getInt(key + ".light-level", 15));
					block.setCountingOnDestroy(csBlocks.getBoolean(key + ".count-on-destroy", false));
					blocks.add(block);
				} else {
					// Material doesn't exist
					plugin.getLoggerManager().printError(OAConstants.DEBUG_CFG_WRONGBLOCK
							.replace("{block}", key));
				}
			}
			ConfigMain.BLOCKS_LIST = blocks;
		}
	}
}
