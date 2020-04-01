package com.alessiodp.oreannouncer.common.configuration;

import com.alessiodp.core.common.logging.ConsoleColor;

public class OAConstants {
	
	// Plugin settings
	public static final String PLUGIN_NAME = "OreAnnouncer";
	public static final String PLUGIN_FALLBACK = "oreannouncer";
	public static final ConsoleColor PLUGIN_CONSOLECOLOR = ConsoleColor.GREEN;
	public static final String PLUGIN_PACKAGENAME = "com.alessiodp.oreannouncer";
	public static final String PLUGIN_SPIGOTCODE = "33464";
	public static final int PLUGIN_BSTATS_BUKKIT_ID = 1730;
	public static final int PLUGIN_BSTATS_BUNGEE_ID = 4513;
	
	// Versions
	public static final int VERSION_BUKKIT_CONFIG = 6;
	public static final int VERSION_BUNGEE_CONFIG = 3;
	public static final int VERSION_BUKKIT_MESSAGES = 6;
	public static final int VERSION_BUNGEE_MESSAGES = 5;
	public static final int VERSION_BLOCKS = 1;
	
	
	// Debug messages
	public static final String DEBUG_CFG_WRONGBLOCK = "Cannot find the block '{block}'";
	public static final String DEBUG_CMD_ALERTS = "{player} performed alerts command with toggle '{toggle}'";
	public static final String DEBUG_CMD_HELP = "{player} performed help command with page '{page}'";
	public static final String DEBUG_CMD_LOG = "{player} performed log command";
	public static final String DEBUG_CMD_LOG_CONSOLE = "Console performed log command";
	public static final String DEBUG_CMD_LOG_FAILED_PARSE_DATE = "Failed to parse format date: {message}";
	public static final String DEBUG_CMD_RELOAD = "{player} performed reload command";
	public static final String DEBUG_CMD_RELOAD_CONSOLE = "Console performed reload command";
	public static final String DEBUG_CMD_RELOADED = "Configuration reloaded by {player}";
	public static final String DEBUG_CMD_RELOADED_CONSOLE = "Configuration reloaded";
	public static final String DEBUG_CMD_STATS = "{player} performed stats command on '{victim}'";
	public static final String DEBUG_CMD_STATS_CONSOLE = "Performed stats command on '{victim}'";
	public static final String DEBUG_CMD_TOP = "{player} performed top command with page '{page}'";
	public static final String DEBUG_CMD_TOP_CONSOLE = "Performed top command with page '{page}'";
	public static final String DEBUG_CMD_VERSION = "{player} performed version command";
	public static final String DEBUG_CMD_VERSION_CONSOLE = "Performed version command";
	public static final String DEBUG_DB_UPDATEPLAYER = "Update player for {player} [{uuid}]";
	public static final String DEBUG_DB_GETPLAYER = "Get player request for '{uuid}'";
	public static final String DEBUG_DB_UPDATEDATABLOCK = "Update data block for '{uuid}' of {block}";
	public static final String DEBUG_DB_TOP_BLOCKS_LIST = "Getting top players with limit {limit} and offset {offset}";
	public static final String DEBUG_DB_TOP_BLOCKS_NUMBER = "Getting number of top players";
	public static final String DEBUG_DB_INSERT_BLOCKS_FOUND = "Inserting blocks found for '{uuid}' of {block}";
	public static final String DEBUG_DB_LATEST_BLOCK_FOUND = "Getting latest blocks found for '{uuid}' of {block}";
	public static final String DEBUG_DB_LOG_BLOCKS = "Getting log blocks for '{uuid}' of '{block}'";
	public static final String DEBUG_DB_LOG_BLOCKS_NUMBER = "Getting number of log blocks for '{uuid}' of '{block}'";
	public static final String DEBUG_EVENT_BLOCK_BREAK = "{player} broke a block of '{block}'";
	public static final String DEBUG_EVENT_BLOCK_PLACE = "{player} placed a marked block '{block}'";
	public static final String DEBUG_EVENT_BLOCK_TNT = "{player} destroyed {number} blocks with TNT";
	public static final String DEBUG_EVENT_BLOCK_BREAK_INFINITE_COUNT = "Security Counter Warning:\n=========================================================\n  WARNING: A player is trying to destroy '{block}' and nearest blocks are over 500.\n  This is a security warning, check if the block is correctly configured or contact the developer.\n=========================================================";
	public static final String DEBUG_MESSAGING_SEND_ALERT = "Sent an alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_FAILED = "Failed to send an alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_TNT = "Sent a tnt alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_TNT_FAILED = "Failed to send a tnt alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_COUNT = "Sent a count alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_COUNT_FAILED = "Failed to send a count alert packet";
	public static final String DEBUG_MESSAGING_SEND_DESTROY = "Sent a destroy packet";
	public static final String DEBUG_MESSAGING_SEND_DESTROY_FAILED = "Failed to send a destroy packet";
	public static final String DEBUG_MESSAGING_SEND_FOUND = "Sent a found packet";
	public static final String DEBUG_MESSAGING_SEND_FOUND_FAILED = "Failed to send a found packet";
	public static final String DEBUG_MESSAGING_RECEIVED = "Received an OreAnnouncer packet of type '{type}'";
	public static final String DEBUG_MESSAGING_RECEIVED_WRONG = "Received a wrong OreAnnouncer packet";
	public static final String DEBUG_MESSAGING_DESTROY_UUID_EMPTY = "Received an OreAnnouncer destroy packet with an empty UUID";
	public static final String DEBUG_MESSAGING_FOUND_UUID_EMPTY = "Received an OreAnnouncer found packet with an empty UUID";
	public static final String DEBUG_PLAYER_LOADBLOCKS = "Loaded {number} blocks of '{uuid}'";
}
