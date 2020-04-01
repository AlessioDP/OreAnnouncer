package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.external.LLAPIHandler;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandLog extends ADPSubCommand {
	private final String syntaxPlayer;
	private final String syntaxBlock;
	
	public CommandLog(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.LOG,
				OreAnnouncerPermission.ADMIN_LOG.toString(),
				ConfigMain.COMMANDS_CMD_LOG,
				true
		);
		
		syntax = String.format("%s <%s/%s> ...",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_BLOCK,
				ConfigMain.COMMANDS_SUB_PLAYER
		);
		syntaxPlayer = String.format("%s %s <%s> [%s] [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_PLAYER,
				Messages.OREANNOUNCER_SYNTAX_PLAYER,
				Messages.OREANNOUNCER_SYNTAX_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_PAGE
		);
		syntaxBlock = String.format("%s %s [%s] [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_BLOCK,
				Messages.OREANNOUNCER_SYNTAX_PAGE
		);
		
		runCommand = baseSyntax() + " ";
		
		description = Messages.HELP_CMD_DESCRIPTIONS_LOG;
		help = Messages.HELP_CMD_LOG;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = null;
		if (sender.isPlayer()) {
			// If the sender is a player
			player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (player != null && !sender.hasPermission(OreAnnouncerPermission.ADMIN_LOG.toString())) {
				player.sendNoPermission(OreAnnouncerPermission.ADMIN_LOG);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		
		if (commandData.getArgs().length < 2) {
			sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null)
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_LOG
					.replace("{player}", player.getName()), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_LOG_CONSOLE, true);
		
		// Command handling
		int selectedPage = 1;
		String playerName = "";
		OAPlayerImpl targetPlayer = null;
		OABlockImpl block = null;
		boolean isGeneralCommand;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_BLOCK)) {
			isGeneralCommand = true;
			
			if (commandData.getArgs().length > 2) {
				if (commandData.getArgs().length > 3) {
					// oa block <BLOCK> <page>
					OABlockImpl b = Blocks.LIST.get(commandData.getArgs()[2].toUpperCase());
					if (b != null && b.isEnabled()) {
						block = b;
					} else {
						sendMessage(player, Messages.CMD_LOG_INVALID_BLOCK);
						return;
					}
					
					// oa block <block> <PAGE>
					try {
						selectedPage = Integer.parseInt(commandData.getArgs()[3]);
					} catch (NumberFormatException ex) {
						sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
								.replace("%syntax%", syntaxBlock));
						return;
					}
				} else {
					try {
						// oa block <block/PAGE>
						selectedPage = Integer.parseInt(commandData.getArgs()[2]);
					} catch (NumberFormatException ex) {
						// oa block <BLOCK/page>
						OABlockImpl b = Blocks.LIST.get(commandData.getArgs()[2].toUpperCase());
						if (b != null && b.isEnabled()) {
							block = b;
						} else {
							sendMessage(player, Messages.CMD_LOG_INVALID_BLOCK);
							return;
						}
					}
				}
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_PLAYER)) {
			isGeneralCommand = false;
			
			if (commandData.getArgs().length > 2) {
				// oa player <PLAYER> ...
				playerName = commandData.getArgs()[2];
				Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
				if (targetPlayersUuid.size() > 0) {
					UUID targetPlayerUuid = targetPlayersUuid.iterator().next();
					targetPlayer = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(targetPlayerUuid);
				} else {
					// Not found
					sendMessage(player, Messages.CMD_LOG_PLAYERNOTFOUND
							.replace("%player%", playerName));
					return;
				}
				
				if (commandData.getArgs().length > 3) {
					if (commandData.getArgs().length > 4) {
						// oa player <player> <BLOCK> <page>
						OABlockImpl b = Blocks.LIST.get(commandData.getArgs()[3].toUpperCase());
						if (b != null && b.isEnabled()) {
							block = b;
						} else {
							sendMessage(player, Messages.CMD_LOG_INVALID_BLOCK);
							return;
						}
						
						// oa player <player> <block> <PAGE>
						try {
							selectedPage = Integer.parseInt(commandData.getArgs()[4]);
						} catch (NumberFormatException ex) {
							sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", syntaxPlayer));
							return;
						}
					} else {
						try {
							// oa player <player> <block/PAGE>
							selectedPage = Integer.parseInt(commandData.getArgs()[3]);
						} catch (NumberFormatException ex) {
							// oa player <player> <BLOCK/page>
							OABlockImpl b = Blocks.LIST.get(commandData.getArgs()[3].toUpperCase());
							if (b != null && b.isEnabled()) {
								block = b;
							} else {
								sendMessage(player, Messages.CMD_LOG_INVALID_BLOCK);
								return;
							}
						}
					}
				}
			} else {
				sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
						.replace("%syntax%", syntaxPlayer));
				return;
			}
		} else {
			sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		boolean empty = true;
		int numberBlocks = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getLogBlocksNumber(targetPlayer, block);
		int limit = Math.max(1, ConfigMain.STATS_ADVANCED_COUNT_LOG_NUMBLOCKS);
		int maxPages;
		if (numberBlocks == 0)
			maxPages = 1;
		else if (numberBlocks % ConfigMain.STATS_ADVANCED_COUNT_LOG_NUMBLOCKS == 0)
			maxPages = numberBlocks / ConfigMain.STATS_ADVANCED_COUNT_LOG_NUMBLOCKS;
		else
			maxPages = (numberBlocks / ConfigMain.STATS_ADVANCED_COUNT_LOG_NUMBLOCKS) + 1;
		
		selectedPage = Math.max(1, selectedPage);
		if (selectedPage > maxPages)
			selectedPage = maxPages;
		
		int offset = selectedPage > 1 ? limit * (selectedPage - 1) : 0;
		LinkedList<BlockFound> blocks = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getLogBlocks(targetPlayer, block, limit, offset);
		
		if (isGeneralCommand) {
			if (block != null) {
				sendMessage(player, Messages.CMD_LOG_HEADER_BLOCK
						.replace("%block%", block.getDisplayName())
						.replace("%page%", Integer.toString(selectedPage))
						.replace("%maxpages%", Integer.toString(maxPages))
						.replace("%total%", Integer.toString(numberBlocks)));
			} else {
				sendMessage(player, Messages.CMD_LOG_HEADER_BLOCK_GENERAL
						.replace("%page%", Integer.toString(selectedPage))
						.replace("%maxpages%", Integer.toString(maxPages))
						.replace("%total%", Integer.toString(numberBlocks)));
			}
		} else {
			sendMessage(player, Messages.CMD_LOG_HEADER_PLAYER
					.replace("%player%", targetPlayer != null ? targetPlayer.getName() : playerName)
					.replace("%page%", Integer.toString(selectedPage))
					.replace("%maxpages%", Integer.toString(maxPages))
					.replace("%total%", Integer.toString(numberBlocks)));
		}
		
		if (blocks.size() > 0) {
			for (BlockFound bf : blocks) {
				OABlockImpl b = Blocks.LIST.get(bf.getMaterialName());
				if (b != null && b.isEnabled()) {
					empty = false;
					sendMessage(player, (isGeneralCommand ? Messages.CMD_LOG_FORMAT_GENERAL_BLOCK : Messages.CMD_LOG_FORMAT_PLAYER_BLOCK)
							.replace("%number%", Integer.toString(bf.getFound()))
							.replace("%block%", b.getDisplayName() != null ? b.getDisplayName() : b.getSingularName())
							.replace("%block_color%", b.getDisplayColor() != null ? plugin.getColorUtils().convertColorByName(b.getDisplayColor()) : "")
							.replace("%date_elapsed%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatElapsed(bf.getTimestamp()))
							.replace("%date%", ((OreAnnouncerPlugin) plugin).getMessageUtils().formatDate(bf.getTimestamp())));
				}
			}
		}
		
		if (empty) {
			sendMessage(player, Messages.CMD_LOG_NOTHING);
		}
		
		if (isGeneralCommand) {
			if (block != null) {
				sendMessage(player, Messages.CMD_LOG_FOOTER_BLOCK
						.replace("%block%", block.getDisplayName())
						.replace("%page%", Integer.toString(selectedPage))
						.replace("%maxpages%", Integer.toString(maxPages))
						.replace("%total%", Integer.toString(numberBlocks)));
			} else {
				sendMessage(player, Messages.CMD_LOG_FOOTER_BLOCK_GENERAL
						.replace("%page%", Integer.toString(selectedPage))
						.replace("%maxpages%", Integer.toString(maxPages))
						.replace("%total%", Integer.toString(numberBlocks)));
			}
		} else {
			sendMessage(player, Messages.CMD_LOG_FOOTER_PLAYER
					.replace("%page%", Integer.toString(selectedPage))
					.replace("%maxpages%", Integer.toString(maxPages))
					.replace("%total%", Integer.toString(numberBlocks)));
		}
	}
	
	private void sendMessage(OAPlayerImpl player, String message) {
		if (player != null)
			player.sendMessage(message);
		else
			plugin.logConsole(plugin.getColorUtils().removeColors(plugin.getJsonHandler().removeJson(message)), false);
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(OreAnnouncerPermission.ADMIN_LOG.toString())) {
			if (args.length == 2) {
				ret.add(ConfigMain.COMMANDS_SUB_BLOCK);
				ret.add(ConfigMain.COMMANDS_SUB_PLAYER);
				
				if (!args[1].isEmpty()) {
					ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[1]);
				}
			} else if (args.length > 2) {
				if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_BLOCK)) {
					if (args.length == 3) {
						// Block
						for (OABlockImpl block : Blocks.LIST.values()) {
							if (block.isEnabled())
								ret.add(block.getMaterialName());
						}
						
						if (!args[2].isEmpty()) {
							ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[2]);
						}
					}
				} else {
					// Player
					if (args.length == 3) {
						return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 2);
					} else if (args.length == 4) {
						for (OABlockImpl block : Blocks.LIST.values()) {
							if (block.isEnabled())
								ret.add(block.getMaterialName());
						}
						
						if (!args[3].isEmpty()) {
							ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[3]);
						}
					}
				}
			}
		}
		return ret;
	}
}
