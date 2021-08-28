package com.alessiodp.oreannouncer.api.events.bungee;

import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public abstract class BungeeOreAnnouncerEvent extends Event implements OreAnnouncerEvent {
	private OreAnnouncerAPI api;
	
	@Override
	@NotNull
	public OreAnnouncerAPI getApi() {
		return api;
	}
	
	@Override
	public void setApi(OreAnnouncerAPI instance) {
		api = instance;
	}
}
