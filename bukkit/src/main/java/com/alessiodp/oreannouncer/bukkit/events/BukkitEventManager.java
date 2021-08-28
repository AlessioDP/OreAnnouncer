package com.alessiodp.oreannouncer.bukkit.events;

import com.alessiodp.core.bukkit.events.BukkitEventDispatcher;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.events.common.IAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.events.EventManager;
import lombok.NonNull;

public class BukkitEventManager extends EventManager {
	public BukkitEventManager(@NonNull OreAnnouncerPlugin plugin) {
		super(plugin, new BukkitEventDispatcher(plugin));
	}
	
	@Override
	public IAlertEvent prepareAlertEvent(OAPlayer player, OABlock block, int number, BlockLocation location, int lightLevel, int heightLevel) {
		return new BukkitOreAnnouncerAlertEvent(player, block, number, location, lightLevel, heightLevel);
	}
	
	@Override
	public IAdvancedAlertEvent prepareAdvancedEvent(OAPlayer player, OABlock block, int total, long elapsedTime, BlockLocation location, int lightLevel, int heightLevel) {
		return new BukkitOreAnnouncerAdvancedAlertEvent(player, block, total, elapsedTime, location, lightLevel, heightLevel);
	}
	
	@Override
	public IBlockDestroyEvent prepareBlockDestroyEvent(OAPlayer player, OABlock block, BlockLocation blockLocation) {
		return new BukkitOreAnnouncerBlockDestroyEvent(player, block, blockLocation);
	}
}
