package com.alessiodp.oreannouncer.api.interfaces;

import java.util.Set;
import java.util.UUID;

public interface OAPlayer {
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	UUID getPlayerUUID();
	
	/**
	 * Get the name
	 *
	 * @return Returns the name of the player
	 */
	String getName();
	
	/**
	 * Have the player alerts messages enabled?
	 *
	 * @return Returns true if the player have alerts on
	 */
	boolean haveAlertsOn();
	
	/**
	 * Set alerts on/off
	 *
	 * @param alerts True to enable alerts
	 */
	void setAlertsOn(boolean alerts);
	
	OABlockDestroy getBlockDestroy(OABlock block);
	
	void setBlockDestroy(OABlockDestroy blockDestroy);
	
	Set<OABlockDestroy> getAllBlockDestroy();
}
