package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

import java.util.UUID;

public class BungeeOAMessageListener extends BungeeMessageListener {
	
	public BungeeOAMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void handlePacket(byte[] bytes) {
		OAPacket packet = OAPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_MESSAGING_RECEIVED
					.replace("{type}", packet.getType().name()), true);
			if (BungeeConfigMain.BLOCKS_LISTALLOWED.contains("*") || BungeeConfigMain.BLOCKS_LISTALLOWED.contains(packet.getMaterialName())) {
				switch (packet.getType()) {
					case ALERT:
					case ALERT_TNT:
						if (ConfigMain.ALERTS_ENABLE) {
							((OreAnnouncerPlugin) plugin).getBlockManager().sendAlerts(
									packet.getMessageUsers(),
									packet.getMessageAdmins(),
									packet.getMessageConsole(),
									"",
									packet.getServerId());
						}
						break;
					case ALERT_COUNT:
						if (ConfigMain.STATS_ADVANCED_COUNT_ENABLE) {
							((OreAnnouncerPlugin) plugin).getBlockManager().sendAlerts(
									packet.getMessageUsers(),
									packet.getMessageAdmins(),
									packet.getMessageConsole(),
									"",
									packet.getServerId());
						}
						break;
					case DESTROY:
						if (!packet.getPlayerUuid().isEmpty()) {
							OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(packet.getPlayerUuid()));
							((OreAnnouncerPlugin) plugin).getBlockManager().updateBlockDestroy(
									player,
									packet.getMaterialName(),
									packet.getDestroyCount());
						} else {
							plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_DESTROY_UUID_EMPTY);
						}
						break;
					case FOUND:
						if (!packet.getPlayerUuid().isEmpty()) {
							((OreAnnouncerPlugin) plugin).getBlockManager().updateBlockFound(new BlockFound(UUID.fromString(packet.getPlayerUuid()), packet.getMaterialName(), packet.getDestroyCount()));
						} else {
							plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_FOUND_UUID_EMPTY);
						}
						break;
					default:
						// Not supported packet type
				}
			}
		} else {
			plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_RECEIVED_WRONG);
		}
	}
}
