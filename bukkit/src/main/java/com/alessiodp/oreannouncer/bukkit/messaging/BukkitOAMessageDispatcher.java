package com.alessiodp.oreannouncer.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.messaging.OAMessageDispatcher;
import com.alessiodp.oreannouncer.common.messaging.OAPacket;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;


public class BukkitOAMessageDispatcher extends BukkitMessageDispatcher implements OAMessageDispatcher {
	public BukkitOAMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin, true, true, false);
	}
	
	@Override
	public void sendUpdatePlayer(OAPlayerImpl player) {
		sendPacket(makePacket(OAPacket.PacketType.UPDATE_PLAYER)
						.setPlayerUuid(player.getPlayerUUID())
				, getMainChannel());
		sendPacket(makePacket(OAPacket.PacketType.UPDATE_PLAYER)
						.setPlayerUuid(player.getPlayerUUID())
				, getSubChannel());
	}
	
	@Override
	public void sendAlert(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getMainChannel());
	}
	
	@Override
	public void sendAlertCount(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT_COUNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getMainChannel());
	}
	
	@Override
	public void sendAlertTNT(BlockData blockData, String messageUser, String messageAdmin, String messageConsole) {
		sendPacket(makePacket(OAPacket.PacketType.ALERT_TNT)
				.setData(blockData)
				.setMessages(messageUser, messageAdmin, messageConsole)
		, getMainChannel());
	}
	
	@Override
	public void sendBlockDestroy(BlockData blockData) {
		sendPacket(makePacket(OAPacket.PacketType.DESTROY)
				.setData(blockData)
		, getMainChannel());
	}
	
	@Override
	public void sendBlockFound(BlockData blockData) {
		sendPacket(makePacket(OAPacket.PacketType.FOUND)
				.setData(blockData)
		, getMainChannel());
	}
	
	private OAPacket makePacket(OAPacket.PacketType type) {
		return new OAPacket(plugin.getVersion())
				.setType(type)
				.setServerName(BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_NAME)
				.setServerId(BukkitConfigMain.OREANNOUNCER_BUNGEECORD_SERVER_ID);
	}
}
