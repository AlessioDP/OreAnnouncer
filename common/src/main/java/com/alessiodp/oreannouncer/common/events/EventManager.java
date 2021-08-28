package com.alessiodp.oreannouncer.common.events;

import com.alessiodp.core.common.events.EventDispatcher;
import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.events.common.IAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IAlertEvent;
import com.alessiodp.oreannouncer.api.events.common.IBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EventManager {
	@NonNull protected final OreAnnouncerPlugin plugin;
	@NonNull protected final EventDispatcher eventDispatcher;
	
	public final void callEvent(OreAnnouncerEvent event) {
		event.setApi(plugin.getApi());
		eventDispatcher.callEvent(event);
	}
	
	public abstract IAlertEvent prepareAlertEvent(OAPlayer player, OABlock block, int number, BlockLocation location, int lightLevel, int heightLevel);
	public abstract IAdvancedAlertEvent prepareAdvancedEvent(OAPlayer player, OABlock block, int total, long elapsedTime, BlockLocation location, int lightLevel, int heightLevel);
	public abstract IBlockDestroyEvent prepareBlockDestroyEvent(OAPlayer player, OABlock block, BlockLocation blockLocation);
}
