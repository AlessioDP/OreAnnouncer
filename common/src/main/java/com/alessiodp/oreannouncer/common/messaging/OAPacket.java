package com.alessiodp.oreannouncer.common.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString
public class OAPacket extends ADPPacket {
	// Common
	@Getter private PacketType type;
	@Getter private String serverName;
	@Getter private String serverId;
	
	@Getter private UUID playerUuid;
	@Getter private Data data;
	@Getter private Messages messages;
	
	public OAPacket(String version) {
		super(version);
	}
	
	@Override
	public String getName() {
		return type != null ? type.name() : "UNKNOWN";
	}
	
	public static OAPacket read(ADPPlugin plugin, byte[] bytes) {
		OAPacket ret = null;
		
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
			ObjectInput in = new ObjectInputStream(bis);
			ret = (OAPacket) in.readObject();
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(String.format(Constants.DEBUG_LOG_MESSAGING_FAILED_READ, ex.getMessage()));
		}
		return ret;
	}
	
	public OAPacket setType(PacketType type) {
		this.type = type;
		return this;
	}
	
	public OAPacket setPlayerUuid(UUID playerUuid) {
		this.playerUuid = playerUuid;
		return this;
	}
	
	public OAPacket setData(BlockData blockData) {
		this.data = new Data(
				blockData.getPlayer().getPlayerUUID(),
				blockData.getBlock().getMaterialName(),
				blockData.getNumber(),
				blockData.getLocation(),
				blockData.getLightLevel(),
				blockData.getHeightLevel()
		);
		return this;
	}
	
	public OAPacket setDataRaw(Data data) {
		this.data = data;
		return this;
	}
	
	public OAPacket setMessages(String user, String admin, String console) {
		this.messages = new Messages(user, admin, console);
		return this;
	}
	
	public OAPacket setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}
	
	public OAPacket setServerId(String serverId) {
		this.serverId = serverId;
		return this;
	}
	
	public enum PacketType {
		UPDATE_PLAYER, ALERT, ALERT_TNT, ALERT_COUNT, DESTROY, FOUND
	}
	
	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class Data implements Serializable {
		@Getter UUID player;
		@Getter String block;
		@Getter int number;
		@Getter ADPLocation location;
		@Getter int lightLevel;
		@Getter int heightLevel;
	}
	
	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class Messages implements Serializable {
		@Getter String user;
		@Getter String admin;
		@Getter String console;
	}
}
