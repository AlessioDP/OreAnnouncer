package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class MessageUtils {
	private final OreAnnouncerPlugin plugin;
	private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([%][^%]+[%])");
	private final Pattern PLACEHOLDER_PATTERN_DESTROYED = Pattern.compile(OAConstants.PLACEHOLDER_PLAYER_DESTROYED_MATERIAL_REGEX, Pattern.CASE_INSENSITIVE);
	
	public String convertPlayerPlaceholders(String message, OAPlayerImpl player) {
		String ret = message;
		if (player != null) {
			String replacement;
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				switch (identifier.toLowerCase()) {
					case OAConstants.PLACEHOLDER_PLAYER_NAME:
						replacement = player.getName();
						ret = ret.replace(identifier, replacement);
						break;
					case OAConstants.PLACEHOLDER_PLAYER_DESTROYED:
						ret = ret.replace(identifier, Integer.toString(plugin.getPlayerManager().getTotalBlocks(player)));
						break;
					default: // Nothing to do
				}
				
				// Player destroyed material placeholder
				Matcher matcherMaterial = PLACEHOLDER_PATTERN_DESTROYED.matcher(identifier);
				if (matcherMaterial.find()) {
					String material = matcherMaterial.group(1);
					if (plugin.getBlockManager().getListBlocks().containsKey(material.toUpperCase())) {
						PlayerDataBlock pdb = player.getDataBlocks().get(material.toLowerCase());
						if (pdb != null) {
							ret = ret.replace(identifier, Integer.toString(pdb.getDestroyCount()));
						} else {
							ret = ret.replace(identifier, "0");
						}
					}
				}
			}
		}
		return ret;
	}
}
