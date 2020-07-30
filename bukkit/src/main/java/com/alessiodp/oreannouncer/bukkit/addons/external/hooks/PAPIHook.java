package com.alessiodp.oreannouncer.bukkit.addons.external.hooks;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PAPIHook extends PlaceholderExpansion {
	@NonNull private final OreAnnouncerPlugin plugin;
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public String getName() {
		return plugin.getPluginName();
	}
	
	@Override
	public String getIdentifier() {
		return "oreannouncer";
	}
	
	@Override
	public String getAuthor() {
		return "AlessioDP";
	}
	
	@Override
	public String getVersion() {
		return plugin.getVersion();
	}
	
	@Override
	public boolean persist(){
		return true;
	}
	
	@Override
	public List<String> getPlaceholders() {
		List<String> ret = new ArrayList<>();
		for (OAPlaceholder placeholder : OAPlaceholder.values()) {
			ret.add("%" + getIdentifier() + "_" + placeholder.getSyntax() + "%");
		}
		return ret;
	}
	
	public String parsePlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
		if (offlinePlayer != null) {
			OAPlayerImpl player = plugin.getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
			
			OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
			
			return placeholder != null ? placeholder.formatPlaceholder(player, identifier) : "";
		}
		return identifier;
	}
}
