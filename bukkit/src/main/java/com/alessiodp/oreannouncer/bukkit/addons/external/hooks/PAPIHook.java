package com.alessiodp.oreannouncer.bukkit.addons.external.hooks;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;

@RequiredArgsConstructor
public class PAPIHook extends PlaceholderHook {
	@NonNull private final OreAnnouncerPlugin plugin;
	
	public boolean register() {
		boolean ret = false;
		try {
			Class.forName("me.clip.placeholderapi.PlaceholderHook").getMethod("onRequest", OfflinePlayer.class, String.class);
			
			if (PlaceholderAPI.isRegistered(plugin.getPluginFallbackName())) {
				PlaceholderAPI.unregisterPlaceholderHook(plugin.getPluginFallbackName());
			}
			ret = PlaceholderAPI.registerPlaceholderHook(plugin.getPluginFallbackName(), this);
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(Constants.DEBUG_ADDON_OUTDATED
					.replace("{addon}", "PlaceholderAPI"));
		}
		return ret;
	}
	
	public String setPlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
		OAPlayerImpl player = plugin.getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
		
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		
		return placeholder != null ? placeholder.formatPlaceholder(player, identifier) : "";
	}
}
