package com.alessiodp.oreannouncer.bukkit.configuration.data;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.Getter;

public class BukkitMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bukkit/messages.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUKKIT_MESSAGES;
	
	
	public BukkitMessages(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}