package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OASubCommand;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class CommandWhitelist extends OASubCommand {
	
	public CommandWhitelist(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.WHITELIST,
				OreAnnouncerPermission.ADMIN_WHITELIST,
				ConfigMain.COMMANDS_SUB_WHITELIST,
				true
		);
		
		syntax = String.format("%s [%s/%s] <%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_ADD,
				ConfigMain.COMMANDS_MISC_REMOVE,
				ConfigMain.COMMANDS_MISC_PLAYER
		);
		
		description = Messages.HELP_CMD_DESCRIPTIONS_WHITELIST;
		help = Messages.HELP_CMD_WHITELIST;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return handlePreRequisitesWithPermission(commandData);
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		// Command handling
		OAPlayerImpl targetPlayer;
		CommandType commandType = CommandType.TOGGLE;
		if (commandData.getArgs().length == 2) {
			// Toggle
			targetPlayer = playerLookup(commandData.getArgs()[1]);
			if (targetPlayer == null) {
				sendMessage(sender, player, Messages.OREANNOUNCER_COMMON_PLAYER_NOT_FOUND
						.replace("%player%", commandData.getArgs()[1]));
				return;
			}
		} else if (commandData.getArgs().length == 3) {
			// Add or remove
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_ADD))
				commandType = CommandType.ADD;
			else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE))
				commandType = CommandType.REMOVE;
			else {
				sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntax));
				return;
			}
			
			targetPlayer = playerLookup(commandData.getArgs()[2]);
			if (targetPlayer == null) {
				sendMessage(sender, player, Messages.OREANNOUNCER_COMMON_PLAYER_NOT_FOUND
						.replace("%player%", commandData.getArgs()[2]));
				return;
			}
		} else {
			sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		boolean toggledValue;
		switch (commandType) {
			case ADD:
				toggledValue = true;
				break;
			case REMOVE:
				toggledValue = false;
				break;
			case TOGGLE:
			default:
				toggledValue = !targetPlayer.isWhitelisted();
				break;
		}
		
		targetPlayer.setWhitelisted(toggledValue);
		
		if (toggledValue) {
			sendMessage(sender, player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_WHITELIST_ADDED, targetPlayer));
		} else {
			sendMessage(sender, player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_WHITELIST_REMOVED, targetPlayer));
		}
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		if (args.length == 2)
			return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
		else if (args.length == 3)
			return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 2);
		return Collections.emptyList();
	}
	
	private enum CommandType {
		TOGGLE, ADD, REMOVE
	}
}
