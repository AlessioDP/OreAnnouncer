package com.alessiodp.oreannouncer.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.oreannouncer.bungeecord.messaging.bungee.BungeeOABungeecordDispatcher;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.messaging.OAMessageDispatcher;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class BungeeOAMessageDispatcher extends BungeeMessageDispatcher implements OAMessageDispatcher {
	public BungeeOAMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(
				plugin,
				new BungeeOABungeecordDispatcher(plugin),
				null
		);
	}
	
	private void sendPacketToBungeecord(@NotNull OAPacket packet) {
		bungeeDispatcher.sendPacket(packet, MessageChannel.SUB);
	}
	
	@Override
	public void sendUpdatePlayer(OAPlayerImpl player) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.UPDATE_PLAYER)
						.setPlayerUuid(player.getPlayerUUID()));
	}
	
	@Override
	public void sendAlert(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.ALERT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole));
	}
	
	@Override
	public void sendAlertCount(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.ALERT_COUNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole));
	}
	
	@Override
	public void sendAlertTNT(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.ALERT_TNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole));
	}
	
	@Override
	public void sendBlockDestroy(BlockData blockData) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.DESTROY)
				.setData(blockData));
	}
	
	@Override
	public void sendBlockFound(BlockData blockData) {
		sendPacketToBungeecord(makePacket(OAPacket.PacketType.FOUND)
				.setData(blockData));
	}
	
	private OAPacket makePacket(OAPacket.PacketType type) {
		return (OAPacket) new OAPacket()
				.setVersion(plugin.getVersion())
				.setType(type);
	}
}
