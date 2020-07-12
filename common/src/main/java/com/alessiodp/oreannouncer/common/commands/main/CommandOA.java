package com.alessiodp.oreannouncer.common.commands.main;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPExecutableCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.sub.CommandAlerts;
import com.alessiodp.oreannouncer.common.commands.sub.CommandDebug;
import com.alessiodp.oreannouncer.common.commands.sub.CommandHelp;
import com.alessiodp.oreannouncer.common.commands.sub.CommandLog;
import com.alessiodp.oreannouncer.common.commands.sub.CommandReload;
import com.alessiodp.oreannouncer.common.commands.sub.CommandStats;
import com.alessiodp.oreannouncer.common.commands.sub.CommandTop;
import com.alessiodp.oreannouncer.common.commands.sub.CommandVersion;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;

import java.util.HashMap;
import java.util.Map;

public class CommandOA extends ADPMainCommand {
	
	public CommandOA(OreAnnouncerPlugin plugin) {
		super(plugin, CommonCommands.OA, ConfigMain.COMMANDS_CMD_OA, true);
		
		description = ConfigMain.COMMANDS_DESCRIPTION_OA;
		subCommands = new HashMap<>();
		subCommandsByEnum = new HashMap<>();
		tabSupport = ConfigMain.COMMANDS_TABSUPPORT;
		
		register(new CommandHelp(plugin, this));
		register(new CommandReload(plugin, this));
		register(new CommandVersion(plugin, this));
		
		if (ConfigMain.OREANNOUNCER_DEBUG_COMMAND)
			register(new CommandDebug(plugin, this));
		
		if (ConfigMain.ALERTS_ENABLE)
			register(new CommandAlerts(plugin, this));
		
		if (ConfigMain.STATS_ENABLE) {
			register(new CommandStats(plugin, this));
			if (ConfigMain.STATS_TOP_ENABLE)
				register(new CommandTop(plugin, this));
			
			if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE && ConfigMain.STATS_ADVANCED_COUNT_LOG_ENABLE)
				register(new CommandLog(plugin, this));
		}
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		String subCommand;
		if (sender.isPlayer()) {
			if (args.length == 0) {
				// Set /llapi to /llapi help
				subCommand = CommonUtils.toLowerCase(ConfigMain.COMMANDS_CMD_HELP);
			} else {
				subCommand = CommonUtils.toLowerCase(args[0]);
			}
			
			if (exists(subCommand)) {
				plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
			} else {
				sender.sendMessage(Messages.OREANNOUNCER_COMMON_INVALIDCMD, true);
			}
		} else {
			// Console
			if (args.length > 0) {
				subCommand = CommonUtils.toLowerCase(args[0]);
				if (exists(subCommand) && getSubCommand(subCommand).isExecutableByConsole()) {
					plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
				} else {
					plugin.logConsole(Color.translateAndStripColor(Messages.OREANNOUNCER_COMMON_INVALIDCMD), false);
				}
			} else {
				// Print help
				plugin.logConsole(Messages.HELP_CONSOLEHELP_HEADER, false);
				for(Map.Entry<ADPCommand, ADPExecutableCommand> e : plugin.getCommandManager().getOrderedCommands().entrySet()) {
					if (e.getValue().isExecutableByConsole()  && e.getValue().isListedInHelp()) {
						plugin.logConsole(Messages.HELP_CONSOLEHELP_COMMAND
								.replace("%command%", e.getValue().getConsoleSyntax())
								.replace("%description%", e.getValue().getDescription()), false);
					}
				}
			}
		}
		return true;
	}
}
