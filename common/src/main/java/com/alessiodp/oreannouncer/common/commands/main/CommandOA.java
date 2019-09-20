package com.alessiodp.oreannouncer.common.commands.main;

import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.sub.CommandAlerts;
import com.alessiodp.oreannouncer.common.commands.sub.CommandHelp;
import com.alessiodp.oreannouncer.common.commands.sub.CommandReload;
import com.alessiodp.oreannouncer.common.commands.sub.CommandStats;
import com.alessiodp.oreannouncer.common.commands.sub.CommandTop;
import com.alessiodp.oreannouncer.common.commands.sub.CommandVersion;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandOA extends ADPMainCommand {
	
	public CommandOA(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		commandName = ConfigMain.COMMANDS_CMD_OA;
		description = ConfigMain.COMMANDS_DESCRIPTION_OA;
		subCommands = new HashMap<>();
		enabledSubCommands = new ArrayList<>();
		tabSupport = ConfigMain.COMMANDS_TABSUPPORT;
		
		register(CommonCommands.HELP, new CommandHelp(plugin, this));
		register(CommonCommands.RELOAD, new CommandReload(plugin, this));
		register(CommonCommands.VERSION, new CommandVersion(plugin, this));
		
		if (ConfigMain.ALERTS_ENABLE)
			register(CommonCommands.ALERTS, new CommandAlerts(plugin, this));
		
		if (ConfigMain.STATS_ENABLE) {
			register(CommonCommands.STATS, new CommandStats(plugin, this));
			if (ConfigMain.STATS_TOP_ENABLE)
				register(CommonCommands.TOP, new CommandTop(plugin, this));
		}
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		String subCommand;
		if (sender.isPlayer()) {
			if (args.length == 0) {
				// Set /oa to /oa help
				subCommand = CommonCommands.HELP.getCommand().toLowerCase();
			} else {
				subCommand = args[0].toLowerCase();
			}
			
			if (exists(subCommand)) {
				plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
			} else {
				sender.sendMessage(Messages.OREANNOUNCER_COMMON_INVALIDCMD, true);
			}
		} else {
			// Console
			if (args.length > 0) {
				subCommand = args[0].toLowerCase();
				if (exists(subCommand) && getSubCommand(subCommand).isExecutableByConsole()) {
					plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
				} else {
					plugin.logConsole(plugin.getColorUtils().removeColors(Messages.OREANNOUNCER_COMMON_INVALIDCMD), false);
				}
			} else {
				// Print help
				for (String str : Messages.HELP_CONSOLEHELP) {
					plugin.logConsole(str, false);
				}
			}
		}
		return true;
	}
}
