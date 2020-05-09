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
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class CommandDebug extends ADPSubCommand {
	private final String syntaxBlock;
	private final String syntaxConfig;
	private final String syntaxPlayer;
	
	public CommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.LOG,
				OreAnnouncerPermission.ADMIN_DEBUG.toString(),
				ConfigMain.COMMANDS_CMD_DEBUG,
				false
		);
		
		syntax = String.format("%s <%s/%s/%s> ...",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_BLOCK,
				ConfigMain.COMMANDS_SUB_CONFIG,
				ConfigMain.COMMANDS_SUB_PLAYER
		);
		syntaxBlock = String.format("%s %s <%s/%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_BLOCK,
				ConfigMain.COMMANDS_SUB_ALL
		);
		syntaxConfig = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_CONFIG
		);
		syntaxPlayer = String.format("%s %s [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_PLAYER,
				Messages.OREANNOUNCER_SYNTAX_PLAYER
		);
		
		
		description = Messages.HELP_CMD_DESCRIPTIONS_DEBUG;
		help = Messages.HELP_CMD_DEBUG;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		if (!sender.hasPermission(permission)) {
			player.sendNoPermission(OreAnnouncerPermission.ADMIN_DEBUG);
			return false;
		}
		
		if (commandData.getArgs().length < 2) {
			player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		((OACommandData) commandData).setPlayer(player);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_DEBUG
				.replace("{player}", player.getName()), true);
		
		// Command handling
		String playerName;
		OAPlayerImpl targetPlayer = null;
		OABlockImpl block = null;
		CommandType commandType;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_BLOCK)) {
			commandType = CommandType.BLOCK;
			if (commandData.getArgs().length == 3) {
				// oa debug block <BLOCK/ALL>
				if (!commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ALL)) {
					OABlockImpl b = Blocks.LIST.get(commandData.getArgs()[2].toUpperCase(Locale.ENGLISH));
					if (b != null) {
						block = b;
					} else {
						player.sendMessage(Messages.CMD_DEBUG_BLOCK_INVALID_BLOCK);
						return;
					}
				}
			} else {
				player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxBlock));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_CONFIG)) {
			commandType = CommandType.CONFIG;
			if (commandData.getArgs().length != 2) {
				player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxConfig));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_PLAYER)) {
			commandType = CommandType.PLAYER;
			if (commandData.getArgs().length == 2) {
				targetPlayer = player;
			} else if (commandData.getArgs().length == 3) {
				// oa debug player <PLAYER>
				playerName = commandData.getArgs()[2];
				Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
				if (targetPlayersUuid.size() > 0) {
					UUID targetPlayerUuid = targetPlayersUuid.iterator().next();
					targetPlayer = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetPlayerUuid);
				} else {
					// Not found
					player.sendMessage(Messages.CMD_DEBUG_PLAYER_PLAYER_OFFLINE
							.replace("%player%", playerName));
					return;
				}
			} else {
				player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxPlayer));
				return;
			}
		} else {
			player.sendMessage(Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		if (commandType == CommandType.BLOCK) {
			// Block
			player.sendMessage(Messages.CMD_DEBUG_BLOCK_HEADER);
			
			if (block == null) {
				// All blocks
				for (OABlockImpl b : Blocks.LIST.values()) {
					for (String line : Messages.CMD_DEBUG_BLOCK_TEXT) {
						player.sendMessage(((OreAnnouncerPlugin) plugin).getMessageUtils().convertBlockFormattedPlaceholders(line, b));
					}
				}
			} else {
				// Specific block
				for (String line : Messages.CMD_DEBUG_BLOCK_TEXT) {
					player.sendMessage(((OreAnnouncerPlugin) plugin).getMessageUtils().convertBlockFormattedPlaceholders(line, block));
				}
			}
		} else if (commandType == CommandType.CONFIG) {
			// Config
			player.sendMessage(Messages.CMD_DEBUG_CONFIG_HEADER);
			
			for (String line : Messages.CMD_DEBUG_CONFIG_TEXT) {
				player.sendMessage(line
						.replace("%alerts%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.ALERTS_ENABLE))
						.replace("%coordinates%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.ALERTS_COORDINATES_ENABLE))
						.replace("%bypass_player%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(ConfigMain.BLOCKS_BYPASS_PLAYERBLOCKS))
						.replace("%bypass_silk%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(ConfigMain.BLOCKS_BYPASS_SILKTOUCH))
						.replace("%bypass_counter%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(ConfigMain.BLOCKS_BYPASS_SECURE_COUNTER))
						.replace("%stats%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.STATS_ENABLE))
						.replace("%stats_values%", ConfigMain.STATS_VALUES)
						.replace("%blacklist_log%", String.join(", ", ConfigMain.STATS_BLACKLIST_BLOCKS_LOG))
						.replace("%blacklist_stats%", String.join(", ", ConfigMain.STATS_BLACKLIST_BLOCKS_STATS))
						.replace("%blacklist_top%", String.join(", ", ConfigMain.STATS_BLACKLIST_BLOCKS_TOP))
						.replace("%advanced_count%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.STATS_ADVANCED_COUNT_ENABLE))
						.replace("%log_command%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.STATS_ADVANCED_COUNT_LOG_ENABLE))
						.replace("%top_command%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatOnOff(ConfigMain.STATS_TOP_ENABLE))
				);
			}
		} else {
			// Player
			User user = plugin.getPlayer(targetPlayer.getPlayerUUID());
			if (user != null) {
				player.sendMessage(((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_DEBUG_PLAYER_HEADER, targetPlayer));
				
				for (String line : Messages.CMD_DEBUG_PLAYER_TEXT) {
					player.sendMessage(((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(
							line.replace("%permission_bypass_alerts%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS.toString())))
									.replace("%permission_bypass_destroy%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY.toString())))
									.replace("%permission_bypass_found%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND.toString())))
									.replace("%see_alerts_users%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE.toString())))
									.replace("%see_alerts_admins%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE.toString())))
									.replace("%see_alerts_others%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER.toString())))
									.replace("%op%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.isOperator()))
							, targetPlayer));
				}
			} else {
				player.sendMessage(Messages.CMD_DEBUG_PLAYER_PLAYER_OFFLINE
						.replace("%player%", targetPlayer.getName()));
			}
		}
	}
	
	private enum CommandType {
		BLOCK, CONFIG, PLAYER
	}
}
