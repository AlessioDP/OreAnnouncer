package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.commands.utils.ADPPermission;

public enum OreAnnouncerPermission implements ADPPermission {
	
	USER_ALERTS_SEE			("oreannouncer.user.alerts.see"),
	USER_ALERTS_SEE_SERVER	("oreannouncer.user.alerts.see.server."),
	USER_ALERTS_TOGGLE		("oreannouncer.user.alerts.toggle"),
	USER_HELP				("oreannouncer.user.help"),
	USER_STATS				("oreannouncer.user.stats"),
	USER_TOP				("oreannouncer.user.top"),
	
	ADMIN_ALERTS_SEE		("oreannouncer.admin.alerts.see"),
	ADMIN_ALERTS_SEE_SERVER	("oreannouncer.admin.alerts.see.server."),
	ADMIN_BYPASS_ALERTS		("oreannouncer.admin.bypass.alerts"),
	ADMIN_BYPASS_DESTROY	("oreannouncer.admin.bypass.destroy"),
	ADMIN_BYPASS_FOUND		("oreannouncer.admin.bypass.found"),
	ADMIN_LOG				("oreannouncer.admin.log"),
	ADMIN_RELOAD			("oreannouncer.admin.reload"),
	ADMIN_STATS_OTHER		("oreannouncer.admin.stats.other"),
	ADMIN_VERSION			("oreannouncer.admin.version"),
	ADMIN_WARNINGS			("oreannouncer.admin.warnings");
	
	private final String perm;
	OreAnnouncerPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
