package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
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

import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends ADPSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandHelp(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(OreAnnouncerPermission.USER_HELP.toString())) {
			player.sendNoPermission(OreAnnouncerPermission.USER_HELP);
			return false;
		}
		
		((OACommandData) commandData).setPlayer(player);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_HELP
				.replace("{player}", player.getName())
				.replace("{page}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		
		// Command starts
		// Get all allowed commands
		List<String> list = new ArrayList<>();
		for (ADPCommand cmd : player.getAllowedCommands()) {
			if (mainCommand.getEnabledSubCommands().contains(cmd))
				list.add(cmd.getHelp());
		}
		
		
		// Start printing
		player.sendMessage(Messages.HELP_HEADER);
		for (String string : list) {
			player.sendMessage(string);
		}
		player.sendMessage(Messages.HELP_FOOTER);
	}
}