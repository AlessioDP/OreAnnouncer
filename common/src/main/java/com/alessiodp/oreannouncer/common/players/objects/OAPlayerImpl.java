package com.alessiodp.oreannouncer.common.players.objects;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPPermission;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OABlockDestroy;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode(doNotUseGetters = true)
public abstract class OAPlayerImpl implements OAPlayer {
	@EqualsAndHashCode.Exclude protected final OreAnnouncerPlugin plugin;
	
	@Getter @Setter private UUID playerUUID;
	private boolean alertsOn;
	@EqualsAndHashCode.Exclude @Getter private String name;
	
	@EqualsAndHashCode.Exclude @Getter private final ReentrantLock lock = new ReentrantLock();
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
	
	public void updatePlayer() {
		plugin.getDatabaseManager().updatePlayer(this);
	}
	
	private void updateValue(Runnable runnable) {
		if (accessible) {
			runnable.run();
		} else {
			lock.lock();
			runnable.run();
			updatePlayer();
			lock.unlock();
		}
	}
	
	public List<ADPCommand> getAllowedCommands() {
		List<ADPCommand> ret = new ArrayList<>();
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (player.hasPermission(OreAnnouncerPermission.USER_HELP.toString()))
			ret.add(CommonCommands.HELP);
		if (player.hasPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE.toString()))
			ret.add(CommonCommands.ALERTS);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_RELOAD.toString()))
			ret.add(CommonCommands.RELOAD);
		if (player.hasPermission(OreAnnouncerPermission.USER_STATS.toString()))
			ret.add(CommonCommands.STATS);
		if (player.hasPermission(OreAnnouncerPermission.USER_TOP.toString())
				|| player.hasPermission(OreAnnouncerPermission.USER_TOP_DESTROY.toString())
				|| player.hasPermission(OreAnnouncerPermission.USER_TOP_FOUND.toString()))
			ret.add(CommonCommands.TOP);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_LOG.toString()))
			ret.add(CommonCommands.LOG);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_VERSION.toString()))
			ret.add(CommonCommands.VERSION);
		
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
}
