package com.alessiodp.oreannouncer.bungeecord.messaging.bungee;

import com.alessiodp.core.bungeecord.messaging.bungee.BungeeBungeecordListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
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
import org.jetbrains.annotations.NotNull;

public class BungeeOABungeecordListener extends BungeeBungeecordListener {
	
	public BungeeOABungeecordListener(@NotNull ADPPlugin plugin) {
		super(plugin, true, false);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, @NotNull MessageChannel messageChannel) {
		if (messageChannel != MessageChannel.MAIN)
			return; // Handle only packets for main channel
		
		OAPacket packet = OAPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name(), messageChannel.getId()), true);
			if (BungeeConfigMain.BLOCKS_LISTALLOWED.contains("*") || BungeeConfigMain.BLOCKS_LISTALLOWED.contains(packet.getData().getBlock())) {
				switch ((OAPacket.PacketType) packet.getType()) {
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
								plugin.getLoggerManager().logError(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_ALERT_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
								ex.printStackTrace();
							}
						}
						break;
					case ALERT_COUNT:
						if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE) {
							try {
								parseAlertPacket(packet, BlockManager.AlertType.COUNT);
							} catch (Exception ex) {
								plugin.getLoggerManager().logError(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_ALERT_COUNT_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
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
							plugin.getLoggerManager().logError(OAConstants.DEBUG_MESSAGING_DESTROY_UUID_EMPTY);
						}
						break;
					case FOUND:
						if (packet.getData().getPlayer() != null) {
							((OreAnnouncerPlugin) plugin).getBlockManager().updateBlockFound(new BlockFound(
									packet.getData().getPlayer(),
									packet.getData().getBlock(),
									packet.getData().getNumber()));
						} else {
							plugin.getLoggerManager().logError(OAConstants.DEBUG_MESSAGING_FOUND_UUID_EMPTY);
						}
						break;
				}
			}
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
						.setServerId(packet.getSource())
		);
		((OreAnnouncerPlugin) plugin).getBlockManager().sendGlobalAlert(
				blockData,
				alertType
		);
	}
}
