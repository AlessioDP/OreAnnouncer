package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Getter;

public class CommandReload extends ADPSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandReload(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RELOAD,
				OreAnnouncerPermission.ADMIN_RELOAD.toString(),
				ConfigMain.COMMANDS_CMD_RELOAD,
				true
		);
		
		syntax = baseSyntax();
		
		runCommand = baseSyntax();
		
		description = Messages.HELP_CMD_DESCRIPTIONS_RELOAD;
		help = Messages.HELP_CMD_RELOAD;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			// If the sender is a player
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (player != null && !sender.hasPermission(OreAnnouncerPermission.ADMIN_RELOAD.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.ADMIN_RELOAD);
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
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_RELOAD
					.replace("{player}", player.getName()), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_RELOAD_CONSOLE, true);
		
		plugin.reloadConfiguration();
		
		if (player != null) {
			player.sendMessage(Messages.OREANNOUNCER_COMMON_CONFIGRELOAD);
			
			plugin.getLoggerManager().log(OAConstants.DEBUG_CMD_RELOADED
					.replace("{player}", player.getName()), true);
		} else {
			plugin.getLoggerManager().log(OAConstants.DEBUG_CMD_RELOADED_CONSOLE, true);
		}
	}
}
