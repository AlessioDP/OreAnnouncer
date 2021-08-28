package com.alessiodp.oreannouncer.common.commands.utils;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPPermission;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

import java.util.Set;
import java.util.UUID;

public abstract class OASubCommand extends ADPSubCommand  {
	public OASubCommand(@NonNull ADPPlugin plugin, @NonNull ADPMainCommand mainCommand, @NonNull ADPCommand command, @NonNull ADPPermission permission, @NonNull String commandName, boolean executableByConsole) {
		super(plugin, mainCommand, command, permission, commandName, executableByConsole);
	}
	
	public void sendMessage(User receiver, OAPlayerImpl playerReceiver, String message) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message);
		else
			receiver.sendMessage(message, true);
	}
	
	public OAPlayerImpl playerLookup(String playerName) {
		OAPlayerImpl ret = null;
		User targetUser = plugin.getPlayerByName(playerName);
		if (targetUser != null) {
			ret = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetUser.getUUID());
		} else {
			Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
			if (targetPlayersUuid.size() > 0) {
				ret = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetPlayersUuid.iterator().next());
			}
		}
		return ret;
	}
	
	protected boolean handlePreRequisitesWithPermission(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				player.sendNoPermission(permission);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		return true;
	}
}
