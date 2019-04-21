package com.alessiodp.oreannouncer.common.blocks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.utils.CoordinateUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public abstract class BlockManager {
	protected final OreAnnouncerPlugin plugin;
	private final CoordinateUtils coordinatesUtils;
	
	@Getter private HashMap<String, OABlockImpl> listBlocks;
	@Getter protected List<String> allowedBlocks;
	
	public BlockManager(OreAnnouncerPlugin plugin) {
		this.plugin = plugin;
		coordinatesUtils = new CoordinateUtils(plugin);
	}
	
	public void reload() {
		listBlocks = new HashMap<>();
		allowedBlocks = new ArrayList<>();
		for (OABlockImpl block : ConfigMain.BLOCKS_LIST) {
			listBlocks.put(block.getMaterialName(), block);
			allowedBlocks.add(block.getMaterialName());
		}
	}
	
	public void sendAlerts(String userMessage, String adminMessage, String consoleMessage) {
		for (User user : plugin.getOnlinePlayers()) {
			OAPlayerImpl onlinePlayer = plugin.getPlayerManager().getPlayer(user.getUUID());
			if (onlinePlayer != null && onlinePlayer.haveAlertsOn()) {
				String msg = null;
				if (!adminMessage.isEmpty() && user.hasPermission(OreAnnouncerPermission.ADMIN_ALERTS_SEE.toString())) {
					msg = adminMessage;
				} else if (!userMessage.isEmpty() && user.hasPermission(OreAnnouncerPermission.USER_ALERTS_SEE.toString())) {
					msg = userMessage;
				}
				
				if (msg != null) {
					user.sendMessage(msg, true);
				}
			}
		}
		
		// Send message to console
		if (ConfigMain.ALERTS_CONSOLE) {
			plugin.getLoggerManager().log(consoleMessage, true);
		}
	}
	
	public void updateBlock(OAPlayerImpl player, String materialName, int numberOfBlocks) {
		PlayerDataBlock pdb = null;
		player.getLock().lock(); // Lock player
		// Get from memory
		for (PlayerDataBlock dataBlock : player.getDataBlocks().values()) {
			if (dataBlock.getMaterialName().equalsIgnoreCase(materialName)) {
				// Update block
				pdb = dataBlock;
				break;
			}
			
		}
		
		if (pdb == null) {
			// New block
			pdb = new PlayerDataBlock(
					materialName,
					player.getPlayerUUID(),
					0
			);
			player.getDataBlocks().put(materialName.toLowerCase(), pdb);
		}
		
		pdb.setDestroyCount(pdb.getDestroyCount() + numberOfBlocks);
		
		plugin.getDatabaseManager().updateDataBlock(pdb);
		player.updatePlayer();
		
		player.getLock().unlock(); // Unlock player
	}
	
	public abstract boolean existsMaterial(String materialName);
	
	public abstract boolean markBlock(ADPLocation blockLocation, String material);
	
	public abstract void unmarkBlock(ADPLocation blockLocation);
	
	public void handleAlerts(boolean alertUsers, boolean alertAdmins, OAPlayerImpl player, OABlockImpl block, ADPLocation blockLocation, int numberOfBlocks) {
		String userMessage = Messages.ALERTS_USER;
		String adminMessage = Messages.ALERTS_ADMIN;
		String consoleMessage = Messages.ALERTS_CONSOLE;
		
		// Replace placeholders
		String pPlayer = player.getName();
		String pNumber = Integer.toString(numberOfBlocks);
		String pBlock = numberOfBlocks > 1 ? block.getPluralName() : block.getSingularName();
		
		Function<String, String> repl = (msg) -> msg
					.replace("%player%", pPlayer)
					.replace("%number%", pNumber)
					.replace("%block%", pBlock);
		
		userMessage = repl.apply(userMessage)
				.replace("%coordinates%", coordinatesUtils.calculate(blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_USER));
		adminMessage = repl.apply(adminMessage)
				.replace("%coordinates%", coordinatesUtils.calculate(blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_ADMIN));
		consoleMessage = repl.apply(consoleMessage)
				.replace("%coordinates%", plugin.getColorUtils().removeColors(coordinatesUtils.calculate(blockLocation, ConfigMain.ALERTS_COORDINATES_HIDE_HIDDENFOR_CONSOLE)));
		
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
					consoleMessage);
		}
	}
	
	public int countNearBlocks(ADPLocation blockLocation, String material) {
		int ret = 0;
		if (markBlock(blockLocation, material)) {
			ret++;
			for (int x=-1; x <= 1; x++) {
				for (int y=-1; y <= 1; y++) {
					for (int z=-1; z <= 1; z++) {
						ret += countNearBlocks(blockLocation.add(x, y, z), material);
					}
				}
			}
		}
		return ret;
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
			updateBlock(player, block.getMaterialName(), numberOfBlocks);
		}
	}
}
