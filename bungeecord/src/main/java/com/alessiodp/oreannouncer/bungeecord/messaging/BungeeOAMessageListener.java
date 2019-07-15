package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
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
			switch (packet.getType()) {
				case ALERT:
					((OreAnnouncerPlugin) plugin).getBlockManager().sendAlerts(
							packet.getMessageUsers(),
							packet.getMessageAdmins(),
							packet.getMessageConsole(),
							"");
					break;
				case DESTROY:
					if (!packet.getPlayerUuid().isEmpty()) {
						OAPlayerImpl player = ((OreAnnouncerPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(packet.getPlayerUuid()));
						((OreAnnouncerPlugin) plugin).getBlockManager().updateBlock(
								player,
								packet.getMaterialName(),
								packet.getDestroyCount());
					} else {
						plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_DESTROY_UUID_EMPTY);
					}
					break;
				default:
					// Not supported packet type
			}
		} else {
			plugin.getLoggerManager().printError(OAConstants.DEBUG_MESSAGING_RECEIVED_WRONG);
		}
	}
}
