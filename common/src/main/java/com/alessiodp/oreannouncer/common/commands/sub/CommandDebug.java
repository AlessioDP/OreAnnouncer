package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OASubCommand;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CommandDebug extends OASubCommand {
	private final String syntaxAlert;
	private final String syntaxBlock;
	private final String syntaxConfig;
	private final String syntaxPlayer;
	
	public CommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DEBUG,
				OreAnnouncerPermission.ADMIN_DEBUG,
				ConfigMain.COMMANDS_SUB_DEBUG,
				true
		);
		
		syntax = String.format("%s <%s/%s/%s/%s> ...",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_ALERT,
				ConfigMain.COMMANDS_MISC_BLOCK,
				ConfigMain.COMMANDS_MISC_CONFIG,
				ConfigMain.COMMANDS_MISC_PLAYER
		);
		syntaxAlert = String.format("%s %s <%s> <%s> <%s> [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_ALERT,
				Messages.OREANNOUNCER_SYNTAX_PLAYER,
				Messages.OREANNOUNCER_SYNTAX_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_NUMBER,
				Messages.OREANNOUNCER_SYNTAX_TYPE
		);
		syntaxBlock = String.format("%s %s <%s/%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_BLOCK,
				ConfigMain.COMMANDS_MISC_ALL
		);
		syntaxConfig = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_CONFIG
		);
		syntaxPlayer = String.format("%s %s [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_PLAYER,
				Messages.OREANNOUNCER_SYNTAX_PLAYER
		);
		
		
		description = Messages.HELP_CMD_DESCRIPTIONS_DEBUG;
		help = Messages.HELP_CMD_DEBUG;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = null;
		if (sender.isPlayer()) {
			player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (!sender.hasPermission(permission)) {
				player.sendNoPermission(permission);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		// Command handling
		OAPlayerImpl targetPlayer = null;
		OABlockImpl block = null;
		int number = -1;
		BlockManager.AlertType alertType = null;
		CommandType commandType;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_ALERT)) {
			commandType = CommandType.ALERT;
			if (commandData.getArgs().length >= 5 && commandData.getArgs().length <= 6) {
				// oa debug alert <PLAYER> <BLOCK> <NUMBER> [TYPE]
				
				// Player
				targetPlayer = playerLookup(commandData.getArgs()[2]);
				if (targetPlayer == null) {
					sendMessage(sender, player, Messages.CMD_DEBUG_COMMON_INVALID_PLAYER
							.replace("%player%", commandData.getArgs()[2]));
					return;
				}
				
				// Block
				OABlockImpl b = Blocks.searchBlock(CommonUtils.toUpperCase(commandData.getArgs()[3]));
				if (b != null) {
					block = b;
				} else {
					sendMessage(sender, player, Messages.CMD_DEBUG_COMMON_INVALID_BLOCK);
					return;
				}
				
				// Number
				try {
					number = Integer.parseInt(commandData.getArgs()[4]);
				} catch (NumberFormatException ignored) {}
				if (number <= 0) {
					sendMessage(sender, player, Messages.CMD_DEBUG_ALERT_INVALID_NUMBER);
					return;
				}
				
				// Type
				if (commandData.getArgs().length == 6) {
					if (commandData.getArgs()[5].equalsIgnoreCase(Messages.OREANNOUNCER_SYNTAX_NORMAL))
						alertType = BlockManager.AlertType.NORMAL;
					else if (commandData.getArgs()[5].equalsIgnoreCase(Messages.OREANNOUNCER_SYNTAX_ADVANCED))
						alertType = BlockManager.AlertType.COUNT;
					else {
						sendMessage(sender, player, Messages.CMD_DEBUG_ALERT_INVALID_TYPE);
						return;
					}
				} else
					alertType = BlockManager.AlertType.NORMAL;
			} else {
				sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxAlert));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_BLOCK)) {
			commandType = CommandType.BLOCK;
			if (commandData.getArgs().length == 3) {
				// oa debug block <BLOCK/ALL>
				if (!commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_ALL)) {
					OABlockImpl b = Blocks.searchBlock(CommonUtils.toUpperCase(commandData.getArgs()[2]));
					if (b != null) {
						block = b;
					} else {
						sendMessage(sender, player, Messages.CMD_DEBUG_COMMON_INVALID_BLOCK);
						return;
					}
				}
			} else {
				sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxBlock));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_CONFIG)) {
			commandType = CommandType.CONFIG;
			if (commandData.getArgs().length != 2) {
				sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxConfig));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PLAYER)) {
			commandType = CommandType.PLAYER;
			if (commandData.getArgs().length == 2) {
				targetPlayer = player;
			} else if (commandData.getArgs().length == 3) {
				// oa debug player <PLAYER>
				User targetUser = plugin.getPlayerByName(commandData.getArgs()[2]);
				if (targetUser != null) {
					targetPlayer = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetUser.getUUID());
				}
				if (targetPlayer == null) {
					sendMessage(sender, player, Messages.CMD_DEBUG_COMMON_INVALID_PLAYER
							.replace("%player%", commandData.getArgs()[2]));
					return;
				}
			} else {
				sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxPlayer));
				return;
			}
		} else {
			sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		if (commandType == CommandType.ALERT) {
			// Alert
			BlockData blockData = new BlockData(targetPlayer, block, number)
					.setAlertUsers(true)
					.setAlertAdmins(true)
					.setLocation(getPlayerLocation(targetPlayer))
					.setLightLevel(getPlayerLightLevel(targetPlayer));
			
			switch (alertType) {
				case NORMAL:
					((OreAnnouncerPlugin) plugin).getBlockManager().handleAlerts(blockData);
					break;
				case COUNT:
					BlocksFoundResult bfr = new BlocksFoundResult(System.currentTimeMillis() / 1000L, number);
					((OreAnnouncerPlugin) plugin).getBlockManager().handleFoundAlerts(blockData, bfr);
					break;
				default:
					// Nothing
					break;
			}
		} else if (commandType == CommandType.BLOCK) {
			// Block
			sendMessage(sender, player, Messages.CMD_DEBUG_BLOCK_HEADER);
			
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
			sendMessage(sender, player, Messages.CMD_DEBUG_CONFIG_HEADER);
			
			for (String line : Messages.CMD_DEBUG_CONFIG_TEXT) {
				sendMessage(sender, player, line
						.replace("%outdated_config%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(((OAConfigurationManager) plugin.getConfigurationManager()).getConfigMain().isOutdated()))
						.replace("%outdated_messages%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(((OAConfigurationManager) plugin.getConfigurationManager()).getMessages().isOutdated()))
						.replace("%storage%", plugin.getDatabaseManager().getDatabaseType().getFormattedName())
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
				sendMessage(sender, player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_DEBUG_PLAYER_HEADER, targetPlayer));
				
				for (String line : Messages.CMD_DEBUG_PLAYER_TEXT) {
					sendMessage(sender, player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(
							line.replace("%permission_bypass_alerts%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_ALERTS)))
									.replace("%permission_bypass_destroy%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_DESTROY)))
									.replace("%permission_bypass_found%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_FOUND)))
									.replace("%see_alerts_users%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE)))
									.replace("%see_alerts_admins%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE)))
									.replace("%see_alerts_others%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.hasPermission(OreAnnouncerPermission.ADMIN_STATS_OTHER)))
									.replace("%op%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatYesNo(user.isOperator()))
							, targetPlayer));
				}
			} else {
				sendMessage(sender, player, Messages.CMD_DEBUG_COMMON_INVALID_PLAYER
						.replace("%player%", targetPlayer.getName()));
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)) {
			if (args.length == 2) {
				ret.add(ConfigMain.COMMANDS_MISC_ALERT);
				ret.add(ConfigMain.COMMANDS_MISC_BLOCK);
				ret.add(ConfigMain.COMMANDS_MISC_CONFIG);
				ret.add(ConfigMain.COMMANDS_MISC_PLAYER);
			} else if(args.length == 3) {
				if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_ALERT))
					return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 2);
				else if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_BLOCK)) {
					ret.add(ConfigMain.COMMANDS_MISC_ALL);
					for (OABlockImpl block : Blocks.LIST.values()) {
						ret.add(block.getMaterialName());
					}
				}
			} else if(args.length == 4 && args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_ALERT)) {
				for (OABlockImpl block : Blocks.LIST.values()) {
					ret.add(block.getMaterialName());
				}
			}
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[args.length - 1]);
	}
	
	protected ADPLocation getPlayerLocation(OAPlayerImpl player) {
		return new ADPLocation("world", 0 , 0, 0, 0, 0);
	}
	
	protected int getPlayerLightLevel(OAPlayerImpl player) {
		return 15;
	}
	
	private enum CommandType {
		ALERT, BLOCK, CONFIG, PLAYER
	}
}
