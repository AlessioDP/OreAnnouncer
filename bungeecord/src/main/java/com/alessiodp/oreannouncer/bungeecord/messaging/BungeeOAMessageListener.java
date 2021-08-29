package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.Alert;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockLocationImpl;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

public class BungeeOAMessageListener extends BungeeMessageListener {
	
	public BungeeOAMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin, true, false);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, String channel) {
		if (channel.equals(getMainChannel()))
			handleChannelMain(bytes, channel); // Dispatch Spigot -> BungeeCord
	}
	
	public void handleChannelMain(byte[] bytes, String channel) {
		OAPacket packet = OAPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name(), channel), true);
			
			if (BungeeConfigMain.BLOCKS_LISTALLOWED.contains("*") || BungeeConfigMain.BLOCKS_LISTALLOWED.contains(packet.getData().getBlock())) {
				switch (packet.getType()) {
					case UPDATE_PLAYER:
						if (((OreAnnouncerPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
							plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER,
									packet.getPlayerUuid().toString()), true);
						}
						break;
					case ALERT:
					case ALERT_TNT:
						if (ConfigMain.ALERTS_ENABLE) {
							try {
								parseAlertPacket(packet, packet.getType() == OAPacket.PacketType.ALERT ? BlockManager.AlertType.NORMAL : BlockManager.AlertType.TNT);
							} catch (Exception ex) {
								plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_ALERT_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
								ex.printStackTrace();
							}
						}
						break;
					case ALERT_COUNT:
						if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE) {
							try {
								parseAlertPacket(packet, BlockManager.AlertType.COUNT);
							} catch (Exception ex) {
								plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_ALERT_COUNT_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
								ex.printStackTrace();
							}
						}
						break;
					case DESTROY:
						if (packet.getData().getPlayer() != null) {
							OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(packet.getData().getPlayer());
							((OreAnnouncerPlugin) plugin).getBlockManager().updateBlockDestroy(
									player,
									packet.getData().getBlock(),
									packet.getData().getNumber());
							
							OABlock block = Blocks.searchBlock(packet.getData().getBlock());
							if (block != null) {
								((OreAnnouncerPlugin) plugin).getEventManager().callEvent(((OreAnnouncerPlugin) plugin).getEventManager().prepareBlockDestroyEvent(
										player,
										block,
										new BlockLocationImpl(packet.getData().getLocation())
								));
							}
						} else {
							plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_DESTROY_UUID_EMPTY);
						}
						break;
					case FOUND:
						if (packet.getData().getPlayer() != null) {
							((OreAnnouncerPlugin) plugin).getBlockManager().updateBlockFound(new BlockFound(
									packet.getData().getPlayer(),
									packet.getData().getBlock(),
									packet.getData().getNumber()));
						} else {
							plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_FOUND_UUID_EMPTY);
						}
						break;
					default:
						// Not supported packet type
				}
			}
		} else {
			plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED_WRONG, channel));
		}
	}
	
	private void parseAlertPacket(OAPacket packet, BlockManager.AlertType alertType) {
		OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(packet.getData().getPlayer());
		OABlockImpl block = Blocks.searchBlock(packet.getData().getBlock());
		BlockData blockData = new BlockData(player, block, packet.getData().getNumber())
				.setLocation(packet.getData().getLocation())
				.setLightLevel(packet.getData().getLightLevel());
		
		((OreAnnouncerPlugin) plugin).getBlockManager().sendAlerts(
				new Alert(packet.getMessages().getUser(), packet.getMessages().getAdmin(), packet.getMessages().getConsole())
						.setServerId(packet.getServerId())
		);
		((OreAnnouncerPlugin) plugin).getBlockManager().sendGlobalAlert(
				blockData,
				alertType
		);
	}
}
