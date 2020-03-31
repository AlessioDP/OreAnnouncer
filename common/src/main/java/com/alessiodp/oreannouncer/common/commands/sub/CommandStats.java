package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandStats extends ADPSubCommand {
	
	public CommandStats(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.STATS,
				OreAnnouncerPermission.USER_STATS.toString(),
				ConfigMain.COMMANDS_CMD_STATS,
				true
		);
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.OREANNOUNCER_SYNTAX_PLAYER
		);
		
		runCommand = baseSyntax() + " ";
		
		description = Messages.HELP_CMD_DESCRIPTIONS_STATS;
		help = Messages.HELP_CMD_STATS;
	}
	
	@Override
	public String getConsoleSyntax() {
		return baseSyntax();
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(OreAnnouncerPermission.USER_STATS.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.USER_ALERTS_TOGGLE);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		commandData.addPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null)
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_STATS
				.replace("{player}", player.getName())
				.replace("{victim}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_STATS_CONSOLE
							.replace("{victim}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		
		// Command handling
		OAPlayerImpl targetPlayer = null;
		if (commandData.getArgs().length > 1
				&& commandData.havePermission(OreAnnouncerPermission.ADMIN_STATS_OTHER)) {
			Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(commandData.getArgs()[1]);
			if (targetPlayersUuid.size() > 0) {
				UUID targetPlayerUuid = targetPlayersUuid.iterator().next();
				targetPlayer = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetPlayerUuid);
			} else {
				// Not found
				sendMessage(player, Messages.CMD_STATS_PLAYERNOTFOUND
						.replace("%player%", commandData.getArgs()[1]));
				return;
			}
		}
		
		if (targetPlayer == null) {
			if (player == null) {
				plugin.logConsole(plugin.getColorUtils().removeColors(Messages.CMD_STATS_PLAYERNOTFOUND
						.replace("%player%", "Player")), false);
				return;
			} else
				targetPlayer = player;
		}
		
		// Command starts
		List<String> blacklist = new ArrayList<>();
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS.forEach((str) -> blacklist.add(str.toUpperCase()));
		
		sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_STATS_HEADER, targetPlayer));
		
		if (targetPlayer.getDataBlocks().size() > 0) {
			HashMap<OABlockImpl, Integer> map = new HashMap<>();
			targetPlayer.getDataBlocks().forEach((name, playerDataBlock) -> {
				if (!blacklist.contains(playerDataBlock.getMaterialName()) && playerDataBlock.getDestroyCount() > 0) {
					OABlockImpl block = Blocks.LIST.get(playerDataBlock.getMaterialName());
					if (block != null && block.isEnabled()) {
						map.put(block, playerDataBlock.getDestroyCount());
					}
				}
			});
			
			Comparator<Map.Entry<OABlockImpl, Integer>> sorter = Map.Entry.comparingByValue(Comparator.reverseOrder());
			if (ConfigMain.STATS_ORDER_BY.equalsIgnoreCase("priority")) {
				sorter = Comparator.comparingInt(b -> b.getKey().getPriority());
				sorter = sorter.reversed();
			}
			
			LinkedHashMap<OABlockImpl, Integer> result = map.entrySet().stream()
					.sorted(sorter)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			
			result.forEach((block, number) -> {
				sendMessage(player, Messages.CMD_STATS_FORMATPLAYER
						.replace("%block_color%", block.getDisplayColor() != null ? plugin.getColorUtils().convertColorByName(block.getDisplayColor()) : "")
						.replace("%block%", block.getDisplayName() != null ? block.getDisplayName() : block.getSingularName())
						.replace("%value%", Integer.toString(number)));
			});
		} else {
			sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_STATS_NOTHING, targetPlayer));
		}
		
		sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_STATS_FOOTER, targetPlayer));
	}
	
	private void sendMessage(OAPlayerImpl player, String message) {
		if (player != null)
			player.sendMessage(message);
		else
			plugin.logConsole(plugin.getColorUtils().removeColors(message), false);
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
	}
}
