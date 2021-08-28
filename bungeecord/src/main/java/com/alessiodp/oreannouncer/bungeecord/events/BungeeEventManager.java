package com.alessiodp.oreannouncer.bungeecord.events;

import com.alessiodp.core.bungeecord.events.BungeeEventDispatcher;
import com.alessiodp.oreannouncer.api.events.bungee.common.BungeeOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.bungee.common.BungeeOreAnnouncerAlertEvent;
import com.alessiodp.oreannouncer.api.events.bungee.common.BungeeOreAnnouncerBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.events.common.IAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.events.EventManager;
import lombok.NonNull;

public class BungeeEventManager extends EventManager {
	public BungeeEventManager(@NonNull OreAnnouncerPlugin plugin) {
		super(plugin, new BungeeEventDispatcher(plugin));
	}
	
	@Override
	public IAlertEvent prepareAlertEvent(OAPlayer player, OABlock block, int number, BlockLocation location, int lightLevel, int heightLevel) {
		return new BungeeOreAnnouncerAlertEvent(player, block, number, location, lightLevel, heightLevel);
	}
	
	@Override
	public IAdvancedAlertEvent prepareAdvancedEvent(OAPlayer player, OABlock block, int total, long elapsedTime, BlockLocation location, int lightLevel, int heightLevel) {
		return new BungeeOreAnnouncerAdvancedAlertEvent(player, block, total, elapsedTime, location, lightLevel, heightLevel);
	}
	
	@Override
	public IBlockDestroyEvent prepareBlockDestroyEvent(OAPlayer player, OABlock block, BlockLocation blockLocation) {
		return new BungeeOreAnnouncerBlockDestroyEvent(player, block, blockLocation);
	}
}
