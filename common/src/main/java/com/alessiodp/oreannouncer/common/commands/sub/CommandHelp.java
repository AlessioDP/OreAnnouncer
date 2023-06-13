package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPExecutableCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OASubCommand;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

import java.util.Map;
import java.util.Set;

public class CommandHelp extends OASubCommand {
	
	public CommandHelp(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.HELP,
				OreAnnouncerPermission.USER_HELP,
				ConfigMain.COMMANDS_SUB_HELP,
				false
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_CMD_DESCRIPTIONS_HELP;
		help = Messages.HELP_CMD_HELP;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return handlePreRequisitesWithPermission(commandData);
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		// Command starts
		player.sendMessage(Messages.HELP_HEADER);
		
		Set<ADPCommand> allowedCommands = player.getAllowedCommands();
		for(Map.Entry<ADPCommand, ADPExecutableCommand> e : plugin.getCommandManager().getOrderedCommands().entrySet()) {
			if (allowedCommands.contains(e.getKey()) && e.getValue().isListedInHelp()) {
				player.sendMessage(e.getValue().getHelp()
						.replace("%syntax%", e.getValue().getSyntaxForUser(commandData.getSender()))
						.replace("%description%", e.getValue().getDescription())
						.replace("%run_command%", e.getValue().getRunCommand())
						.replace("%perform_command%", Messages.HELP_PERFORM_COMMAND));
			}
		}
		
		player.sendMessage(Messages.HELP_FOOTER);
	}
}