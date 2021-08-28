package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OASubCommand;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CommandTop extends OASubCommand {
	private final String syntaxBase;
	
	public CommandTop(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.TOP,
				OreAnnouncerPermission.USER_TOP,
				ConfigMain.COMMANDS_SUB_TOP,
				true
		);
		
		syntaxBase = String.format("%s [%s]",
				baseSyntax(),
				Messages.OREANNOUNCER_SYNTAX_PAGE
		);
		
		switch (CommandUsage.getCurrentUsage()) {
			case FULL:
				syntax = String.format("%s [%s] [%s] [%s]",
						baseSyntax(),
						Messages.OREANNOUNCER_SYNTAX_ORDER,
						Messages.OREANNOUNCER_SYNTAX_BLOCK,
						Messages.OREANNOUNCER_SYNTAX_PAGE
				);
				break;
			case ONLY_BLOCK:
				syntax = String.format("%s [%s] [%s]",
						baseSyntax(),
						Messages.OREANNOUNCER_SYNTAX_BLOCK,
						Messages.OREANNOUNCER_SYNTAX_PAGE
				);
				break;
			case ONLY_ORDER:
				syntax = String.format("%s [%s] [%s]",
						baseSyntax(),
						Messages.OREANNOUNCER_SYNTAX_ORDER,
						Messages.OREANNOUNCER_SYNTAX_PAGE
				);
				break;
			case BASE:
			default:
				syntax = syntaxBase;
				break;
		}
		
		description = Messages.HELP_CMD_DESCRIPTIONS_TOP;
		help = Messages.HELP_CMD_TOP;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (!user.hasPermission(OreAnnouncerPermission.USER_TOP))
			return syntaxBase;
		return syntax;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)
					&& !sender.hasPermission(OreAnnouncerPermission.USER_TOP_DESTROY)
					&& !sender.hasPermission(OreAnnouncerPermission.USER_TOP_FOUND)) {
				player.sendNoPermission(permission);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
			commandData.addPermission(OreAnnouncerPermission.USER_TOP);
			commandData.addPermission(OreAnnouncerPermission.USER_TOP_DESTROY);
			commandData.addPermission(OreAnnouncerPermission.USER_TOP_FOUND);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		// Command handling
		List<String> blacklist = new ArrayList<>();
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP.forEach((str) -> blacklist.add(CommonUtils.toUpperCase(str)));
		
		int selectedPage = 1;
		OADatabaseManager.ValueType orderBy = null;
		OABlockImpl block = null;
		if (commandData.getArgs().length > 1) {
			CommandUsage commandUsage = CommandUsage.getCurrentUsage();
			String[] args = Arrays.copyOfRange(commandData.getArgs(), 1, commandData.getArgs().length);
			if (args.length == 3) {
				// Full command
				if (commandUsage == CommandUsage.FULL) {
					try {
						selectedPage = Integer.parseInt(args[2]);
					} catch(NumberFormatException ex) {
						sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
								.replace("%syntax%", syntax));
						return;
					}
					
					block = Blocks.searchBlock(CommonUtils.toUpperCase(args[1]));
					if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
						sendMessage(commandData.getSender(), player, Messages.CMD_TOP_INVALID_BLOCK);
						return;
					}
					
					
					orderBy = OADatabaseManager.ValueType.parse(args[0]);
					if (orderBy == null) {
						sendMessage(sender, player, Messages.CMD_TOP_INVALID_ORDER);
						return;
					}
				} else {
					sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
							.replace("%syntax%", syntax));
					return;
				}
			} else if (args.length == 2) {
				// Fail if command usage is BASE
				if (commandUsage == CommandUsage.BASE) {
					sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
							.replace("%syntax%", syntax));
					return;
				}
				
				// Check if page
				try {
					selectedPage = Integer.parseInt(args[1]);
					
					if (commandUsage == CommandUsage.FULL) {
						// Check for or order or block
						OADatabaseManager.ValueType temp = OADatabaseManager.ValueType.parse(args[0]);
						
						if (temp == null) {
							block = Blocks.searchBlock(CommonUtils.toUpperCase(args[0]));
							if (block == null) {
								sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
										.replace("%syntax%", syntax));
								return;
							}
						} else {
							orderBy = temp;
						}
						
					} else if (commandUsage == CommandUsage.ONLY_BLOCK) {
						// Check for block
						block = Blocks.searchBlock(CommonUtils.toUpperCase(args[0]));
						if (block == null) {
							sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", syntax));
							return;
						}
					} else {
						// Check for order
						orderBy = OADatabaseManager.ValueType.parse(args[0]);
						if (orderBy == null) {
							sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", syntax));
							return;
						}
					}
					
				} catch (NumberFormatException ignored) {
					if (commandUsage == CommandUsage.FULL) {
						// Check for or order or block
						block = Blocks.searchBlock(CommonUtils.toUpperCase(args[1]));
						if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
							sendMessage(sender, player, Messages.CMD_TOP_INVALID_BLOCK);
							return;
						}
						
						
						orderBy = OADatabaseManager.ValueType.parse(args[0]);
						if (orderBy == null) {
							sendMessage(sender, player, Messages.CMD_TOP_INVALID_ORDER);
							return;
						}
					} else {
						sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
								.replace("%syntax%", syntax));
						return;
					}
				}
			} else if (args.length == 1) {
				// Check if page
				try {
					selectedPage = Integer.parseInt(args[0]);
				} catch (NumberFormatException ignored) {
					if (commandUsage == CommandUsage.FULL) {
						// Check for or order or block
						orderBy = OADatabaseManager.ValueType.parse(args[0]);
						if (orderBy == null) {
							block = Blocks.searchBlock(CommonUtils.toUpperCase(args[0]));
							if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
								sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
										.replace("%syntax%", syntax));
								return;
							}
						}
					} else if (commandUsage == CommandUsage.ONLY_BLOCK) {
						block = Blocks.searchBlock(CommonUtils.toUpperCase(args[0]));
						if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
							sendMessage(sender, player, Messages.CMD_TOP_INVALID_BLOCK);
							return;
						}
					} else if (commandUsage == CommandUsage.ONLY_ORDER) {
						orderBy = OADatabaseManager.ValueType.parse(args[0]);
						if (orderBy == null) {
							sendMessage(sender, player, Messages.CMD_TOP_INVALID_ORDER);
							return;
						}
					} else {
						sendMessage(sender, player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
								.replace("%syntax%", syntax));
						return;
					}
				}
			}
		}
		
		if (orderBy != null) {
			if (!commandData.havePermission(OreAnnouncerPermission.USER_TOP) &&
					(
							(orderBy == OADatabaseManager.ValueType.DESTROY && !commandData.havePermission(OreAnnouncerPermission.USER_TOP_DESTROY))
							|| (orderBy == OADatabaseManager.ValueType.FOUND && !commandData.havePermission(OreAnnouncerPermission.USER_TOP_FOUND))
					)) {
				sendMessage(sender, player, Messages.CMD_TOP_INVALID_ORDER);
				return;
			}
		} else {
			if (commandData.havePermission(OreAnnouncerPermission.USER_TOP))
				orderBy = OADatabaseManager.ValueType.getType(ConfigMain.STATS_TOP_ORDER_BY);
			else if (commandData.havePermission(OreAnnouncerPermission.USER_TOP_DESTROY))
				orderBy = OADatabaseManager.ValueType.DESTROY;
			else if (commandData.havePermission(OreAnnouncerPermission.USER_TOP_FOUND))
				orderBy = OADatabaseManager.ValueType.FOUND;
			
			
			if (orderBy == null) orderBy = OADatabaseManager.ValueType.DESTROY;
		}
		
		// Command starts
		int numberPlayers = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getTopPlayersNumber(orderBy, block);
		int limit = Math.max(1, ConfigMain.STATS_TOP_NUMPLAYERS);
		int maxPages;
		if (numberPlayers == 0)
			maxPages = 1;
		else if (numberPlayers % ConfigMain.STATS_TOP_NUMPLAYERS == 0)
			maxPages = numberPlayers / ConfigMain.STATS_TOP_NUMPLAYERS;
		else
			maxPages = (numberPlayers / ConfigMain.STATS_TOP_NUMPLAYERS) + 1;
		
		if (selectedPage > maxPages)
			selectedPage = maxPages;
		
		int offset = selectedPage > 1 ? limit * (selectedPage - 1) : 0;
		LinkedHashMap<UUID, Integer> players = ((OreAnnouncerPlugin) plugin).getDatabaseManager().getTopPlayers(orderBy, block, limit, offset);
		
		sendMessage(sender, player, Messages.CMD_TOP_HEADER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages%", Integer.toString(maxPages)));
		
		if (players.size() > 0) {
			players.forEach((id, number) -> {
				OAPlayerImpl p = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(id);
				sendMessage(sender, player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_TOP_FORMATPLAYER
						.replace("%value%", Integer.toString(number)), p));
			});
		} else {
			sendMessage(sender, player, Messages.CMD_TOP_NOONE);
		}
		
		sendMessage(sender, player, Messages.CMD_TOP_FOOTER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages", Integer.toString(maxPages)));
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)) {
			CommandUsage commandUsage = CommandUsage.getCurrentUsage();
			if (args.length == 2) {
				switch (commandUsage) {
					case FULL:
					case ONLY_ORDER:
						for (OADatabaseManager.ValueType ord : OADatabaseManager.ValueType.values())
							ret.add(CommonUtils.toLowerCase(ord.name()));
						break;
					case ONLY_BLOCK:
						for (OABlockImpl block : Blocks.LIST.values()) {
							if (block.isEnabled())
								ret.add(block.getMaterialName());
						}
						break;
					case BASE:
					default:
						// Nothing to do
						break;
				}
			} else if (args.length == 3) {
				switch (commandUsage) {
					case FULL:
						for (OABlockImpl block : Blocks.LIST.values()) {
							if (block.isEnabled())
								ret.add(block.getMaterialName());
						}
						break;
					case ONLY_ORDER:
					case ONLY_BLOCK:
					case BASE:
					default:
						// Nothing to do
						break;
				}
			}
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[args.length - 1]);
	}
	
	private enum CommandUsage {
		FULL, ONLY_BLOCK, ONLY_ORDER, BASE;
		
		private static CommandUsage getCurrentUsage() {
			if (ConfigMain.STATS_TOP_CHANGE_ORDER && ConfigMain.STATS_TOP_CHANGE_BLOCK) {
				return FULL;
			} else if (ConfigMain.STATS_TOP_CHANGE_BLOCK) {
				return ONLY_BLOCK;
			} else if (ConfigMain.STATS_TOP_CHANGE_ORDER) {
				return ONLY_ORDER;
			}
			return BASE;
		}
	}
}
