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
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CommandTop extends ADPSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandTop(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(OreAnnouncerPermission.USER_TOP.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.USER_TOP);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null)
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_TOP
					.replace("{player}", player.getName())
					.replace("{page}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_TOP_CONSOLE, true);
		
		// Command handling
		int selectedPage = 1;
		if (commandData.getArgs().length == 2) {
			try {
				selectedPage = Integer.parseInt(commandData.getArgs()[1]);
			} catch(NumberFormatException ex) {
				sendMessage(player, Messages.CMD_TOP_WRONGCMD);
				return;
			}
		}
		OADatabaseManager.TopOrderBy orderBy;
		switch (ConfigMain.STATS_TOP_ORDER_BY.toLowerCase()) {
			case "found":
				orderBy = OADatabaseManager.TopOrderBy.FOUND;
				break;
			case "destroy":
			default:
				orderBy = OADatabaseManager.TopOrderBy.DESTROY;
		}
		
		// Command starts
		int numberPlayers = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getTopPlayersNumber(orderBy);
		int limit = Math.min(ConfigMain.STATS_TOP_PAGESIZE, ConfigMain.STATS_TOP_NUMPLAYERS);
		int maxPages;
		if (numberPlayers == 0)
			maxPages = 1;
		else if (numberPlayers % ConfigMain.STATS_TOP_NUMPLAYERS == 0)
			maxPages = numberPlayers / ConfigMain.STATS_TOP_NUMPLAYERS;
		else
			maxPages = (numberPlayers / ConfigMain.STATS_TOP_NUMPLAYERS) + 1;
		
		if (selectedPage > maxPages)
			selectedPage = maxPages;
		
		int offset = selectedPage > 1 ? limit * (selectedPage - 1) : 0;
		ArrayList<OAPlayerImpl> players = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getTopPlayersDestroyed(orderBy, limit, offset);
		
		sendMessage(player, Messages.CMD_TOP_HEADER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages%", Integer.toString(maxPages)));
		
		if (players.size() > 0) {
			for (OAPlayerImpl p : players) {
				sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_TOP_FORMATPLAYER, p));
			}
		} else {
			sendMessage(player, Messages.CMD_TOP_NOONE);
		}
		
		sendMessage(player, Messages.CMD_TOP_FOOTER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages", Integer.toString(maxPages)));
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
