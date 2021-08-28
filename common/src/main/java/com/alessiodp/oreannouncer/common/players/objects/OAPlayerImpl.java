package com.alessiodp.oreannouncer.common.players.objects;

import com.alessiodp.core.common.bootstrap.PluginPlatform;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPPermission;
import com.alessiodp.core.common.scheduling.ADPScheduler;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OABlockDestroy;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.messaging.OAMessageDispatcher;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode(doNotUseGetters = true)
public abstract class OAPlayerImpl implements OAPlayer {
	@EqualsAndHashCode.Exclude protected final OreAnnouncerPlugin plugin;
	
	@Getter @Setter private UUID playerUUID;
	private boolean alertsOn;
	@Getter private boolean whitelisted;
	@EqualsAndHashCode.Exclude @Getter private String name;
	
	@EqualsAndHashCode.Exclude @ToString.Exclude private boolean accessible = false;
	
	protected OAPlayerImpl(OreAnnouncerPlugin plugin, UUID uuid) {
		this.plugin = plugin;
		
		playerUUID = uuid;
		alertsOn = true;
		name = plugin.getOfflinePlayer(uuid).getName();
		if (name == null || name.isEmpty())
			name = LLAPIHandler.getPlayerName(playerUUID); // Use LastLoginAPI to get the name
	}
	
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	public CompletableFuture<Void> updatePlayer() {
		return plugin.getDatabaseManager().updatePlayer(this);
	}
	
	private void updateValue(Runnable runnable) {
		if (accessible) {
			runnable.run();
		} else {
			synchronized (this) {
				runnable.run();
				
				updatePlayer().thenRun(this::sendPacketUpdate).exceptionally(ADPScheduler.exceptionally());
			}
		}
	}
	
	public Set<ADPCommand> getAllowedCommands() {
		Set<ADPCommand> ret = new HashSet<>();
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (player.hasPermission(OreAnnouncerPermission.USER_HELP))
			ret.add(CommonCommands.HELP);
		if (player.hasPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE))
			ret.add(CommonCommands.ALERTS);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_RELOAD))
			ret.add(CommonCommands.RELOAD);
		if (player.hasPermission(OreAnnouncerPermission.USER_STATS))
			ret.add(CommonCommands.STATS);
		if (player.hasPermission(OreAnnouncerPermission.USER_TOP)
				|| player.hasPermission(OreAnnouncerPermission.USER_TOP_DESTROY)
				|| player.hasPermission(OreAnnouncerPermission.USER_TOP_FOUND))
			ret.add(CommonCommands.TOP);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_DEBUG))
			ret.add(CommonCommands.DEBUG);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_LOG))
			ret.add(CommonCommands.LOG);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_VERSION))
			ret.add(CommonCommands.VERSION);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_WHITELIST))
			ret.add(CommonCommands.WHITELIST);
		
		return ret;
	}
	
	@Override
	public boolean haveAlertsOn(){
		return alertsOn;
	}
	
	@Override
	public void setAlertsOn(boolean alerts) {
		updateValue(() -> this.alertsOn = alerts);
	}
	
	@Override
	public void setWhitelisted(boolean whitelisted) {
		updateValue(() -> this.whitelisted = whitelisted);
	}
	
	@Override
	public OABlockDestroy getBlockDestroy(OABlock block) {
		return plugin.getDatabaseManager().getBlockDestroy(playerUUID, block);
	}
	
	@Override
	public void setBlockDestroy(OABlockDestroy blockDestroy) {
		plugin.getDatabaseManager().setBlockDestroy((BlockDestroy) blockDestroy);
	}
	
	@Override
	public Set<OABlockDestroy> getAllBlockDestroy() {
		return new HashSet<>(plugin.getDatabaseManager().getAllBlockDestroy(playerUUID));
	}
	
	public void sendNoPermission(ADPPermission perm) {
		sendMessage(Messages.OREANNOUNCER_NOPERMISSION
				.replace("%permission%", perm.toString()));
	}
	
	public void sendMessage(String message) {
		sendMessage(message, this);
	}
	
	public void sendMessage(String message, OAPlayerImpl victim) {
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = plugin.getMessageUtils().convertPlayerPlaceholders(message, victim);
		formattedMessage = Color.translateAlternateColorCodes(formattedMessage);
		sendDirect(formattedMessage);
	}
	
	private void sendDirect(String message) {
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, false);
		}
	}
	
	public void sendPacketUpdate() {
		if (plugin.getPlatform() == PluginPlatform.BUNGEECORD || plugin.isBungeeCordEnabled())
			((OAMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdatePlayer(this);
	}
}
