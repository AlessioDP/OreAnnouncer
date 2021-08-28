package com.alessiodp.oreannouncer.api.events.common;

import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import org.jetbrains.annotations.NotNull;

public interface IAlertEvent extends OreAnnouncerEvent {
	
	/**
	 * Get the target player of the alert
	 *
	 * @return Returns the {@link OAPlayer}
	 */
	@NotNull
	OAPlayer getPlayer();
	
	/**
	 * Get the block
	 *
	 * @return Returns the {@link OABlock}
	 */
	@NotNull
	OABlock getBlock();
	
	/**
	 * Get the number of blocks that have been found
	 *
	 * @return Returns the number of blocks
	 */
	int getNumberOfBlocks();
	
	/**
	 * Get the location of the player when he found the blocks
	 *
	 * @return Returns the {@link BlockLocation}
	 */
	BlockLocation getLocation();
	
	/**
	 * Get the light level
	 *
	 * @return Returns the light level
	 */
	int getLightLevel();
	
	/**
	 * Get the height level
	 *
	 * @return Returns the height level
	 */
	int getHeightLevel();
}
