package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OASubCommand;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

import java.util.List;

public class CommandAlerts extends OASubCommand {
	
	public CommandAlerts(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.ALERTS,
				OreAnnouncerPermission.USER_ALERTS_TOGGLE,
				ConfigMain.COMMANDS_SUB_ALERTS,
				false
		);
		
		syntax = String.format("%s [%s/%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_ON,
				ConfigMain.COMMANDS_MISC_OFF
		);
		
		description = Messages.HELP_CMD_DESCRIPTIONS_ALERTS;
		help = Messages.HELP_CMD_ALERTS;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return handlePreRequisitesWithPermission(commandData);
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		// Command handling
		Boolean alerts = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(player.haveAlertsOn(), commandData.getArgs());
		if (alerts == null) {
			player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		player.setAlertsOn(alerts);
		
		if (alerts) {
			player.sendMessage(Messages.CMD_ALERTS_TOGGLEON);
		} else {
			player.sendMessage(Messages.CMD_ALERTS_TOGGLEOFF);
		}
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}
