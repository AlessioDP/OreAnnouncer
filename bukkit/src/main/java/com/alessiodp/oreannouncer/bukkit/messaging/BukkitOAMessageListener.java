package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import lombok.NonNull;

public class BukkitOAMessageListener extends BukkitMessageListener {
	
	public BukkitOAMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, String channel) {
		if (channel.equals(getSubChannel()))
			handleFromSub(bytes, channel); // Dispatch BungeeCord -> Spigot
	}
	
	public void handleFromSub(byte[] bytes, String channel) {
		OAPacket packet = OAPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name(), channel), true);
			
			if (packet.getType() == OAPacket.PacketType.UPDATE_PLAYER) {
				if (((OreAnnouncerPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
					plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER,
							packet.getPlayerUuid().toString()), true);
				}
			}
		} else {
			plugin.getLoggerManager().printError(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED_WRONG, channel));
		}
	}
}
