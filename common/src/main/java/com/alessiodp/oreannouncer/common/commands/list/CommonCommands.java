package com.alessiodp.oreannouncer.common.commands.list;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.Getter;

public enum CommonCommands implements ADPCommand {
	OA,
	
	ALERTS,
	HELP,
	RELOAD,
	STATS,
	TOP,
	VERSION;
	
	@Getter private String command;
	@Getter private String help;
	@Getter private String permission;
	
	CommonCommands() {
		command = "";
		help = "";
		permission = "";
	}
	
	public static void setup() {
		CommonCommands.OA.command = ConfigMain.COMMANDS_CMD_OA;
		
		CommonCommands.ALERTS.command = ConfigMain.COMMANDS_CMD_ALERTS;
		CommonCommands.ALERTS.help = Messages.HELP_CMD_ALERTS;
		CommonCommands.ALERTS.permission = OreAnnouncerPermission.USER_ALERTS_TOGGLE.toString();
		CommonCommands.HELP.command = ConfigMain.COMMANDS_CMD_HELP;
		CommonCommands.HELP.help = Messages.HELP_CMD_HELP;
		CommonCommands.HELP.permission = OreAnnouncerPermission.USER_HELP.toString();
		CommonCommands.RELOAD.command = ConfigMain.COMMANDS_CMD_RELOAD;
		CommonCommands.RELOAD.help = Messages.HELP_CMD_RELOAD;
		CommonCommands.RELOAD.permission = OreAnnouncerPermission.ADMIN_RELOAD.toString();
		CommonCommands.STATS.command = ConfigMain.COMMANDS_CMD_STATS;
		CommonCommands.STATS.help = Messages.HELP_CMD_STATS;
		CommonCommands.STATS.permission = OreAnnouncerPermission.USER_STATS.toString();
		CommonCommands.TOP.command = ConfigMain.COMMANDS_CMD_TOP;
		CommonCommands.TOP.help = Messages.HELP_CMD_TOP;
		CommonCommands.TOP.permission = OreAnnouncerPermission.USER_TOP.toString();
		CommonCommands.VERSION.command = ConfigMain.COMMANDS_CMD_VERSION;
		CommonCommands.VERSION.help = Messages.HELP_CMD_VERSION;
		CommonCommands.VERSION.permission = OreAnnouncerPermission.ADMIN_VERSION.toString();
	}
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
