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
import lombok.NonNull;

import java.util.List;

public class CommandAlerts extends ADPSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandAlerts(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE.toString())) {
			player.sendNoPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE);
			return false;
		}
		
		((OACommandData) commandData).setPlayer(player);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_ALERTS
				.replace("{player}", player.getName())
				.replace("{toggle}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		
		// Command handling
		player.getLock().lock(); // Lock
		Boolean alerts = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(player.haveAlertsOn(), commandData.getArgs());
		if (alerts == null) {
			player.sendMessage(Messages.CMD_ALERTS_WRONGCMD);
			return;
		}
		
		// Command starts
		player.setAlertsOn(alerts);
		player.updatePlayer();
		player.getLock().unlock(); // Unlock
		
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
