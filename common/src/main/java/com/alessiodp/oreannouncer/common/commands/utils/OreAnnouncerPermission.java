package com.alessiodp.oreannouncer.common.commands.utils;

import com.alessiodp.core.common.commands.utils.ADPPermission;

public enum OreAnnouncerPermission implements ADPPermission {
	
	USER_ALERTS_SEE		("oreannouncer.user.alerts.see"),
	USER_ALERTS_TOGGLE	("oreannouncer.user.alerts.toggle"),
	USER_STATS			("oreannouncer.user.stats"),
	USER_TOP			("oreannouncer.user.top"),
	USER_HELP			("oreannouncer.user.help"),
	
	ADMIN_ALERTS_SEE	("oreannouncer.admin.alerts.see"),
	ADMIN_RELOAD		("oreannouncer.admin.reload"),
	ADMIN_STATS_OTHER	("oreannouncer.admin.stats.other"),
	ADMIN_UPDATES		("oreannouncer.admin.updates"),
	ADMIN_VERSION		("oreannouncer.admin.version");
	
	private final String perm;
	OreAnnouncerPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
