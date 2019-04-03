package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

public class CommandStats extends ADPSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandStats(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(OreAnnouncerPermission.USER_STATS.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		commandData.addPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null)
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_STATS
				.replace("{player}", player.getName())
				.replace("{victim}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_STATS_CONSOLE
							.replace("{victim}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		
		// Command handling
		OAPlayerImpl targetPlayer = null;
		if (commandData.getArgs().length > 1
				&& commandData.havePermission(OreAnnouncerPermission.ADMIN_STATS_OTHER)) {
			targetPlayer = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getPlayerByName(commandData.getArgs()[1]);
			if (targetPlayer == null) {
				// Not found
				sendMessage(player, Messages.CMD_STATS_PLAYERNOTFOUND
						.replace("%player%", commandData.getArgs()[1]));
				return;
			}
		}
		
		if (targetPlayer == null) {
			if (player == null) {
				plugin.logConsole(plugin.getColorUtils().removeColors(Messages.CMD_STATS_PLAYERNOTFOUND
						.replace("%player%", "")), false);
				return;
			} else
				targetPlayer = player;
		}
		
		// Command starts
		for (String line : Messages.CMD_STATS_CONTENT) {
			sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(line, targetPlayer));
		}
		
		
	}
	
	private void sendMessage(OAPlayerImpl player, String message) {
		if (player != null)
			player.sendMessage(message);
		else
			plugin.logConsole(plugin.getColorUtils().removeColors(message), false);
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}
