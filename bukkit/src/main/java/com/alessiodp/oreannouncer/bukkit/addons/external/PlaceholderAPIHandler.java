package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.bukkit.addons.external.hooks.PAPIHook;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.UUID;

@RequiredArgsConstructor
public class PlaceholderAPIHandler {
	@NonNull private final OreAnnouncerPlugin plugin;
	private static final String ADDON_NAME = "PlaceholderAPI";
	private static boolean firstTime = true;
	private static boolean active;
	private static PAPIHook hook;
	
	public void init() {
		if (active) {
			// Already active, print hooked then return
			plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			return;
		}
		
		if (firstTime) {
			firstTime = false; // Register PAPI only one time, this is called by server thread on startup (async not supported)
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				hook = new PAPIHook(plugin);
				
				if (hook.register()) {
					active = true;
					
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
				}
			}
		}
	}
	
	public static String getPlaceholders(UUID uuid, String message) {
		String ret = message;
		if (active && hook != null)
			ret = hook.parsePlaceholders(Bukkit.getOfflinePlayer(uuid), message);
		return ret;
	}
}
