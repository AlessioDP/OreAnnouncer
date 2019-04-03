package com.alessiodp.oreannouncer.common.addons.internal;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

public enum OAPlaceholder {
	PLAYER_DESTROYED,
	PLAYER_DESTROYEDMATERIAL;
	
	private static OreAnnouncerPlugin plugin;
	private String format;
	private static final String DESTROYED_MATERIAL_PREFIX = "player_destroyed_";
	
	OAPlaceholder() {
		format = "";
	}
	
	public static void setupFormats(OreAnnouncerPlugin plugin) {
		OAPlaceholder.plugin = plugin;
		
		PLAYER_DESTROYED.format = OAConstants.PLACEHOLDER_PLAYER_DESTROYED;
		PLAYER_DESTROYEDMATERIAL.format = OAConstants.PLACEHOLDER_PLAYER_DESTROYED_MATERIAL;
	}
	
	public static OAPlaceholder getPlaceholder(String identifier) {
		OAPlaceholder ret = null;
		for (OAPlaceholder en : OAPlaceholder.values()) {
			if (en.name().equalsIgnoreCase(identifier)) {
				ret = en;
				break;
			}
		}
		
		if (identifier.toLowerCase().startsWith(DESTROYED_MATERIAL_PREFIX)) {
			ret = PLAYER_DESTROYEDMATERIAL;
		}
		return ret;
	}
	
	public String formatPlaceholder(OAPlayerImpl player, String identifier) {
		String ret = "";
		if (player != null) {
			String pFormat = format;
			if (this.equals(PLAYER_DESTROYEDMATERIAL)) {
				pFormat = pFormat.replace("{material}", identifier.substring(DESTROYED_MATERIAL_PREFIX.length()));
			}
			ret = plugin.getMessageUtils().convertPlayerPlaceholders(pFormat, player);
		}
		return ret;
	}
}