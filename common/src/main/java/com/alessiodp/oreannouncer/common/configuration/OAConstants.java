package com.alessiodp.oreannouncer.common.configuration;

import com.alessiodp.core.common.logging.ConsoleColor;

public class OAConstants {
	
	// Plugin settings
	public static final String PLUGIN_NAME = "OreAnnouncer";
	public static final String PLUGIN_FALLBACK = "oreannouncer";
	public static final ConsoleColor PLUGIN_CONSOLECOLOR = ConsoleColor.GREEN;
	public static final String PLUGIN_PACKAGENAME = "com.alessiodp.oreannouncer";
	public static final int PLUGIN_BSTATS_BUKKIT_ID = 1730;
	public static final int PLUGIN_BSTATS_BUNGEE_ID = 4513;
	
	// Versions
	public static final int VERSION_BUKKIT_CONFIG = 12;
	public static final int VERSION_BUNGEE_CONFIG = 7;
	public static final int VERSION_BUKKIT_MESSAGES = 10;
	public static final int VERSION_BUNGEE_MESSAGES = 8;
	public static final int VERSION_BLOCKS = 1;
	
	
	// Debug messages
	public static final String DEBUG_CFG_WRONG_BLOCK = "Cannot find the block '%s'";
	public static final String DEBUG_CFG_WRONG_VARIANT = "Cannot find the variant block '%s'";
	
	public static final String DEBUG_FAILED_PARSE_DATE = "Failed to parse format date";
	
	public static final String DEBUG_COUNTER_HANDLING = "Handling a block found result of %s with %d blocks";
	
	public static final String DEBUG_DB_UPDATEPLAYER = "Update player request for %s (uuid: %s)";
	public static final String DEBUG_DB_GETPLAYER = "Get player request for %s";
	public static final String DEBUG_DB_UPDATE_BLOCK_DESTROY = "Update block destroy request for %s of %s";
	public static final String DEBUG_DB_SET_BLOCK_DESTROY = "Set block destroy request for %s of %s";
	public static final String DEBUG_DB_GET_BLOCK_DESTROY = "Get block destroy request for %s of %s";
	public static final String DEBUG_DB_GET_ALL_BLOCK_DESTROY = "Get all block destroy request for %s";
	public static final String DEBUG_DB_INSERT_BLOCK_FOUND = "Insert block found request for %s of %s";
	public static final String DEBUG_DB_GET_BLOCK_FOUND = "Get block found request for %s of %s";
	public static final String DEBUG_DB_LOG_BLOCKS = "Get log blocks request for %s of %s";
	public static final String DEBUG_DB_LOG_BLOCKS_NUMBER = "Get number of log blocks request for %s of %s";
	public static final String DEBUG_DB_TOP_PLAYERS = "Get top players by %s (block: %s) with limit %d and offset %d";
	public static final String DEBUG_DB_TOP_PLAYERS_NUMBER = "Get number of top players by %s (block: %s)";
	public static final String DEBUG_DB_TOP_PLAYER_POSITION = "Get top player position by %s (block: %s)";
	public static final String DEBUG_DB_TOTAL_DESTROY = "Get total by destroy (block: %s)";
	public static final String DEBUG_DB_TOTAL_FOUND = "Get total by found (block: %s)";
	public static final String DEBUG_DB_STATS_PLAYER = "Get player stats of %s by %s";
	
	public static final String DEBUG_EVENT_BLOCK_BREAK = "%s broke a block of '%s'";
	public static final String DEBUG_EVENT_BLOCK_PLACE = "%s placed a marked block '%s'";
	public static final String DEBUG_EVENT_BLOCK_TNT = "%s destroyed %d blocks with TNT";
	public static final String DEBUG_EVENT_BLOCK_BREAK_INFINITE_COUNT = "Security Counter Warning:\n=========================================================\n  WARNING: A player is trying to destroy '%s' and nearest blocks are over 500.\n  This is a security warning, check if the block is correctly configured or contact the developer.\n=========================================================";
	
	public static final String DEBUG_MESSAGING_RECEIVED = "Received an OreAnnouncer packet of type '%s' from channel '%s'";
	public static final String DEBUG_MESSAGING_RECEIVED_WRONG = "Received a wrong OreAnnouncer packet from channel '%s'";
	public static final String DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER = "Received an OreAnnouncer packet, updated player %s";
	public static final String DEBUG_MESSAGING_LISTEN_ALERT_ERROR = "Received an OreAnnouncer packet, parsing of Alert failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_ALERT_COUNT_ERROR = "Received an OreAnnouncer packet, parsing of AlertCount failed: %s";
	public static final String DEBUG_MESSAGING_DESTROY_UUID_EMPTY = "Received an OreAnnouncer destroy packet with an empty UUID";
	public static final String DEBUG_MESSAGING_FOUND_UUID_EMPTY = "Received an OreAnnouncer found packet with an empty UUID";
	
	public static final String DEBUG_PLAYER_RELOADED = "Reloaded player %s";
	public static final String DEBUG_PLAYER_GET_DATABASE = "Got player %s from database (p-uuid: %s)";
	public static final String DEBUG_PLAYER_GET_LIST = "Got player %s from list (p-uuid: %s)";
	public static final String DEBUG_PLAYER_GET_NEW = "Got a new player %s (p-uuid: %s)";
}
