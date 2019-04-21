package com.alessiodp.oreannouncer.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.AddonManager;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import lombok.NonNull;

public abstract class OAAddonManager extends AddonManager {
	private final LLAPIHandler llapiHandler;
	
	public OAAddonManager(@NonNull ADPPlugin plugin) {
		super(plugin);
		llapiHandler = new LLAPIHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		plugin.getLoggerManager().logDebug(Constants.DEBUG_ADDON_INIT, true);
		
		llapiHandler.init();
	}
}
