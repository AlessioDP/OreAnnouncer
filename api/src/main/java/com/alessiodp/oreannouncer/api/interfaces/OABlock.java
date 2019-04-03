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
