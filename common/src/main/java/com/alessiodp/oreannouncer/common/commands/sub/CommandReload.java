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
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

public class CommandReload extends OASubCommand {
	
	public CommandReload(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RELOAD,
				OreAnnouncerPermission.ADMIN_RELOAD,
				ConfigMain.COMMANDS_SUB_RELOAD,
				true
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_CMD_DESCRIPTIONS_RELOAD;
		help = Messages.HELP_CMD_RELOAD;
	}
	
	@Override
	public String getRunCommand() {
		return baseSyntax();
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			// If the sender is a player
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (player != null && !sender.hasPermission(permission)) {
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
		
		plugin.reloadConfiguration();
		
		sendMessage(commandData.getSender(), player, Messages.OREANNOUNCER_COMMON_CONFIGRELOAD);
	}
}
