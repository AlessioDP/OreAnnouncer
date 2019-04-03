package com.alessiodp.oreannouncer.bukkit;

import com.alessiodp.core.bukkit.scheduling.ADPBukkitScheduler;
import com.alessiodp.core.bukkit.utils.BukkitColorUtils;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.oreannouncer.bukkit.addons.BukkitAddonManager;
import com.alessiodp.oreannouncer.bukkit.addons.external.BukkitMetricsHandler;
import com.alessiodp.oreannouncer.bukkit.blocks.BukkitBlockManager;
import com.alessiodp.oreannouncer.bukkit.commands.BukkitOACommandManager;
import com.alessiodp.oreannouncer.bukkit.configuration.BukkitOAConfigurationManager;
import com.alessiodp.oreannouncer.bukkit.listeners.BukkitBlockListener;
import com.alessiodp.oreannouncer.bukkit.listeners.BukkitJoinLeaveListener;
import com.alessiodp.oreannouncer.bukkit.players.BukkitPlayerManager;
import com.alessiodp.oreannouncer.bukkit.utils.BukkitMessageUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public class BukkitOreAnnouncerPlugin extends OreAnnouncerPlugin {
	
	public BukkitOreAnnouncerPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPBukkitScheduler(this);
		configurationManager = new BukkitOAConfigurationManager(this);
		messageUtils = new BukkitMessageUtils(this);
		
		super.initializeCore();
	}
	
	@Override
	protected void loadCore() {
		blockManager = new BukkitBlockManager(this);
		playerManager = new BukkitPlayerManager(this);
		commandManager = new BukkitOACommandManager(this);
		
		super.loadCore();
	}
	
	@Override
	protected void postHandle() {
		colorUtils = new BukkitColorUtils();
		addonManager = new BukkitAddonManager(this);
		
		super.postHandle();
		
		new BukkitMetricsHandler(this);
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(OAConstants.DEBUG_PLUGIN_REGISTERING, true);
		PluginManager pm = ((Plugin) getBootstrap()).getServer().getPluginManager();
		pm.registerEvents(new BukkitBlockListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitJoinLeaveListener(this), ((Plugin) getBootstrap()));
	}
}
