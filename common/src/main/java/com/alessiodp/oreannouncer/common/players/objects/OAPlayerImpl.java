package com.alessiodp.oreannouncer.common.players.objects;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public abstract class OAPlayerImpl implements OAPlayer {
	protected final OreAnnouncerPlugin plugin;
	@Getter private final HashMap<String, PlayerDataBlock> dataBlocks = new HashMap<>();
	
	@Getter @Setter private UUID playerUUID;
	@Getter @Setter private String name;
	@Setter private boolean alertsOn;
	
	@Getter private final ReentrantLock lock = new ReentrantLock();
	
	protected OAPlayerImpl(OreAnnouncerPlugin plugin, UUID uuid) {
		this.plugin = plugin;
		
		playerUUID = uuid;
		name = plugin.getOfflinePlayer(uuid).getName();
		if (name == null)
			name = "";
		alertsOn = true;
	}
	
	public void updatePlayer() {
		plugin.getDatabaseManager().updatePlayer(this);
	}
	
	public void updateName() {
		lock.lock(); // Lock
		String serverName = plugin.getOfflinePlayer(getPlayerUUID()).getName();
		if (!serverName.isEmpty() && !serverName.equals(getName())) {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_PLAYER_UPDATENAME
					.replace("{uuid}", getPlayerUUID().toString())
					.replace("{old}", getName())
					.replace("{new}", serverName), true);
			
			setName(serverName);
			updatePlayer();
		}
		lock.unlock(); // Unlock
	}
	
	public void loadBlocks(ArrayList<PlayerDataBlock> blocks) {
		lock.lock(); // Lock
		getDataBlocks().clear();
		for (PlayerDataBlock pdb : blocks) {
			getDataBlocks().put(pdb.getMaterialName().toLowerCase(), pdb);
		}
		lock.unlock(); // Unlock
		
		plugin.getLoggerManager().logDebug(OAConstants.DEBUG_PLAYER_LOADBLOCKS
				.replace("{uuid}", getPlayerUUID().toString())
				.replace("{number}", Integer.toString(getDataBlocks().size())), true);
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
		if (player.hasPermission(OreAnnouncerPermission.USER_TOP.toString()))
			ret.add(CommonCommands.TOP);
		if (player.hasPermission(OreAnnouncerPermission.ADMIN_VERSION.toString()))
			ret.add(CommonCommands.VERSION);
		
		return ret;
	}
	
	@Override
	public boolean haveAlertsOn(){
		return alertsOn;
	}
	
	public void sendNoPermission(OreAnnouncerPermission perm) {
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
		formattedMessage = plugin.getColorUtils().convertColors(formattedMessage);
		sendDirect(formattedMessage);
	}
	
	private void sendDirect(String message) {
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, false);
		}
	}
}
