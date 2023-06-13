package com.alessiodp.oreannouncer.bukkit;

import com.alessiodp.core.bukkit.addons.internal.BukkitJsonHandler;
import com.alessiodp.core.bukkit.addons.internal.SpigotJsonHandler;
import com.alessiodp.core.bukkit.scheduling.ADPBukkitScheduler;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.oreannouncer.bukkit.addons.BukkitOAAddonManager;
import com.alessiodp.oreannouncer.bukkit.addons.external.BukkitMetricsHandler;
import com.alessiodp.oreannouncer.bukkit.blocks.BukkitBlockManager;
import com.alessiodp.oreannouncer.bukkit.bootstrap.BukkitOreAnnouncerBootstrap;
import com.alessiodp.oreannouncer.bukkit.commands.BukkitOACommandManager;
import com.alessiodp.oreannouncer.bukkit.configuration.BukkitOAConfigurationManager;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.bukkit.events.BukkitEventManager;
import com.alessiodp.oreannouncer.bukkit.listeners.BukkitBlockListener;
import com.alessiodp.oreannouncer.bukkit.listeners.BukkitJoinLeaveListener;
import com.alessiodp.oreannouncer.bukkit.messaging.BukkitOAMessenger;
import com.alessiodp.oreannouncer.bukkit.players.BukkitPlayerManager;
import com.alessiodp.oreannouncer.bukkit.utils.BukkitMessageUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public class BukkitOreAnnouncerPlugin extends OreAnnouncerPlugin {
	@Getter private final int bstatsId = OAConstants.PLUGIN_BSTATS_BUKKIT_ID;
	
	public BukkitOreAnnouncerPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPBukkitScheduler(this);
		configurationManager = new BukkitOAConfigurationManager(this);
		messageUtils = new BukkitMessageUtils(this);
		messenger = new BukkitOAMessenger(this);
		
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
		addonManager = new BukkitOAAddonManager(this);
		eventManager = new BukkitEventManager(this);
		
		super.postHandle();
		
		new BukkitMetricsHandler(this);
	}
	
	@Override
	protected void initializeJsonHandler() {
		if (((BukkitOreAnnouncerBootstrap) getBootstrap()).isSpigot())
			jsonHandler = new SpigotJsonHandler(this);
		else
			jsonHandler = new BukkitJsonHandler(this);
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_REGISTERING, true);
		PluginManager pm = ((Plugin) getBootstrap()).getServer().getPluginManager();
		pm.registerEvents(new BukkitBlockListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitJoinLeaveListener(this), ((Plugin) getBootstrap()));
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return BukkitConfigMain.OREANNOUNCER_BUNGEECORD_ENABLE;
	}
	
	@Override
	public String getServerName() {
		return BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_NAME;
	}
	
	@Override
	public String getServerId() {
		return BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_ID;
	}
}
