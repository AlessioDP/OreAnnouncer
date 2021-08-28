package com.alessiodp.oreannouncer.common.addons.internal;

import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum OAPlaceholder {
	TOP_PLAYER_BY_DESTROY_NUMBER (true, "top_player_by_destroy_<number>"),
	TOP_PLAYER_BY_DESTROY_NUMBER_PLACEHOLDER (true, "top_player_by_destroy_<number>_<placeholder>"),
	TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER (true, "top_player_by_destroy_<block>_<number>"),
	TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER_PLACEHOLDER (true, "top_player_by_destroy_<block>_<number>_<placeholder>"),
	TOP_PLAYER_BY_FOUND_NUMBER (true, "top_player_by_found_<number>"),
	TOP_PLAYER_BY_FOUND_NUMBER_PLACEHOLDER (true, "top_player_by_found_<number>_<placeholder>"),
	TOP_PLAYER_BY_FOUND_BLOCK_NUMBER (true, "top_player_by_found_<block>_<number>"),
	TOP_PLAYER_BY_FOUND_BLOCK_NUMBER_PLACEHOLDER (true, "top_player_by_found_<block>_<number>_<placeholder>"),
	TOP_PLAYERS_TOTAL_BY_DESTROY,
	TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY (true, "top_players_total_<block>_by_destroy"),
	TOP_PLAYERS_TOTAL_BY_FOUND,
	TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND (true, "top_players_total_<block>_by_found"),
	TOTAL_BY_DESTROY,
	TOTAL_BLOCK_BY_DESTROY (true, "total_<block>_by_destroy"),
	TOTAL_BY_FOUND,
	TOTAL_BLOCK_BY_FOUND (true, "total_<block>_by_found"),
	PLAYER_DESTROY,
	PLAYER_DESTROY_BLOCK (true, "player_destroy_<block>"),
	PLAYER_FOUND,
	PLAYER_FOUND_BLOCK (true, "player_found_<block>"),
	PLAYER_FOUNDIN_RANGE (true, "player_foundin_<range>"),
	PLAYER_FOUNDIN_RANGE_BLOCK (true, "player_foundin_<range>_<block>"),
	PLAYER_ID,
	PLAYER_NAME,
	PLAYER_TOP_BY_DESTROY,
	PLAYER_TOP_BY_DESTROY_BLOCK (true, "player_top_by_destroy_<block>"),
	PLAYER_TOP_BY_FOUND,
	PLAYER_TOP_BY_FOUND_BLOCK (true, "player_top_by_found_<block>"),
	SERVER_NAME,
	SERVER_ID;
	
	private final OreAnnouncerPlugin plugin;
	private final boolean custom; // Ignore placeholder auto-matching by name
	@Getter private final String syntax;
	
	
	private static final String PREFIX_TOP_PLAYER_BY_DESTROY = "top_player_by_destroy";
	private static final String PREFIX_TOP_PLAYER_BY_FOUND = "top_player_by_found";
	private static final String PREFIX_TOP_PLAYERS_TOTAL_BLOCK = "top_players_total_";
	private static final String PREFIX_TOTAL_BLOCK = "total_";
	private static final String PREFIX_DESTROY_BLOCK = "player_destroy_"; // %player_destroy_BLOCK%
	private static final String PREFIX_FOUND_BLOCK = "player_found_"; // %player_found_BLOCK%
	private static final String PREFIX_FOUNDIN_RANGE_BLOCK = "player_foundin_"; // %player_foundin_RANGE%, %player_foundin_RANGE_BLOCK%
	
	private static final String SUFFIX_BY_DESTROY = "_by_destroy";
	private static final String SUFFIX_BY_FOUND = "_by_found";
	
	private static final String SUFFIX_PLAYER_TOP_BY_DESTROY = "player_top_by_destroy_";
	private static final String SUFFIX_PLAYER_TOP_BY_FOUND = "player_top_by_found_";
	
	private static final Pattern PATTERN_TOP_PLAYER_BY_DESTROY = Pattern.compile("top_player_by_destroy_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOP_PLAYER_BY_DESTROY_BLOCK = Pattern.compile("top_player_by_destroy_([a-z_]+)_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOP_PLAYER_BY_FOUND = Pattern.compile("top_player_by_found_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOP_PLAYER_BY_FOUND_BLOCK = Pattern.compile("top_player_by_found_([a-z_]+)_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY = Pattern.compile("top_players_total_([a-z_]+)_by_destroy", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND = Pattern.compile("top_players_total_([a-z_]+)_by_found", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOTAL_BLOCK_BY_DESTROY = Pattern.compile("total_([a-z_]+)_by_destroy", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_TOTAL_BLOCK_BY_FOUND = Pattern.compile("total_([a-z_]+)_by_found", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_DESTROY_BLOCK = Pattern.compile("player_destroy_([a-zA-Z_]+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_FOUND_BLOCK = Pattern.compile("player_found_([a-zA-Z_]+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_FOUNDIN_RANGE_BLOCK = Pattern.compile("player_foundin_([0-9]+)(_([a-zA-Z_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_PLAYER_TOP = Pattern.compile("player_top_by_(destroy|found)_([a-z_]+)", Pattern.CASE_INSENSITIVE);
	
	OAPlaceholder() {
		this(false, null);
	}
	
	OAPlaceholder(boolean custom, String syntax){
		this.custom = custom;
		this.syntax = syntax != null ? syntax : CommonUtils.toLowerCase(this.name());
		plugin = ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance());
	}
	
	public static OAPlaceholder getPlaceholder(String identifier) {
		String identifierLower = CommonUtils.toLowerCase(identifier);
		for (OAPlaceholder en : OAPlaceholder.values()) {
			if (!en.custom && en.syntax.equals(identifierLower)) {
				return en;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_TOP_PLAYER_BY_DESTROY)) {
			Matcher matcher = PATTERN_TOP_PLAYER_BY_DESTROY.matcher(identifier);
			if (matcher.find())
				return matcher.group(3) != null ? TOP_PLAYER_BY_DESTROY_NUMBER_PLACEHOLDER : TOP_PLAYER_BY_DESTROY_NUMBER;
			
			matcher = PATTERN_TOP_PLAYER_BY_DESTROY_BLOCK.matcher(identifier);
			if (matcher.find())
				return matcher.group(4) != null ? TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER_PLACEHOLDER : TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER;
		}
		
		if (identifierLower.startsWith(PREFIX_TOP_PLAYER_BY_FOUND)) {
			Matcher matcher = PATTERN_TOP_PLAYER_BY_FOUND.matcher(identifier);
			if (matcher.find())
				return matcher.group(3) != null ? TOP_PLAYER_BY_FOUND_NUMBER_PLACEHOLDER : TOP_PLAYER_BY_FOUND_NUMBER;
			
			matcher = PATTERN_TOP_PLAYER_BY_FOUND_BLOCK.matcher(identifier);
			if (matcher.find())
				return matcher.group(4) != null ? TOP_PLAYER_BY_FOUND_BLOCK_NUMBER_PLACEHOLDER : TOP_PLAYER_BY_FOUND_BLOCK_NUMBER;
		}
		
		if (identifierLower.startsWith(PREFIX_TOP_PLAYERS_TOTAL_BLOCK)) {
			if (identifierLower.endsWith(SUFFIX_BY_DESTROY)) {
				Matcher matcher = PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY.matcher(identifier);
				if (matcher.find())
					return TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY;
			}
			if (identifierLower.endsWith(SUFFIX_BY_FOUND)) {
				Matcher matcher = PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND.matcher(identifier);
				if (matcher.find())
					return TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_TOTAL_BLOCK)) {
			if (identifierLower.endsWith(SUFFIX_BY_DESTROY)) {
				Matcher matcher = PATTERN_TOTAL_BLOCK_BY_DESTROY.matcher(identifier);
				if (matcher.find())
					return TOTAL_BLOCK_BY_DESTROY;
			}
			if (identifierLower.endsWith(SUFFIX_BY_FOUND)) {
				Matcher matcher = PATTERN_TOTAL_BLOCK_BY_FOUND.matcher(identifier);
				if (matcher.find())
					return TOTAL_BLOCK_BY_FOUND;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_DESTROY_BLOCK))
			return PLAYER_DESTROY_BLOCK;
		
		if (identifierLower.startsWith(PREFIX_FOUND_BLOCK))
			return PLAYER_FOUND_BLOCK;
		
		if (identifierLower.startsWith(PREFIX_FOUNDIN_RANGE_BLOCK)) {
			Matcher matcher = PATTERN_FOUNDIN_RANGE_BLOCK.matcher(identifier);
			if (matcher.find()) {
				if (matcher.group(3) != null)
					return PLAYER_FOUNDIN_RANGE_BLOCK;
				else
					return PLAYER_FOUNDIN_RANGE;
			}
		}
		
		if (identifierLower.startsWith(SUFFIX_PLAYER_TOP_BY_DESTROY)) {
			Matcher matcher = PATTERN_PLAYER_TOP.matcher(identifier);
			if (matcher.find() && matcher.group(2) != null)
				return PLAYER_TOP_BY_DESTROY_BLOCK;
		}
		
		if (identifierLower.startsWith(SUFFIX_PLAYER_TOP_BY_FOUND)) {
			Matcher matcher = PATTERN_PLAYER_TOP.matcher(identifier);
			if (matcher.find() && matcher.group(2) != null)
				return PLAYER_TOP_BY_FOUND_BLOCK;
		}
		return null;
	}
	
	public String formatPlaceholder(OAPlayerImpl player, String identifier) {
		return formatPlaceholder(player, identifier, "");
	}
	
	public String formatPlaceholder(OAPlayerImpl player, String identifier, String emptyPlaceholder) {
		Matcher matcher;
		if (player != null) {
			OABlockImpl tempBlock = null;
			switch (this) {
				case TOP_PLAYER_BY_DESTROY_NUMBER:
				case TOP_PLAYER_BY_DESTROY_NUMBER_PLACEHOLDER:
					return getTopPlayerBy(identifier, emptyPlaceholder, PATTERN_TOP_PLAYER_BY_DESTROY, OADatabaseManager.ValueType.DESTROY);
				case TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER:
				case TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER_PLACEHOLDER:
					return getTopPlayerPerBlockBy(identifier, emptyPlaceholder, PATTERN_TOP_PLAYER_BY_DESTROY_BLOCK, OADatabaseManager.ValueType.DESTROY);
				case TOP_PLAYER_BY_FOUND_NUMBER:
				case TOP_PLAYER_BY_FOUND_NUMBER_PLACEHOLDER:
					return getTopPlayerBy(identifier, emptyPlaceholder, PATTERN_TOP_PLAYER_BY_FOUND, OADatabaseManager.ValueType.FOUND);
				case TOP_PLAYER_BY_FOUND_BLOCK_NUMBER:
				case TOP_PLAYER_BY_FOUND_BLOCK_NUMBER_PLACEHOLDER:
					return getTopPlayerPerBlockBy(identifier, emptyPlaceholder, PATTERN_TOP_PLAYER_BY_FOUND_BLOCK, OADatabaseManager.ValueType.FOUND);
				case TOP_PLAYERS_TOTAL_BY_DESTROY:
				case TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY:
					matcher = PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY.matcher(identifier);
					if (matcher.find())
						tempBlock = Blocks.searchBlock(matcher.group(1));
					return Integer.toString(plugin.getDatabaseManager().getTopPlayersNumber(OADatabaseManager.ValueType.DESTROY, tempBlock));
				case TOP_PLAYERS_TOTAL_BY_FOUND:
				case TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND:
					matcher = PATTERN_TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND.matcher(identifier);
					if (matcher.find())
						tempBlock = Blocks.searchBlock(matcher.group(1));
					return Integer.toString(plugin.getDatabaseManager().getTopPlayersNumber(OADatabaseManager.ValueType.FOUND, tempBlock));
				case TOTAL_BY_DESTROY:
				case TOTAL_BLOCK_BY_DESTROY:
					matcher = PATTERN_TOTAL_BLOCK_BY_DESTROY.matcher(identifier);
					if (matcher.find())
						tempBlock = Blocks.searchBlock(matcher.group(1));
					return Integer.toString(plugin.getDatabaseManager().getTotalDestroy(tempBlock));
				case TOTAL_BY_FOUND:
				case TOTAL_BLOCK_BY_FOUND:
					matcher = PATTERN_TOTAL_BLOCK_BY_FOUND.matcher(identifier);
					if (matcher.find())
						tempBlock = Blocks.searchBlock(matcher.group(1));
					return Integer.toString(plugin.getDatabaseManager().getTotalFound(tempBlock));
				case PLAYER_DESTROY:
				case PLAYER_DESTROY_BLOCK:
					matcher = PATTERN_DESTROY_BLOCK.matcher(identifier);
					if (matcher.find()) {
						tempBlock = Blocks.searchBlock(matcher.group(1));
					}
					return Integer.toString(((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getPlayerManager().getTotalBlocksDestroy(player, tempBlock));
				case PLAYER_FOUND:
				case PLAYER_FOUND_BLOCK:
					matcher = PATTERN_FOUND_BLOCK.matcher(identifier);
					if (matcher.find()) {
						tempBlock = Blocks.searchBlock(matcher.group(1));
					}
					return Integer.toString(plugin.getPlayerManager().getTotalBlocksFound(player, tempBlock, 0));
				case PLAYER_FOUNDIN_RANGE:
				case PLAYER_FOUNDIN_RANGE_BLOCK:
					matcher = PATTERN_FOUNDIN_RANGE_BLOCK.matcher(identifier);
					if (matcher.find()) {
						try {
							long sinceTimestamp = Long.parseLong(matcher.group(1));
							if (matcher.groupCount() > 2 && matcher.group(3) != null) {
								tempBlock = Blocks.searchBlock(matcher.group(3));
							}
							return Integer.toString(plugin.getPlayerManager().getTotalBlocksFound(player, tempBlock, sinceTimestamp));
						} catch (Exception ignored) {}
					}
					return "0";
				case PLAYER_ID:
					return player.getPlayerUUID().toString();
				case PLAYER_NAME:
					return player.getName();
				case PLAYER_TOP_BY_DESTROY:
				case PLAYER_TOP_BY_DESTROY_BLOCK:
					matcher = PATTERN_PLAYER_TOP.matcher(identifier);
					if (matcher.find() && matcher.group(2) != null) {
						tempBlock = Blocks.searchBlock(matcher.group(2));
					}
					return Integer.toString(plugin.getDatabaseManager().getTopPlayerPosition(player.getPlayerUUID(), OADatabaseManager.ValueType.DESTROY, tempBlock));
				case PLAYER_TOP_BY_FOUND:
				case PLAYER_TOP_BY_FOUND_BLOCK:
					matcher = PATTERN_PLAYER_TOP.matcher(identifier);
					if (matcher.find() && matcher.group(2) != null) {
						tempBlock = Blocks.searchBlock(matcher.group(2));
					}
					return Integer.toString(plugin.getDatabaseManager().getTopPlayerPosition(player.getPlayerUUID(), OADatabaseManager.ValueType.FOUND, tempBlock));
				case SERVER_NAME:
					return plugin.getServerName();
				case SERVER_ID:
					return plugin.getServerId();
				default:
					return null;
			}
		}
		return null;
	}
	
	private String getTopPlayerBy(String identifier, String emptyPlaceholder, Pattern pattern, OADatabaseManager.ValueType order) {
		Matcher matcher = pattern.matcher(identifier);
		if (matcher.find()) {
			try {
				int number = Integer.parseInt(matcher.group(1));
				Optional<Map.Entry<UUID, Integer>> resultOptional = plugin.getDatabaseManager().getTopPlayers(order, null, 1, number - 1).entrySet().stream().findAny();
				
				if (resultOptional.isPresent()) {
					OAPlayerImpl player = plugin.getPlayerManager().getPlayer(resultOptional.get().getKey());
					
					if (matcher.group(3) != null) {
						OAPlaceholder newPlaceholder = OAPlaceholder.getPlaceholder(matcher.group(3));
						return newPlaceholder != null ? newPlaceholder.formatPlaceholder(player, matcher.group(3)) : null;
					}
					
					return player.getName() != null ? player.getName() : emptyPlaceholder;
				}
				return emptyPlaceholder;
			} catch (Exception ignored) {}
		}
		return null;
	}
	
	private String getTopPlayerPerBlockBy(String identifier, String emptyPlaceholder, Pattern pattern, OADatabaseManager.ValueType order) {
		Matcher matcher = pattern.matcher(identifier);
		if (matcher.find()) {
			try {
				OABlockImpl block = Blocks.searchBlock(matcher.group(1));
				if (block != null) {
					int number = Integer.parseInt(matcher.group(2));
					Optional<Map.Entry<UUID, Integer>> resultOptional = plugin.getDatabaseManager().getTopPlayers(order, block, 1, number - 1).entrySet().stream().findAny();
					
					if (resultOptional.isPresent()) {
						OAPlayerImpl player = plugin.getPlayerManager().getPlayer(resultOptional.get().getKey());
						
						if (matcher.group(4) != null) {
							OAPlaceholder newPlaceholder = OAPlaceholder.getPlaceholder(matcher.group(4));
							return newPlaceholder != null ? newPlaceholder.formatPlaceholder(player, matcher.group(4)) : null;
						}
						
						return player.getName() != null ? player.getName() : emptyPlaceholder;
					}
				}
				return emptyPlaceholder;
			} catch (Exception ignored) {}
		}
		return null;
	}
}