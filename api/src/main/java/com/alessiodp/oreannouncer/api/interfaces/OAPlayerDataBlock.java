package com.alessiodp.oreannouncer.api.interfaces;

import java.util.UUID;

@Deprecated
public interface OAPlayerDataBlock {
	/**
	 * Get the material name of the block
	 *
	 * @return Returns the name of the block
	 */
	@Deprecated
	String getMaterialName();
	
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	@Deprecated
	UUID getPlayer();
	
	/**
	 * Get the number of destroyed blocks
	 *
	 * @return Returns the number of destroyed blocks
	 */
	@Deprecated
	int getDestroyCount();
	
	/**
	 * Set the number of destroyed blocks
	 *
	 * @param destroyCount The number to set
	 */
	@Deprecated
	void setDestroyCount(int destroyCount);
}
