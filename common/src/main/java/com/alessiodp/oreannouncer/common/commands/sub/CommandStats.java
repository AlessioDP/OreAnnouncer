package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandStats extends ADPSubCommand {
	private final String syntaxConsole;
	private final String syntaxOthers;
	private final String syntaxBase;
	private final String syntaxOthersBase;
	
	public CommandStats(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.STATS,
				OreAnnouncerPermission.USER_STATS,
				ConfigMain.COMMANDS_CMD_STATS,
				true
		);
		
		syntaxBase = baseSyntax();
		
		syntaxConsole = String.format("%s <%s>",
				baseSyntax(),
				Messages.OREANNOUNCER_SYNTAX_PLAYER
		);
		
		syntaxOthersBase = String.format("%s [%s]",
				baseSyntax(),
				Messages.OREANNOUNCER_SYNTAX_PLAYER
		);
		
		if (CommandUsage.getCurrentUsage() == CommandUsage.FULL) {
			syntax = String.format("%s [%s]",
					baseSyntax(),
					Messages.OREANNOUNCER_SYNTAX_TYPE
			);
			
			syntaxOthers = String.format("%s [%s] [%s]",
					baseSyntax(),
					Messages.OREANNOUNCER_SYNTAX_TYPE,
					Messages.OREANNOUNCER_SYNTAX_PLAYER
			);
		} else {
			syntax = syntaxBase;
			
			syntaxOthers = syntaxOthersBase;
		}
		
		description = Messages.HELP_CMD_DESCRIPTIONS_STATS;
		help = Messages.HELP_CMD_STATS;
		
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER))
			return user.hasPermission(OreAnnouncerPermission.USER_TOP) ? syntaxOthers: syntaxOthersBase;
		return user.hasPermission(OreAnnouncerPermission.USER_TOP) ? syntax : syntaxBase;
	}
	
	@Override
	public String getConsoleSyntax() {
		return syntaxConsole;
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
		commandData.addPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER);
		commandData.addPermission(OreAnnouncerPermission.USER_STATS);
		commandData.addPermission(OreAnnouncerPermission.USER_STATS_DESTROY);
		commandData.addPermission(OreAnnouncerPermission.USER_STATS_FOUND);
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
		OADatabaseManager.ValueType commandType = null;
		if (commandData.getArgs().length > 1) {
			CommandUsage commandUsage = CommandUsage.getCurrentUsage();
			String[] args = Arrays.copyOfRange(commandData.getArgs(), 1, commandData.getArgs().length);
			if (commandUsage == CommandUsage.FULL) {
				// Full
				if (args.length > 1) {
					commandType = OADatabaseManager.ValueType.parse(args[0]);
					
					if (commandType == null) {
						sendMessage(player, Messages.CMD_STATS_INVALID_TYPE);
						return;
					}
					
					targetPlayer = playerLookup(args[1]);
					if (targetPlayer == null) {
						sendMessage(player, Messages.CMD_STATS_PLAYERNOTFOUND
								.replace("%player%", args[1]));
						return;
					}
				} else {
					commandType = OADatabaseManager.ValueType.parse(args[0]);
					
					if (commandType == null) {
						if (commandData.havePermission(OreAnnouncerPermission.ADMIN_STATS_OTHER)) {
							targetPlayer = playerLookup(args[0]);
							if (targetPlayer == null) {
								sendMessage(player, Messages.CMD_STATS_PLAYERNOTFOUND
										.replace("%player%", args[0]));
								return;
							}
						} else {
							sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", getSyntaxForUser(commandData.getSender())));
							return;
						}
					}
				}
			} else {
				// Base
				targetPlayer = playerLookup(args[0]);
				if (targetPlayer == null) {
					sendMessage(player, Messages.CMD_STATS_PLAYERNOTFOUND
							.replace("%player%", args[0]));
					return;
				}
			}
		}
		
		if (commandType != null) {
			if (!commandData.havePermission(OreAnnouncerPermission.USER_STATS) &&
					(
							(commandType == OADatabaseManager.ValueType.DESTROY && !commandData.havePermission(OreAnnouncerPermission.USER_STATS_DESTROY))
									|| (commandType == OADatabaseManager.ValueType.FOUND && !commandData.havePermission(OreAnnouncerPermission.USER_STATS_FOUND))
					)) {
				sendMessage(player, Messages.CMD_STATS_INVALID_TYPE);
				return;
			}
		} else {
			if (commandData.havePermission(OreAnnouncerPermission.USER_TOP))
				commandType = OADatabaseManager.ValueType.getType(ConfigMain.STATS_VALUES);
			else if (commandData.havePermission(OreAnnouncerPermission.USER_TOP_DESTROY))
				commandType = OADatabaseManager.ValueType.DESTROY;
			else if (commandData.havePermission(OreAnnouncerPermission.USER_TOP_FOUND))
				commandType = OADatabaseManager.ValueType.FOUND;
			
			
			if (commandType == null) commandType = OADatabaseManager.ValueType.DESTROY;
		}
		
		if (targetPlayer == null) {
			if (player == null) {
				sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", getSyntaxForUser(commandData.getSender())));
				return;
			} else
				targetPlayer = player;
		}
		
		// Command starts
		LinkedHashMap<OABlockImpl, Integer> blocks = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getStatsPlayer(commandType, targetPlayer.getPlayerUUID());
		
		sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_STATS_HEADER, targetPlayer));
		
		if (blocks.size() > 0) {
			Comparator<Map.Entry<OABlockImpl, Integer>> sorter = Map.Entry.comparingByValue(Comparator.reverseOrder());
			if (ConfigMain.STATS_ORDER_BY.equalsIgnoreCase("priority")) {
				sorter = Comparator.comparingInt(b -> b.getKey().getPriority());
				sorter = sorter.reversed();
			}
			
			LinkedHashMap<OABlockImpl, Integer> result = blocks.entrySet().stream()
					.sorted(sorter)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			
			result.forEach((block, number) -> {
				sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertBlockPlaceholders(Messages.CMD_STATS_FORMATPLAYER
						.replace("%value%", Integer.toString(number)), block));
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
			plugin.logConsole(Color.translateAndStripColor(message), false);
	}
	
	private OAPlayerImpl playerLookup(String playerName) {
		OAPlayerImpl ret = null;
		User targetUser = plugin.getPlayerByName(playerName);
		if (targetUser != null) {
			ret = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetUser.getUUID());
		} else {
			Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
			if (targetPlayersUuid.size() > 0) {
				ret = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetPlayersUuid.iterator().next());
			}
		}
		return ret;
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
	}
	
	private enum CommandUsage {
		FULL, BASE;
		
		private static CommandUsage getCurrentUsage() {
			if (ConfigMain.STATS_CHANGE_VALUES) {
				return FULL;
			}
			return BASE;
		}
	}
}
