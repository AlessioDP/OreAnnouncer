package com.alessiodp.oreannouncer.api.interfaces;

public interface OABlock {
	/**
	 * Get material name
	 *
	 * @return Returns the material name as String
	 */
	String getMaterialName();
	
	/**
	 * Is alerting users option enabled?
	 *
	 * @return Returns true if the option is enabled
	 */
	boolean isAlertingUsers();
	
	/**
	 * Set alert on users on/off
	 *
	 * @param alertingUsers True to enable alerts on users
	 */
	void setAlertingUsers(boolean alertingUsers);
	
	/**
	 * Is alerting users option enabled?
	 *
	 * @return Returns true if the option is enabled
	 */
	boolean isAlertingAdmins();
	
	/**
	 * Set alerts on admins on/off
	 *
	 * @param alertingAdmins True to enable alerts on admins
	 */
	void setAlertingAdmins(boolean alertingAdmins);
	
	/**
	 * Get singular block name
	 *
	 * @return Returns the singular name of the block
	 */
	String getSingularName();
	
	/**
	 * Set the singular name of the block
	 *
	 * @param singularName The singular name of the block
	 */
	void setSingularName(String singularName);
	
	/**
	 * Get plural block name
	 *
	 * @return Returns the plural name of the block
	 */
	String getPluralName();
	
	/**
	 * Set the plural name of the block
	 *
	 * @param pluralName The plural name of the block
	 */
	void setPluralName(String pluralName);
	
	/**
	 * Get custom message for users
	 *
	 * @return Returns the alert message for users
	 */
	String getMessageUser();
	
	/**
	 * Set the custom message for users
	 *
	 * @param messageUser The message to set
	 */
	void setMessageUser(String messageUser);
	
	/**
	 * Get custom message for admins
	 *
	 * @return Returns the alert message for admins
	 */
	String getMessageAdmin();
	
	/**
	 * Set the custom message for admins
	 *
	 * @param messageAdmin The message to set
	 */
	void setMessageAdmin(String messageAdmin);
	
	/**
	 * Get custom message for the console
	 *
	 * @return Returns the alert message for the console
	 */
	String getMessageConsole();
	
	/**
	 * Set the custom message for the console
	 *
	 * @param messageConsole The message to set
	 */
	void setMessageConsole(String messageConsole);
	
	/**
	 * Get sound name
	 *
	 * @return Returns the name of the sound
	 */
	String getSound();
	
	/**
	 * Set sound name
	 * @param sound The sound name to set
	 */
	void setSound(String sound);
	
	/**
	 * Get light level value
	 *
	 * @return Returns the light level of the block
	 */
	int getLightLevel();
	
	/**
	 * Set light level cap
	 *
	 * @param lightLevel The light level to set
	 */
	void setLightLevel(int lightLevel);
	
	/**
	 * Is count on destroy enabled?
	 *
	 * @return Returns true if the option is enabled
	 */
	boolean isCountingOnDestroy();
	
	/**
	 * Set count on destroy option on/off
	 *
	 * @param countingOnDestroy True to enable count on destroy
	 */
	void setCountingOnDestroy(boolean countingOnDestroy);
}
