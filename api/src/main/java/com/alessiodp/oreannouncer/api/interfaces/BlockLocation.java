package com.alessiodp.oreannouncer.api.interfaces;

import org.jetbrains.annotations.NotNull;

public interface BlockLocation {
	/**
	 * Gets the world name of the location
	 *
	 * @return Returns the world name
	 */
	@NotNull
	String getWorld();
	
	/**
	 * Get the x-coordinate of the location
	 *
	 * @return Returns the x-coordinate
	 */
	double getX();
	
	/**
	 * Get the y-coordinate of the location
	 *
	 * @return Returns the y-coordinate
	 */
	double getY();
	
	/**
	 * Get the z-coordinate of the location
	 *
	 * @return Returns the z-coordinate
	 */
	double getZ();
	
	/**
	 * Get the yaw of the location
	 *
	 * @return Returns the yaw
	 */
	float getYaw();
	
	/**
	 * Get the pitch of the location
	 *
	 * @return Returns the pitch
	 */
	float getPitch();
}
