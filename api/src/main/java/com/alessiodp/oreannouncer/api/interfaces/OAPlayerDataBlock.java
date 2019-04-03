package com.alessiodp.oreannouncer.api.interfaces;

import java.util.UUID;

public interface OAPlayerDataBlock {
	/**
	 * Get the material name of the block
	 *
	 * @return Returns the name of the block
	 */
	String getMaterialName();
	
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	UUID getPlayer();
	
	/**
	 * Get the number of destroyed blocks
	 *
	 * @return Returns the number of destroyed blocks
	 */
	int getDestroyCount();
	
	/**
	 * Set the number of destroyed blocks
	 *
	 * @param destroyCount The number to set
	 */
	void setDestroyCount(int destroyCount);
}
