package com.alessiodp.oreannouncer.bukkit.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.AddonManager;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import lombok.NonNull;

public class BukkitAddonManager extends AddonManager {
	private final EssentialsChatHandler essentialsChat;
	private final PlaceholderAPIHandler placeholderAPI;
	
	public BukkitAddonManager(@NonNull ADPPlugin plugin) {
		super(plugin);
		
		essentialsChat = new EssentialsChatHandler((OreAnnouncerPlugin) plugin);
		placeholderAPI = new PlaceholderAPIHandler((OreAnnouncerPlugin) plugin);
	}
	
	@Override
	public void loadAddons() {
		plugin.getLoggerManager().logDebug(Constants.DEBUG_ADDON_INIT, true);
		
		essentialsChat.init();
		placeholderAPI.init();
	}
}
