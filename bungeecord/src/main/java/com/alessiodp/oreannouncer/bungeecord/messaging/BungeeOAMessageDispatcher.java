package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.messaging.OAMessageDispatcher;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;

public class BungeeOAMessageDispatcher extends BungeeMessageDispatcher implements OAMessageDispatcher {
	public BungeeOAMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
	
	@Override
	public void sendUpdatePlayer(OAPlayerImpl player) {
		sendPacket(makePacket(OAPacket.PacketType.UPDATE_PLAYER)
						.setPlayerUuid(player.getPlayerUUID())
				, getSubChannel());
	}
	
	@Override
	public void sendAlert(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getSubChannel());
	}
	
	@Override
	public void sendAlertCount(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT_COUNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getSubChannel());
	}
	
	@Override
	public void sendAlertTNT(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT_TNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getSubChannel());
	}
	
	@Override
	public void sendBlockDestroy(BlockData blockData) {
		sendPacket(makePacket(OAPacket.PacketType.DESTROY)
				.setData(blockData)
		, getSubChannel());
	}
	
	@Override
	public void sendBlockFound(BlockData blockData) {
		sendPacket(makePacket(OAPacket.PacketType.FOUND)
				.setData(blockData)
		, getSubChannel());
	}
	
	private OAPacket makePacket(OAPacket.PacketType type) {
		return new OAPacket(plugin.getVersion()).setType(type);
	}
}
