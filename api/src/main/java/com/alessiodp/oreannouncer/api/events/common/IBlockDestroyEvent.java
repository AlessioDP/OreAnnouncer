package com.alessiodp.oreannouncer.api.events.common;

import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import org.jetbrains.annotations.NotNull;

public interface IBlockDestroyEvent extends OreAnnouncerEvent {
	/**
	 * Get the player who destroy the block
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
	 * Get the block location
	 *
	 * @return Returns the {@link BlockLocation}
	 */
	@NotNull
	BlockLocation getBlockLocation();
}
