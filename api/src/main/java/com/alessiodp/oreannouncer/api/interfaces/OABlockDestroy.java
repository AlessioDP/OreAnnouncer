package com.alessiodp.oreannouncer.api.interfaces;

import java.util.UUID;

public interface OABlockDestroy {
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	UUID getPlayer();
	
	/**
	 * Get the material name of the block
	 *
	 * @return Returns the name of the block
	 */
	String getMaterialName();
	
	/**
	 * Get the number of destroyed blocks
	 *
	 * @return Returns the number of destroyed blocks
	 */
	int getDestroyCount();
}
