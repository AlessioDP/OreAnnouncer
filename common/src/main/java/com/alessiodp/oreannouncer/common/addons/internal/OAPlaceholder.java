package com.alessiodp.oreannouncer.common.addons.internal;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum OAPlaceholder {
	PLAYER_DESTROY,
	PLAYER_DESTROY_BLOCK (true),
	PLAYER_FOUND,
	PLAYER_FOUND_BLOCK (true),
	PLAYER_FOUNDIN_RANGE (true),
	PLAYER_FOUNDIN_RANGE_BLOCK (true);
	
	private final OreAnnouncerPlugin plugin;
	private final boolean custom; // Ignore placeholder auto-matching by name
	
	
	private static final String PREFIX_DESTROY_BLOCK = "player_destroy_"; // %player_destroy_BLOCK%
	private static final String PREFIX_FOUND_BLOCK = "player_found_"; // %player_found_BLOCK%
	private static final String PREFIX_FOUNDIN_RANGE_BLOCK = "player_foundin_"; // %player_foundin_RANGE%, %player_foundin_RANGE_BLOCK%
	
	private static final Pattern PATTERN_DESTROY_BLOCK = Pattern.compile("player_destroy_([a-zA-Z_]+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_FOUND_BLOCK = Pattern.compile("player_found_([a-zA-Z_]+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_FOUNDIN_RANGE_BLOCK = Pattern.compile("player_foundin_([0-9]+)(_([a-zA-Z_]+))?", Pattern.CASE_INSENSITIVE);
	
	OAPlaceholder() {
		this(false);
	}
	
	OAPlaceholder(boolean custom) {
		this.custom = custom;
		plugin = ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance());
	}
	
	public static OAPlaceholder getPlaceholder(String identifier) {
		for (OAPlaceholder en : OAPlaceholder.values()) {
			if (!en.custom && en.name().equalsIgnoreCase(identifier)) {
				return en;
			}
		}
		
		if (identifier.toLowerCase(Locale.ENGLISH).startsWith(PREFIX_DESTROY_BLOCK))
			return PLAYER_DESTROY_BLOCK;
		
		if (identifier.toLowerCase(Locale.ENGLISH).startsWith(PREFIX_FOUND_BLOCK))
			return PLAYER_FOUND_BLOCK;
		
		if (identifier.toLowerCase(Locale.ENGLISH).startsWith(PREFIX_FOUNDIN_RANGE_BLOCK)) {
			Matcher matcher = PATTERN_FOUNDIN_RANGE_BLOCK.matcher(identifier);
			if (matcher.find()) {
				if (matcher.group(3) != null)
					return PLAYER_FOUNDIN_RANGE_BLOCK;
				else
					return PLAYER_FOUNDIN_RANGE;
			}
		}
		return null;
	}
	
	public String formatPlaceholder(OAPlayerImpl player, String identifier) {
		Matcher matcher;
		if (player != null) {
			OABlockImpl tempBlock = null;
			switch (this) {
				case PLAYER_DESTROY:
				case PLAYER_DESTROY_BLOCK:
					matcher = PATTERN_DESTROY_BLOCK.matcher(identifier);
					if (matcher.find()) {
						tempBlock = Blocks.LIST.get(matcher.group(1));
					}
					return Integer.toString(((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getPlayerManager().getTotalBlocksDestroy(player, tempBlock));
				case PLAYER_FOUND:
				case PLAYER_FOUND_BLOCK:
					matcher = PATTERN_FOUND_BLOCK.matcher(identifier);
					if (matcher.find()) {
						tempBlock = Blocks.LIST.get(matcher.group(1));
					}
					return Integer.toString(plugin.getPlayerManager().getTotalBlocksFound(player, tempBlock, 0));
				case PLAYER_FOUNDIN_RANGE:
				case PLAYER_FOUNDIN_RANGE_BLOCK:
					matcher = PATTERN_FOUNDIN_RANGE_BLOCK.matcher(identifier);
					if (matcher.find()) {
						try {
							long sinceTimestamp = Long.parseLong(matcher.group(1));
							if (matcher.groupCount() > 2) {
								tempBlock = Blocks.LIST.get(matcher.group(3));
							}
							return Integer.toString(plugin.getPlayerManager().getTotalBlocksFound(player, tempBlock, sinceTimestamp));
						} catch (NumberFormatException ignored) {}
					}
				default:
					return null;
			}
		}
		return null;
	}
}