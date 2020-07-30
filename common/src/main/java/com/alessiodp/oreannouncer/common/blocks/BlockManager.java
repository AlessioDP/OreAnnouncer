package com.alessiodp.oreannouncer.common.blocks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.DurationUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.Alert;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import com.alessiodp.oreannouncer.common.utils.CoordinateUtils;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class BlockManager {
	protected final OreAnnouncerPlugin plugin;
	private final CoordinateUtils coordinatesUtils;
	
	public BlockManager(OreAnnouncerPlugin plugin) {
		this.plugin = plugin;
		coordinatesUtils = new CoordinateUtils(plugin);
	}
	
	public void sendAlerts(Alert alert) {
		for (User user : plugin.getOnlinePlayers()) {
			OAPlayerImpl onlinePlayer = plugin.getPlayerManager().getPlayer(user.getUUID());
			if (onlinePlayer != null && onlinePlayer.haveAlertsOn()) {
				String msg = null;
				if (!alert.getMessageAdmin().isEmpty()
						&& (
								user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE)
								|| (alert.getServerId() != null && user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE_SERVER.toString() + alert.getServerId()))
							)) {
					msg = alert.getMessageAdmin();
				} else if (!alert.getMessageUser().isEmpty()
						&& (
							user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE)
								|| (alert.getServerId() != null && user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE_SERVER.toString() + alert.getServerId()))
							)) {
					msg = alert.getMessageUser();
				}
				
				if (msg != null) {
					user.sendMessage(msg, true);
					
					// Send alert sound
					CommonUtils.ifNonEmptyDo(alert.getSound(), (s) -> user.playSound(s, ConfigMain.ALERTS_SOUND_VOLUME, ConfigMain.ALERTS_SOUND_PITCH));
				}
			}
		}
		
		// Send message to console
		if (ConfigMain.ALERTS_CONSOLE) {
			plugin.getLoggerManager().log(alert.getMessageConsole(), true);
		}
	}
	
	public void sendGlobalAlert(BlockData data, AlertType type) {}
	
	public void updateBlockDestroy(OAPlayerImpl player, String materialName, int numberOfBlocks) {
		plugin.getDatabaseManager().updateBlockDestroy(new BlockDestroy(
				player.getPlayerUUID(),
				materialName,
				numberOfBlocks
		));
	}
	
	public void updateBlockFound(BlockFound bf) {
		if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE) {
			OABlockImpl block = Blocks.LIST.get(bf.getMaterialName());
			if (block != null) {
				plugin.getDatabaseManager().insertBlockFound(bf);
			}
		}
	}
	
	public abstract boolean existsMaterial(String materialName);
	
	public abstract boolean isBlockMarked(ADPLocation blockLocation, String material, MarkType markType);
	
	public abstract boolean markBlock(ADPLocation blockLocation, String material, MarkType markType);
	
	public abstract void unmarkBlock(ADPLocation blockLocation, MarkType markType);
	
	public void handleAlerts(BlockData data) {
		String userMessage = CommonUtils.getOr(data.getBlock().getMessageUser(), Messages.ALERTS_USER);
		String adminMessage = CommonUtils.getOr(data.getBlock().getMessageAdmin(), Messages.ALERTS_ADMIN);
		String consoleMessage = CommonUtils.getOr(data.getBlock().getMessageConsole(), Messages.ALERTS_CONSOLE);
		
		userMessage = parseMessage(userMessage, data, AlerterType.USER);
		adminMessage = parseMessage(adminMessage, data, AlerterType.ADMIN);
		consoleMessage = parseMessage(consoleMessage, data, AlerterType.CONSOLE);
		
		// Send message to players
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(data.getBlock().getMaterialName())
					.setType(OAPacket.PacketType.ALERT)
					.setMessageUsers(userMessage)
					.setMessageAdmins(adminMessage)
					.setMessageConsole(consoleMessage);
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_ALERT, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_ALERT_FAILED);
			}
		} else {
			Alert alert = new Alert()
					.setMessageConsole(consoleMessage)
					.setData(data);
			if (data.isAlertUsers()) alert.setMessageUser(userMessage);
			if (data.isAlertAdmins()) alert.setMessageAdmin(adminMessage);
			CommonUtils.ifNonNullDo(data.getBlock().getSound(), alert::setSound);
			sendAlerts(alert);
		}
		sendGlobalAlert(data, AlertType.NORMAL);
	}
	
	public void handleBlockDestroy(BlockData data) {
		// Execute commands on destroy
		executeBlockCommands(ConfigMain.EXECUTE_COMMANDS_ON_DESTROY, data);
		
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(data.getBlock().getMaterialName())
					.setType(OAPacket.PacketType.DESTROY)
					.setPlayerUuid(data.getPlayer().getPlayerUUID())
					.setDestroyCount(data.getNumber());
			
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_DESTROY, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_DESTROY_FAILED);
			}
		} else {
			updateBlockDestroy(data.getPlayer(), data.getBlock().getMaterialName(), data.getNumber());
		}
	}
	
	public void handleBlockFound(BlockData data) {
		BlockFound newBf = new BlockFound(data.getPlayer().getPlayerUUID(), data.getBlock(), data.getNumber());
		BlocksFoundResult bfr = plugin.getDatabaseManager().getBlockFound(data.getPlayer().getPlayerUUID(), data.getBlock(), data.getBlock().getCountTime());
		if (bfr != null) {
			bfr.setTimestamp(Math.min(bfr.getTimestamp(), newBf.getTimestamp()));
			bfr.setTotal(bfr.getTotal() + newBf.getFound());
		} else {
			bfr = new BlocksFoundResult(newBf.getTimestamp(), newBf.getFound());
		}
		plugin.getLoggerManager().logDebug(OAConstants.DEBUG_COUNTER_HANDLING
				.replace("{player}", data.getPlayer().getName())
				.replace("{value}", Integer.toString(bfr.getTotal())), true);
		
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(data.getBlock().getMaterialName())
					.setType(OAPacket.PacketType.FOUND)
					.setPlayerUuid(data.getPlayer().getPlayerUUID())
					.setDestroyCount(data.getNumber());
			
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_FOUND, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_FOUND_FAILED);
			}
		} else {
			updateBlockFound(newBf);
		}
		
		// If count number enabled handle alerts
		if (data.getBlock().getCountNumber() > 0 && bfr.getTotal() >= data.getBlock().getCountNumber()) {
			data.setNumber(bfr.getTotal());
			
			// Alert
			String userMessage = CommonUtils.getOr(data.getBlock().getCountMessageUser(), Messages.ALERTS_COUNT_USER);
			String adminMessage = CommonUtils.getOr(data.getBlock().getCountMessageAdmin(), Messages.ALERTS_COUNT_ADMIN);
			String consoleMessage = CommonUtils.getOr(data.getBlock().getCountMessageConsole(), Messages.ALERTS_COUNT_CONSOLE);
			
			userMessage = parseMessage(userMessage, data, AlerterType.USER, bfr.getTimestamp());
			adminMessage = parseMessage(adminMessage, data, AlerterType.ADMIN, bfr.getTimestamp());
			consoleMessage = parseMessage(consoleMessage, data, AlerterType.CONSOLE, bfr.getTimestamp());
			
			// Execute commands on destroy
			executeBlockCommands(ConfigMain.EXECUTE_COMMANDS_ON_FOUND, data);
			
			if (plugin.isBungeeCordEnabled()) {
				OAPacket packet = new OAPacket(plugin.getVersion());
				packet
						.setMaterialName(data.getBlock().getMaterialName())
						.setType(OAPacket.PacketType.ALERT_COUNT)
						.setMessageUsers(userMessage)
						.setMessageAdmins(adminMessage)
						.setMessageConsole(consoleMessage);
				if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
					plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_ALERT_COUNT, true);
				} else {
					plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_ALERT_COUNT_FAILED);
				}
			} else {
				Alert alert = new Alert(userMessage, adminMessage, consoleMessage)
						.setData(data);
				CommonUtils.ifNonNullDo(data.getBlock().getSound(), alert::setSound);
				sendAlerts(alert);
			}
			sendGlobalAlert(data.setElapsed(bfr.getTimestamp()), AlertType.COUNT);
		}
	}
	
	public void handleTNTDestroy(BlockData data) {
		// data.getPlayer() can be null
		String userMessage = Messages.ALERTS_TNT_USER;
		String adminMessage = Messages.ALERTS_TNT_ADMIN;
		String consoleMessage = Messages.ALERTS_TNT_CONSOLE;
		
		userMessage = parseMessage(userMessage, data, AlerterType.USER);
		adminMessage = parseMessage(adminMessage, data, AlerterType.ADMIN);
		consoleMessage = parseMessage(consoleMessage, data, AlerterType.CONSOLE);
		
		// Send message to players
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(data.getBlock().getMaterialName())
					.setType(OAPacket.PacketType.ALERT_TNT)
					.setMessageUsers(userMessage)
					.setMessageAdmins(adminMessage)
					.setMessageConsole(consoleMessage);
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_ALERT_TNT, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_ALERT_TNT_FAILED);
			}
		} else {
			Alert alert = new Alert(userMessage, adminMessage, consoleMessage)
					.setData(data);
			CommonUtils.ifNonNullDo(data.getBlock().getSound(), alert::setSound);
			sendAlerts(alert);
		}
		sendGlobalAlert(data, AlertType.TNT);
	}
	
	public int countNearBlocks(ADPLocation blockLocation, String material, MarkType markType) {
		return countNearBlocks(blockLocation, material, markType, 0);
	}
	
	public int countNearBlocks(ADPLocation blockLocation, String material, MarkType markType, int currentNumber) {
		int ret = currentNumber;
		if (markBlock(blockLocation, material, markType)) {
			ret++;
			for (int x=-1; x <= 1; x++) {
				for (int y=-1; y <= 1; y++) {
					for (int z=-1; z <= 1; z++) {
						if (ret < 500 || ConfigMain.BLOCKS_BYPASS_SECURE_COUNTER) {
							ret = countNearBlocks(blockLocation.add(x, y, z), material, markType, ret);
						} else {
							// Calculating too many blocks, print an error
							// and force to stop the counter
							plugin.getLoggerManager().printError(OAConstants.DEBUG_EVENT_BLOCK_BREAK_INFINITE_COUNT
									.replace("{block}", material));
							return -1;
						}
						
						if (ret == -1)
							return -1;
					}
				}
			}
		}
		return ret;
	}
	
	public String parseMessage(String message, BlockData data, AlerterType alerterType) {
		return parseMessage(message, data, alerterType, CommonUtils.getOr(data.getElapsed(), -1L));
	}
	
	public String parseMessage(String message, BlockData data, AlerterType alerterType, long elapsed) {
		// Replace placeholders
		String pPlayer = data.getPlayer() != null ? data.getPlayer().getName() : Messages.ALERTS_TNT_UNKNOWN_PLAYER;
		String pNumber = Integer.toString(data.getNumber());
		String pBlock = data.getNumber() > 1 ? data.getBlock().getPluralName() : data.getBlock().getSingularName();
		
		Function<String, String> repl = (msg) -> plugin.getMessageUtils().convertBlockPlaceholders(msg
					.replace("%player%", pPlayer)
					.replace("%number%", pNumber)
					.replace("%block%", pBlock)
					.replace("%time%", elapsed >= 0 ? formatElapsed(elapsed) : "%time%"
					.replace("%light_level%", Integer.toString(data.getLightLevel()))
				), data.getBlock());
		
		String ret = plugin.getMessageUtils().convertPlayerPlaceholders(repl.apply(message), data.getPlayer());
		
		boolean hiddenCoordinates = alerterType == AlerterType.USER ?
				ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_USER :
				(alerterType == AlerterType.ADMIN ?
						ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN :
						ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE);
		ret = coordinatesUtils.replaceCoordinates(ret, data.getLocation(), hiddenCoordinates);
		
		// PlaceholderAPI
		return data.getPlayer() != null ? parsePAPI(data.getPlayer().getPlayerUUID(), ret) : ret;
	}
	
	private String formatElapsed(long timestamp) {
		return DurationUtils.formatWith(
				(System.currentTimeMillis() / 1000L) - timestamp,
				ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_LARGE,
				ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_MEDIUM,
				ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_SMALL
		);
	}
	
	private void executeBlockCommands(List<String> commands, BlockData data) {
		if (ConfigMain.EXECUTE_COMMANDS_ENABLE && !commands.isEmpty() && data.getPlayer() != null) {
			plugin.getScheduler().getSyncExecutor().execute(() -> {
				User user = plugin.getPlayer(data.getPlayer().getPlayerUUID());
				if (!user.hasPermission(OreAnnouncerPermission.ADMIN_BYPASS_EXECUTE_COMMANDS)) {
					for (String cmd : commands) {
						if (ConfigMain.EXECUTE_COMMANDS_RUN_AS.equalsIgnoreCase("custom")) {
							if (CommonUtils.toLowerCase(cmd).startsWith("console:"))
								plugin.getBootstrap().executeCommand(parseMessage(cmd.substring(8), data, AlerterType.CONSOLE));
							else if (CommonUtils.toLowerCase(cmd).startsWith("player:"))
								plugin.getBootstrap().executeCommandByUser(parseMessage(cmd.substring(7), data, AlerterType.CONSOLE), user);
							else
								plugin.getBootstrap().executeCommandByUser(parseMessage(cmd, data, AlerterType.CONSOLE), user);
						} else if (ConfigMain.EXECUTE_COMMANDS_RUN_AS.equalsIgnoreCase("console")) {
							plugin.getBootstrap().executeCommand(parseMessage(cmd, data, AlerterType.CONSOLE));
						} else {
							plugin.getBootstrap().executeCommandByUser(parseMessage(cmd, data, AlerterType.CONSOLE), user);
						}
					}
				}
			});
		}
	}
	
	protected abstract String parsePAPI(UUID playerUuid, String message);
	
	public enum MarkType {
		ALERT("OreAnnouncer_alert"),
		FOUND("OreAnnouncer_found"),
		STORE("OreAnnouncer_store");
		
		@Getter private final String mark;
		
		MarkType(String mark) {
			this.mark = mark;
		}
	}
	
	public enum AlertType {
		NORMAL, COUNT, TNT
	}
	
	public enum AlerterType {
		USER, ADMIN, CONSOLE
	}
}
