package com.alessiodp.oreannouncer.bungeecord;

import com.alessiodp.core.bungeecord.addons.internal.json.BungeeJsonHandler;
import com.alessiodp.core.bungeecord.scheduling.ADPBungeeScheduler;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.bungeecord.addons.BungeeOAAddonManager;
import com.alessiodp.oreannouncer.bungeecord.addons.external.BungeeMetricsHandler;
import com.alessiodp.oreannouncer.bungeecord.blocks.BungeeBlockManager;
import com.alessiodp.oreannouncer.bungeecord.commands.BungeeOACommandManager;
import com.alessiodp.oreannouncer.bungeecord.configuration.BungeeOAConfigurationManager;
import com.alessiodp.oreannouncer.bungeecord.listeners.BungeeJoinLeaveListener;
import com.alessiodp.oreannouncer.bungeecord.messaging.BungeeOAMessenger;
import com.alessiodp.oreannouncer.bungeecord.players.BungeePlayerManager;
import com.alessiodp.oreannouncer.bungeecord.utils.BungeeMessageUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeOreAnnouncerPlugin extends OreAnnouncerPlugin {
	@Getter private final int bstatsId = OAConstants.PLUGIN_BSTATS_BUNGEE_ID;
	
	public BungeeOreAnnouncerPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPBungeeScheduler(this);
		configurationManager = new BungeeOAConfigurationManager(this);
		messageUtils = new BungeeMessageUtils(this);
		messenger = new BungeeOAMessenger(this);
		
		super.initializeCore();
	}
	
	@Override
	protected void loadCore() {
		blockManager = new BungeeBlockManager(this);
		playerManager = new BungeePlayerManager(this);
		commandManager = new BungeeOACommandManager(this);
		
		super.loadCore();
	}
	
	@Override
	protected void postHandle() {
		addonManager = new BungeeOAAddonManager(this);
		
		super.postHandle();
		
		new BungeeMetricsHandler(this);
	}
	
	@Override
	protected void initializeJsonHandler() {
		jsonHandler = new BungeeJsonHandler();
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_REGISTERING, true);
		Plugin plugin = (Plugin) getBootstrap();
		PluginManager pm = plugin.getProxy().getPluginManager();
		pm.registerListener(plugin, new BungeeJoinLeaveListener(this));
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return false;
	}
	
	@Override
	public String getServerName() {
		return "";
	}
	
	@Override
	public String getServerId() {
		return "";
	}
}

