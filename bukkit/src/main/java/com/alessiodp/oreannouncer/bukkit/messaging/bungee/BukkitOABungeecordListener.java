package com.alessiodp.oreannouncer.bukkit.messaging.bungee;

import com.alessiodp.core.bukkit.messaging.bungee.BukkitBungeecordListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import org.jetbrains.annotations.NotNull;

public class BukkitOABungeecordListener extends BukkitBungeecordListener {
	public BukkitOABungeecordListener(@NotNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, @NotNull MessageChannel messageChannel) {
		if (messageChannel != MessageChannel.SUB)
			return; // Handle only packets for sub channel
		
		OAPacket packet = OAPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name(), messageChannel.getId()), true);
			if (packet.getType() == OAPacket.PacketType.UPDATE_PLAYER) {
				if (((OreAnnouncerPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
					plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER,
							packet.getPlayerUuid().toString()), true);
				}
			}
		}
	}
}
