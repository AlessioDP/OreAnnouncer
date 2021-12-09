package com.alessiodp.oreannouncer.api.events.common;

import com.alessiodp.oreannouncer.api.events.OreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import org.jetbrains.annotations.NotNull;

public interface IAlertFoundEvent extends OreAnnouncerEvent {
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
}
