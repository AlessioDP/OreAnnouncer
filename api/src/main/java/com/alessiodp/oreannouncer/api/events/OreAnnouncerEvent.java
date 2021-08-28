package com.alessiodp.oreannouncer.api.events;

import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;
import org.jetbrains.annotations.NotNull;

public interface OreAnnouncerEvent {
	/**
	 * Get the OreAnnouncer API instance
	 *
	 * @return Returns the {@link OreAnnouncerAPI}
	 */
	@NotNull
	OreAnnouncerAPI getApi();
	
	/**
	 * Set the OreAnnouncer API instance. Used by OreAnnouncer instance to let you hook directly to the main API.
	 *
	 * @param instance {@link OreAnnouncerAPI} instance to set
	 */
	void setApi(OreAnnouncerAPI instance);
}
