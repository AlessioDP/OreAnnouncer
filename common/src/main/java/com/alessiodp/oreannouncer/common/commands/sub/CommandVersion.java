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
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Getter;

public class CommandVersion extends ADPSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandVersion(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(OreAnnouncerPermission.ADMIN_VERSION.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.ADMIN_VERSION);
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
			plugin.logConsole(plugin.getColorUtils().removeColors(message), false);
		}
	}
}
