package com.alessiodp.oreannouncer.common.blocks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
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
import org.apache.commons.lang.time.DurationFormatUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.function.Function;

public abstract class BlockManager {
	protected final OreAnnouncerPlugin plugin;
	private final CoordinateUtils coordinatesUtils;
	
	public BlockManager(OreAnnouncerPlugin plugin) {
		this.plugin = plugin;
		coordinatesUtils = new CoordinateUtils(plugin);
	}
	
	public void sendAlerts(String userMessage, String adminMessage, String consoleMessage, String sound) {
		sendAlerts(userMessage, adminMessage, consoleMessage, sound, null);
	}
	
	public void sendAlerts(String userMessage, String adminMessage, String consoleMessage, String sound, String serverId) {
		for (User user : plugin.getOnlinePlayers()) {
			OAPlayerImpl onlinePlayer = plugin.getPlayerManager().getPlayer(user.getUUID());
			if (onlinePlayer != null && onlinePlayer.haveAlertsOn()) {
				String msg = null;
				if (!adminMessage.isEmpty()
						&& (
								user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE.toString())
								|| (serverId != null && user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE_SERVER.toString() + serverId))
							)) {
					msg = adminMessage;
				} else if (!userMessage.isEmpty()
						&& (
							user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE.toString())
								|| (serverId != null && user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE_SERVER.toString() + serverId))
							)) {
					msg = userMessage;
				}
				
				if (msg != null) {
					user.sendMessage(msg, true);
					
					// Send alert sound
					if (!sound.isEmpty())
						user.playSound(sound, ConfigMain.ALERTS_SOUND_VOLUME, ConfigMain.ALERTS_SOUND_PITCH);
				}
			}
		}
		
		// Send message to console
		if (ConfigMain.ALERTS_CONSOLE) {
			plugin.getLoggerManager().log(consoleMessage, true);
		}
	}
	
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
	
	public void handleAlerts(boolean alertUsers, boolean alertAdmins, OAPlayerImpl player, OABlockImpl block, ADPLocation blockLocation, int numberOfBlocks) {
		String userMessage = block.getMessageUser() != null ? block.getMessageUser() : Messages.ALERTS_USER;
		String adminMessage = block.getMessageAdmin() != null ? block.getMessageAdmin() : Messages.ALERTS_ADMIN;
		String consoleMessage = block.getMessageConsole() != null ? block.getMessageConsole() : Messages.ALERTS_CONSOLE;
		
		userMessage = parseMessage(userMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_USER, numberOfBlocks);
		adminMessage = parseMessage(adminMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN, numberOfBlocks);
		consoleMessage = parseMessage(consoleMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE, numberOfBlocks);
		
		// Send message to players
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(block.getMaterialName())
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
			sendAlerts(
					alertUsers ? userMessage : "",
					alertAdmins ? adminMessage : "",
					consoleMessage,
					block.getSound() != null ? block.getSound() : ConfigMain.ALERTS_SOUND_DEFAULT);
		}
	}
	
	public void handleBlockDestroy(OABlockImpl block, OAPlayerImpl player, int numberOfBlocks) {
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(block.getMaterialName())
					.setType(OAPacket.PacketType.DESTROY)
					.setPlayerUuid(player.getPlayerUUID())
					.setDestroyCount(numberOfBlocks);
			
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_DESTROY, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_DESTROY_FAILED);
			}
		} else {
			updateBlockDestroy(player, block.getMaterialName(), numberOfBlocks);
		}
	}
	
	public void handleBlockFound(OABlockImpl block, OAPlayerImpl player, ADPLocation blockLocation, int numberOfBlocks) {
		BlockFound newBf = new BlockFound(player.getPlayerUUID(), block, numberOfBlocks);
		BlocksFoundResult bfr = plugin.getDatabaseManager().getBlockFound(player.getPlayerUUID(), block, block.getCountTime());
		if (bfr != null) {
			bfr.setTimestamp(Math.min(bfr.getTimestamp(), newBf.getTimestamp()));
			bfr.setTotal(bfr.getTotal() + newBf.getFound());
		} else {
			bfr = new BlocksFoundResult(newBf.getTimestamp(), newBf.getFound());
		}
		
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(block.getMaterialName())
					.setType(OAPacket.PacketType.FOUND)
					.setPlayerUuid(player.getPlayerUUID())
					.setDestroyCount(numberOfBlocks);
			
			if (plugin.getMessenger().getMessageDispatcher().sendPacket(packet)) {
				plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_SEND_FOUND, true);
			} else {
				plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_SEND_FOUND_FAILED);
			}
		} else {
			updateBlockFound(newBf);
		}
		
		// If count number enabled handle alerts
		if (block.getCountNumber() > 0 && bfr.getTotal() >= block.getCountNumber()) {
			
			// Alert
			String userMessage = block.getCountMessageUser() != null ? block.getCountMessageUser() : Messages.ALERTS_COUNT_USER;
			String adminMessage = block.getCountMessageAdmin() != null ? block.getCountMessageAdmin() : Messages.ALERTS_COUNT_ADMIN;
			String consoleMessage = block.getCountMessageConsole() != null ? block.getCountMessageConsole() : Messages.ALERTS_COUNT_CONSOLE;
			
			userMessage = parseMessage(userMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_USER, bfr.getTotal(), bfr.getTimestamp());
			adminMessage = parseMessage(adminMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN, bfr.getTotal(), bfr.getTimestamp());
			consoleMessage = parseMessage(consoleMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE, bfr.getTotal(), bfr.getTimestamp());
			
			if (plugin.isBungeeCordEnabled()) {
				OAPacket packet = new OAPacket(plugin.getVersion());
				packet
						.setMaterialName(block.getMaterialName())
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
				sendAlerts(
						userMessage,
						adminMessage,
						consoleMessage,
						block.getSound() != null ? block.getSound() : ConfigMain.ALERTS_SOUND_DEFAULT);
			}
		}
	}
	
	public void handleTNTDestroy(@Nullable OAPlayerImpl player, OABlockImpl block, ADPLocation blockLocation, int numberOfBlocks) {
		String userMessage = player != null ? Messages.ALERTS_TNT_PLAYER_USER : Messages.ALERTS_TNT_USER;
		String adminMessage = player != null ? Messages.ALERTS_TNT_PLAYER_ADMIN : Messages.ALERTS_TNT_ADMIN;
		String consoleMessage = player != null ? Messages.ALERTS_TNT_PLAYER_CONSOLE : Messages.ALERTS_TNT_CONSOLE;
		
		userMessage = parseMessage(userMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_USER, numberOfBlocks);
		adminMessage = parseMessage(adminMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN, numberOfBlocks);
		consoleMessage = parseMessage(consoleMessage, player, block, blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE, numberOfBlocks);
		
		// Send message to players
		if (plugin.isBungeeCordEnabled()) {
			OAPacket packet = new OAPacket(plugin.getVersion());
			packet
					.setMaterialName(block.getMaterialName())
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
			sendAlerts(
					userMessage,
					adminMessage,
					consoleMessage,
					block.getSound() != null ? block.getSound() : ConfigMain.ALERTS_SOUND_DEFAULT);
		}
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
	
	protected String parseMessage(String message, OAPlayerImpl player, OABlockImpl block, ADPLocation blockLocation, boolean hiddenCoordinates, int numberOfBlocks) {
		return parseMessage(message, player, block, blockLocation, hiddenCoordinates, numberOfBlocks, 0);
	}
	
	protected String parseMessage(String message, @Nullable OAPlayerImpl player, OABlockImpl block, ADPLocation blockLocation, boolean hiddenCoordinates, int numberOfBlocks, long elapsed) {
		// Replace placeholders
		String pPlayer = player != null ? player.getName() : "unknown";
		String pNumber = Integer.toString(numberOfBlocks);
		String pBlock = numberOfBlocks > 1 ? block.getPluralName() : block.getSingularName();
		
		Function<String, String> repl = (msg) -> msg
				.replace("%player%", pPlayer)
				.replace("%number%", pNumber)
				.replace("%block%", pBlock)
				.replace("%time%", formatElapsed(elapsed));
		
		String ret = plugin.getMessageUtils().convertPlayerPlaceholders(repl.apply(message), player);
		
		ret = coordinatesUtils.replaceCoordinates(ret, blockLocation, hiddenCoordinates);
		
		// PlaceholderAPI
		return player != null ? parsePAPI(player.getPlayerUUID(), ret) : ret;
	}
	
	private String formatElapsed(long timestamp) {
		long elapsed = System.currentTimeMillis() - (timestamp * 1000L);
		String format = elapsed >= 3600000 ? ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_LARGE
				: (elapsed >= 60000 ? ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_MEDIUM
				: ConfigMain.STATS_ADVANCED_COUNT_TIME_FORMAT_SMALL);
		return DurationFormatUtils.formatDuration(elapsed, format);
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
}
