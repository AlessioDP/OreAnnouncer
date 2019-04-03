package com.alessiodp.oreannouncer.api;

import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;

public final class OreAnnouncer {
	private static OreAnnouncerAPI api = null;
	private static boolean flagHook = false;
	
	private OreAnnouncer() {
	}
	
	/**
	 * Get the {@link OreAnnouncerAPI} instance
	 *
	 * @return Returns the {@link OreAnnouncerAPI} interface
	 * @throws IllegalStateException if OreAnnouncerAPI has not been initialized, in other words,
	 *                               OreAnnouncerAPI has not been loaded
	 */
	public static OreAnnouncerAPI getApi() throws IllegalStateException {
		flagHook = true;
		if (api == null)
			throw new IllegalStateException("OreAnnouncerAPI has not been initialized");
		return api;
	}
	
	/**
	 * Set the OreAnnouncer API instance. This should not be used.
	 *
	 * @param instance The OreAnnouncerAPI instance.
	 */
	public static void setApi(OreAnnouncerAPI instance) {
		api = instance;
	}
	
	/**
	 * Flag to know if OreAnnouncer has been hooked
	 *
	 * @return Returns true if the API has been hooked at least one time
	 */
	public static boolean isFlagHook() {
		return flagHook;
	}
}