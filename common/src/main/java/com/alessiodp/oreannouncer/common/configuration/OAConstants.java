package com.alessiodp.oreannouncer.common.configuration;

import com.alessiodp.core.common.logging.ConsoleColor;

public class OAConstants {
	
	// Plugin settings
	public static final String PLUGIN_NAME = "OreAnnouncer";
	public static final String PLUGIN_FALLBACK = "oreannouncer";
	public static final ConsoleColor PLUGIN_CONSOLECOLOR = ConsoleColor.GREEN;
	public static final String PLUGIN_SPIGOTCODE = "33464";
	
	// Versions
	public static final int VERSION_BUKKIT_CONFIG = 3;
	public static final int VERSION_BUNGEE_CONFIG = 1;
	public static final int VERSION_BUKKIT_MESSAGES = 4;
	public static final int VERSION_BUNGEE_MESSAGES = 3;
	public static final int VERSION_DATABASE_MYSQL = 2;
	public static final int VERSION_DATABASE_SQLITE = 2;
	
	
	// Blocks
	public static final String BLOCK_METADATA = "OreAnnouncer_counted";
	
	
	// Placeholders
	public static final String PLACEHOLDER_PLAYER_NAME = "%player%";
	public static final String PLACEHOLDER_PLAYER_DESTROYED = "%player_destroyed%";
	public static final String PLACEHOLDER_PLAYER_DESTROYED_MATERIAL = "%player_destroyed_{material}%";
	public static final String PLACEHOLDER_PLAYER_DESTROYED_MATERIAL_REGEX = "%player_destroyed_([^%]+)%";
	
	
	// SQL queries
	public static final String QUERY_PLAYER_INSERT_MYSQL = "INSERT INTO {table_players} (`uuid`, `alerts`) VALUES (?,?) ON DUPLICATE KEY UPDATE `alerts`=VALUES(`alerts`);";
	public static final String QUERY_PLAYER_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_players} (`uuid`, `alerts`) VALUES (?,?);";
	public static final String QUERY_PLAYER_DELETE = "DELETE FROM {table_players} WHERE `uuid`=?;";
	public static final String QUERY_PLAYER_GET = "SELECT * FROM {table_players} WHERE `uuid`=?;";
	public static final String QUERY_PLAYER_TOP_BLOCKS = "SELECT player, sum(destroyed) as total FROM {table_blocks} GROUP BY player ORDER BY total DESC LIMIT ? OFFSET ?;";
	public static final String QUERY_PLAYER_TOP_NUMBER = "SELECT COUNT(DISTINCT player) as total FROM {table_blocks};";
	
	public static final String QUERY_BLOCK_INSERT_MYSQL = "INSERT INTO {table_blocks} (`player`, `material_name`, `destroyed`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `destroyed`=VALUES(`destroyed`);";
	public static final String QUERY_BLOCK_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_blocks} (`player`, `material_name`, `destroyed`) VALUES (?,?,?);";
	public static final String QUERY_BLOCK_GET_PLAYER = "SELECT * FROM {table_blocks} WHERE `player`=?;";
	
	
	// Debug messages
	public static final String DEBUG_CFG_WRONGBLOCK = "Cannot find the block '{block}'";
	public static final String DEBUG_CMD_ALERTS = "{player} performed alerts command with toggle '{toggle}'";
	public static final String DEBUG_CMD_HELP = "{player} performed help command with page '{page}'";
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
	public static final String DEBUG_DB_GETPLAYER_BYNAME = "Get player request by name for '{player}'";
	public static final String DEBUG_DB_UPDATEDATABLOCK = "Update data block for '{uuid}' of {block}";
	public static final String DEBUG_DB_TOP_PLAYERBLOCKS = "Getting top players with limit {limit} and offset {offset}";
	public static final String DEBUG_DB_TOP_NUMBER = "Getting number of top players";
	public static final String DEBUG_EVENT_BLOCK_BREAK = "{player} broke a block of '{block}'";
	public static final String DEBUG_EVENT_BLOCK_PLACE = "{player} placed a marked block '{block}'";
	public static final String DEBUG_MESSAGING_SEND_ALERT = "Sent an alert packet";
	public static final String DEBUG_MESSAGING_SEND_ALERT_FAILED = "Failed to send an alert packet";
	public static final String DEBUG_MESSAGING_SEND_DESTROY = "Sent a destroy packet";
	public static final String DEBUG_MESSAGING_SEND_DESTROY_FAILED = "Failed to send a destroy packet";
	public static final String DEBUG_MESSAGING_RECEIVED = "Received an OreAnnouncer packet of type '{type}'";
	public static final String DEBUG_MESSAGING_RECEIVED_WRONG = "Received a wrong OreAnnouncer packet";
	public static final String DEBUG_MESSAGING_DESTROY_UUID_EMPTY = "Received an OreAnnouncer destroy packet with an empty UUID";
	public static final String DEBUG_PLAYER_UPDATENAME = "Changed name of '{uuid}' from '{old}' to '{new}'";
	public static final String DEBUG_PLAYER_LOADBLOCKS = "Loaded {number} blocks of '{uuid}'";
}
