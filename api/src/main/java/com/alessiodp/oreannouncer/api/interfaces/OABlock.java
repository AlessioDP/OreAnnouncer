package com.alessiodp.oreannouncer.api.interfaces;

public interface OABlock {
	
	/**
	 * Get material name
	 *
	 * @return Returns the material name as String
	 */
	String getMaterialName();
	
	/**
	 * Is the block enabled?
	 *
	 * @return Returns true if its enabled
	 */
	boolean isEnabled();
	
	/**
	 * Enable/disable the block
	 *
	 * @param enable True to enable
	 */
	void setEnabled(boolean enable);
	
	/**
	 * Get display name
	 *
	 * @return Returns the display name
	 */
	String getDisplayName();
	
	/**
	 * Set the display name
	 *
	 * @param displayName The display name to set
	 */
	void setDisplayName(String displayName);
	
	/**
	 * Get display color
	 *
	 * @return Returns the display color
	 */
	String getDisplayColor();
	
	/**
	 * Set the display color code
	 *
	 * @param displayColor The display color to set
	 */
	void setDisplayColor(String displayColor);
	
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
	 * Get the count number
	 *
	 * @return Returns the count number of the block
	 */
	int getCountNumber();
	
	/**
	 * Set the count number of the block
	 *
	 * @param number The count number of the block
	 */
	void setCountNumber(int number);
	
	/**
	 * Get the count time
	 *
	 * @return Returns the count time of the block
	 */
	int getCountTime();
	
	/**
	 * Set the count time of the block
	 *
	 * @param time The count time of the block
	 */
	void setCountTime(int time);
	
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
	 * Get custom count message for users
	 *
	 * @return Returns the count alert message for users
	 */
	String getCountMessageUser();
	
	/**
	 * Set the custom count message for users
	 *
	 * @param countMessageUser The message to set
	 */
	void setCountMessageUser(String countMessageUser);
	
	/**
	 * Get custom count message for admins
	 *
	 * @return Returns the count alert message for admins
	 */
	String getCountMessageAdmin();
	
	/**
	 * Set the custom count message for admins
	 *
	 * @param countMessageAdmin The message to set
	 */
	void setCountMessageAdmin(String countMessageAdmin);
	
	/**
	 * Get custom count message for the console
	 *
	 * @return Returns the count alert message for the console
	 */
	String getCountMessageConsole();
	
	/**
	 * Set the custom message for the console
	 *
	 * @param countMessageConsole The message to set
	 */
	void setCountMessageConsole(String countMessageConsole);
	
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
	
	/**
	 * Is TNT support enabled?
	 *
	 * @return Returns true if the option is enabled
	 */
	boolean isTNTEnabled();
	
	/**
	 * Set TNT support option on/off
	 *
	 * @param tntEnabled True to enable TNT support
	 */
	void setTNTEnabled(boolean tntEnabled);
	
	/**
	 * Get priority value
	 *
	 * @return Returns the priority
	 */
	int getPriority();
	
	/**
	 * Set the priority value
	 *
	 * @param priority The priority to set
	 */
	void setPriority(int priority);
}
