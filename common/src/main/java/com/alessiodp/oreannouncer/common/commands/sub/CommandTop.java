package com.alessiodp.oreannouncer.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
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
import java.util.Locale;
import java.util.UUID;

public class CommandTop extends ADPSubCommand {
	
	public CommandTop(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.TOP,
				OreAnnouncerPermission.USER_TOP.toString(),
				ConfigMain.COMMANDS_CMD_TOP,
				true
		);
		
		if (ConfigMain.STATS_TOP_CHANGE_ORDER) {
			syntax = String.format("%s [%s] [%s] [%s]",
					baseSyntax(),
					Messages.OREANNOUNCER_SYNTAX_ORDER,
					Messages.OREANNOUNCER_SYNTAX_BLOCK,
					Messages.OREANNOUNCER_SYNTAX_PAGE
			);
		} else {
			syntax = String.format("%s [%s] [%s]",
					baseSyntax(),
					Messages.OREANNOUNCER_SYNTAX_BLOCK,
					Messages.OREANNOUNCER_SYNTAX_PAGE
			);
		}
		
		description = Messages.HELP_CMD_DESCRIPTIONS_TOP;
		help = Messages.HELP_CMD_TOP;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				player.sendNoPermission(OreAnnouncerPermission.USER_TOP);
				return false;
			}
			
			((OACommandData) commandData).setPlayer(player);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		OAPlayerImpl player = ((OACommandData) commandData).getPlayer();
		
		if (player != null)
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_TOP
					.replace("{player}", player.getName()), true);
		else
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_CMD_TOP_CONSOLE, true);
		
		// Command handling
		List<String> blacklist = new ArrayList<>();
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP.forEach((str) -> blacklist.add(str.toUpperCase(Locale.ENGLISH)));
		
		int selectedPage = 1;
		OADatabaseManager.TopOrderBy orderBy = OADatabaseManager.TopOrderBy.parse(ConfigMain.STATS_TOP_ORDER_BY);
		if (orderBy == null) orderBy = OADatabaseManager.TopOrderBy.DESTROY;
		OABlockImpl block = null;
		if (commandData.getArgs().length > 1) {
			String[] args = Arrays.copyOfRange(commandData.getArgs(), 1, commandData.getArgs().length);
			
			if (args.length > 2) {
				// All
				try {
					selectedPage = Integer.parseInt(args[2]);
				} catch(NumberFormatException ex) {
					sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
							.replace("%syntax%", syntax));
					return;
				}
				
				block = Blocks.LIST.get(args[1].toUpperCase());
				if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
					sendMessage(player, Messages.CMD_TOP_INVALID_BLOCK);
					return;
				}
				
				
				orderBy = OADatabaseManager.TopOrderBy.parse(args[0]);
				if (orderBy == null) {
					sendMessage(player, Messages.CMD_TOP_INVALID_ORDER);
					return;
				}
				
				
			} else if (args.length > 1) {
				// Check if page
				try {
					selectedPage = Integer.parseInt(args[1]);
					
					// Check for or order or block
					OADatabaseManager.TopOrderBy temp = OADatabaseManager.TopOrderBy.parse(args[0]);
					
					if (temp == null) {
						block = Blocks.LIST.get(args[0].toUpperCase());
						if (block == null) {
							sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", syntax));
							return;
						}
					} else {
						orderBy = temp;
					}
				} catch (NumberFormatException ignored) {
					// Check for both order and block
					block = Blocks.LIST.get(args[1].toUpperCase());
					if (block == null || !block.isEnabled() || blacklist.contains(block.getMaterialName())) {
						sendMessage(player, Messages.CMD_TOP_INVALID_BLOCK);
						return;
					}
					
					
					orderBy = OADatabaseManager.TopOrderBy.parse(args[0]);
					if (orderBy == null) {
						sendMessage(player, Messages.CMD_TOP_INVALID_ORDER);
						return;
					}
				}
			} else {
				// Check if page
				try {
					selectedPage = Integer.parseInt(args[0]);
				} catch (NumberFormatException ignored) {
					// Check if order
					OADatabaseManager.TopOrderBy temp = OADatabaseManager.TopOrderBy.parse(args[0]);
					
					if (temp == null) {
						// Check if block
						block = Blocks.LIST.get(args[0].toUpperCase());
						if (block == null) {
							sendMessage(player, Messages.OREANNOUNCER_SYNTAX_WRONGMESSAGE
									.replace("%syntax%", syntax));
							return;
						}
					} else {
						orderBy = temp;
					}
				}
			}
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
		
		sendMessage(player, Messages.CMD_TOP_HEADER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages%", Integer.toString(maxPages)));
		
		if (players.size() > 0) {
			players.forEach((id, number) -> {
				OAPlayerImpl p = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(id);
				sendMessage(player, ((OreAnnouncerPlugin) plugin).getMessageUtils().convertPlayerPlaceholders(Messages.CMD_TOP_FORMATPLAYER
						.replace("%value%", Integer.toString(number)), p));
			});
		} else {
			sendMessage(player, Messages.CMD_TOP_NOONE);
		}
		
		sendMessage(player, Messages.CMD_TOP_FOOTER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages", Integer.toString(maxPages)));
	}
	
	private void sendMessage(OAPlayerImpl player, String message) {
		if (player != null)
			player.sendMessage(message);
		else
			plugin.logConsole(plugin.getColorUtils().removeColors(message), false);
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)) {
			if (args.length == 2) {
				if (ConfigMain.STATS_TOP_CHANGE_ORDER) {
					for (OADatabaseManager.TopOrderBy ord : OADatabaseManager.TopOrderBy.values())
						ret.add(ord.name().toLowerCase());
				} else {
					for (OABlockImpl block : Blocks.LIST.values()) {
						if (block.isEnabled())
							ret.add(block.getMaterialName());
					}
				}
			} else if (args.length == 3 && ConfigMain.STATS_TOP_CHANGE_ORDER) {
				for (OABlockImpl block : Blocks.LIST.values()) {
					if (block.isEnabled())
						ret.add(block.getMaterialName());
				}
			}
		}
		return ret;
	}
}
