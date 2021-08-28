package com.alessiodp.oreannouncer.api.events.bukkit;

import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BukkitOreAnnouncerEvent extends Event implements OreAnnouncerEvent {
	private OreAnnouncerAPI api;
	private static final HandlerList HANDLERS = new HandlerList();
	
	public BukkitOreAnnouncerEvent(boolean async) {
		super(async);
	}
	
	@Override
	@NotNull
	public OreAnnouncerAPI getApi() {
		return api;
	}
	
	@Override
	public void setApi(OreAnnouncerAPI instance) {
		api = instance;
	}
	
	@Override
	@NotNull
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
