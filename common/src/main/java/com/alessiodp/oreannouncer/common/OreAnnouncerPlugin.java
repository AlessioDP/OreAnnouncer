package com.alessiodp.oreannouncer.common;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.logging.ConsoleColor;
import com.alessiodp.core.common.messaging.ADPMessenger;
import com.alessiodp.oreannouncer.api.OreAnnouncer;
import com.alessiodp.oreannouncer.common.api.ApiHandler;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.MessageUtils;
import com.alessiodp.oreannouncer.common.utils.OAPlayerUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class OreAnnouncerPlugin extends ADPPlugin {
	// Plugin fields
	@Getter private final String pluginName = OAConstants.PLUGIN_NAME;
	@Getter private final String pluginFallbackName = OAConstants.PLUGIN_FALLBACK;
	@Getter private final ConsoleColor consoleColor = OAConstants.PLUGIN_CONSOLECOLOR;
	
	// OreAnnouncer fields
	@Getter protected BlockManager blockManager;
	@Getter @Setter protected PlayerManager playerManager;
	@Getter protected MessageUtils messageUtils;
	@Getter protected ADPMessenger messenger;
	
	@Getter @Setter protected Runnable onReloadEvent = () -> {};
	
	public OreAnnouncerPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	public void onDisabling() {
		// Nothing to disable
	}
	
	@Override
	protected void initializeCore() {
		databaseManager = new OADatabaseManager(this);
	}
	
	@Override
	protected void loadCore() {
		getConfigurationManager().reload();
		reloadLoggerManager();
		getDatabaseManager().reload();
	}
	
	@Override
	protected void postHandle() {
		ApiHandler api = new ApiHandler(this);
		playerUtils = new OAPlayerUtils(this);
		
		getPlayerManager().reload();
		getCommandManager().setup();
		getMessenger().reload();
		registerListeners();
		
		reloadAdpUpdater();
		getAddonManager().loadAddons();
		OreAnnouncer.setApi(api);
	}
	
	protected abstract void registerListeners();
	
	@Override
	public void reloadConfiguration() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_RELOADING, true);
		getLoginAlertsManager().reload();
		getConfigurationManager().reload();
		reloadLoggerManager();
		getDatabaseManager().reload();
		
		getPlayerManager().reload();
		getAddonManager().loadAddons();
		getCommandManager().setup();
		getMessenger().reload();
		
		reloadAdpUpdater();
		
		onReloadEvent.run();
	}
	
	@Override
	public OADatabaseManager getDatabaseManager() {
		return (OADatabaseManager) databaseManager;
	}
	
	private void reloadLoggerManager() {
		getLoggerManager().reload(
				ConfigMain.OREANNOUNCER_LOGGING_DEBUG,
				ConfigMain.OREANNOUNCER_LOGGING_SAVE_ENABLE,
				ConfigMain.OREANNOUNCER_LOGGING_SAVE_FILE,
				ConfigMain.OREANNOUNCER_LOGGING_SAVE_FORMAT
		);
	}
	
	private void reloadAdpUpdater() {
		getAdpUpdater().reload(
				getPluginFallbackName(),
				OAConstants.PLUGIN_SPIGOTCODE,
				ConfigMain.OREANNOUNCER_UPDATES_CHECK,
				ConfigMain.OREANNOUNCER_UPDATES_WARN,
				OreAnnouncerPermission.ADMIN_WARNINGS.toString(),
				Messages.OREANNOUNCER_UPDATEAVAILABLE
		);
		getAdpUpdater().asyncTaskCheckUpdates();
	}
	
	public abstract boolean isBungeeCordEnabled();
}
