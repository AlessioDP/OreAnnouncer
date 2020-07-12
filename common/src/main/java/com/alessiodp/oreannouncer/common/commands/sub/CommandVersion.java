package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

public class CommandVersion extends ADPSubCommand {
	
	public CommandVersion(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.VERSION,
				OreAnnouncerPermission.ADMIN_VERSION,
				ConfigMain.COMMANDS_CMD_VERSION,
				true
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_CMD_DESCRIPTIONS_VERSION;
		help = Messages.HELP_CMD_VERSION;
	}
	
	@Override
	public String getRunCommand() {
		return baseSyntax();
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				player.sendNoPermission(permission);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null) {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_VERSION
					.replace("{player}", player.getName()), true);
		} else {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_VERSION_CONSOLE, true);
		}
		
		// Command starts
		String version = plugin.getVersion();
		String newVersion = plugin.getAdpUpdater().getFoundVersion().isEmpty() ? version : plugin.getAdpUpdater().getFoundVersion();
		String message = version.equals(newVersion) ? Messages.CMD_VERSION_UPDATED : Messages.CMD_VERSION_OUTDATED;
		
		if (player != null) {
			player.sendMessage(message
					.replace("%version%", version)
					.replace("%newversion%", newVersion)
					.replace("%platform%", plugin.getPlatform()));
		} else {
			plugin.logConsole(Color.translateAndStripColor(message)
					.replace("%version%", version)
					.replace("%newversion%", newVersion)
					.replace("%platform%", plugin.getPlatform()), false);
		}
	}
}
