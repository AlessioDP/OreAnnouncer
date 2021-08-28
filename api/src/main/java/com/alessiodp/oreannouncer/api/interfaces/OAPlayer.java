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
	
	/**
	 * Is the player whitelisted?
	 *
	 * @return Returns true if the player is whitelisted
	 */
	boolean isWhitelisted();
	
	/**
	 * Add or remove the player from the whitelist
	 *
	 * @param whitelisted True to whitelist
	 */
	void setWhitelisted(boolean whitelisted);
	
	/**
	 * Get block destroy stats of the player
	 *
	 * @param block The block
	 * @return Returns the {@link OABlockDestroy} instance
	 */
	OABlockDestroy getBlockDestroy(OABlock block);
	
	/**
	 * Set/update a block destroy of the player
	 *
	 * @param blockDestroy The {@link OABlockDestroy} to set/update
	 */
	void setBlockDestroy(OABlockDestroy blockDestroy);
	
	/**
	 * Get all blocks destroy stats of the player
	 *
	 * @return Returns a set of {@link OABlockDestroy}
	 */
	Set<OABlockDestroy> getAllBlockDestroy();
}
