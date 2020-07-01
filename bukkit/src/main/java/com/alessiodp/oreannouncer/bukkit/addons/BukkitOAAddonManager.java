package com.alessiodp.oreannouncer.bukkit.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bukkit.addons.external.DiscordSRVHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.OAAddonManager;
import lombok.NonNull;

public class BukkitOAAddonManager extends OAAddonManager {
	private final DiscordSRVHandler discordSRV;
	private final EssentialsChatHandler essentialsChat;
	private final PlaceholderAPIHandler placeholderAPI;
	
	public BukkitOAAddonManager(@NonNull ADPPlugin plugin) {
		super(plugin);
		
		discordSRV = new DiscordSRVHandler((OreAnnouncerPlugin) plugin);
		essentialsChat = new EssentialsChatHandler((OreAnnouncerPlugin) plugin);
		placeholderAPI = new PlaceholderAPIHandler((OreAnnouncerPlugin) plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		discordSRV.init();
		essentialsChat.init();
		placeholderAPI.init();
	}
}
