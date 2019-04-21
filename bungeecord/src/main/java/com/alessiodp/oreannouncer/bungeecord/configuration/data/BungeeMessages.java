package com.alessiodp.oreannouncer.bungeecord.configuration.data;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.Getter;

public class BungeeMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bungee/messages.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUNGEE_MESSAGES;
	
	
	public BungeeMessages(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}