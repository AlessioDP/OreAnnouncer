package com.alessiodp.oreannouncer.bukkit.utils;

import com.alessiodp.oreannouncer.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.utils.MessageUtils;

public class BukkitMessageUtils extends MessageUtils {
	public BukkitMessageUtils(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public String convertPlayerPlaceholders(String message, OAPlayerImpl player) {
		String ret = super.convertPlayerPlaceholders(message, player);
		if (player != null)
			ret = PlaceholderAPIHandler.getPlaceholders(player.getPlayerUUID(), ret);
		return ret;
	}
}
