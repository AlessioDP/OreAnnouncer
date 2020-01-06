package com.alessiodp.oreannouncer.common.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;

import java.util.UUID;

public class OAPacket extends ADPPacket {
	// Common
	@Getter private String materialName = "";
	@Getter private PacketType type;
	
	// Alert
	@Getter private String messageUsers = "";
	@Getter private String messageAdmins = "";
	@Getter private String messageConsole = "";
	
	// Destroy
	@Getter private String playerUuid = "";
	@Getter private int destroyCount;
	
	
	public OAPacket(String version) {
		super(version);
	}
	
	@Override
	public byte[] build() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		
		try {
			output.writeUTF(getVersion());
			output.writeUTF(materialName);
			output.writeUTF(type.name());
			output.writeUTF(messageUsers);
			output.writeUTF(messageAdmins);
			output.writeUTF(messageConsole);
			output.writeUTF(playerUuid);
			output.writeInt(destroyCount);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return output.toByteArray();
	}
	
	public static OAPacket read(ADPPlugin plugin, byte[] bytes) {
		OAPacket ret = null;
		try {
			ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
			String foundVersion = input.readUTF();
			
			if (foundVersion.equals(plugin.getVersion())) {
				OAPacket packet = new OAPacket(foundVersion);
				packet.materialName = input.readUTF();
				packet.type = PacketType.valueOf(input.readUTF());
				packet.messageUsers = input.readUTF();
				packet.messageAdmins = input.readUTF();
				packet.messageConsole = input.readUTF();
				packet.playerUuid = input.readUTF();
				packet.destroyCount = input.readInt();
				ret = packet;
			} else {
				plugin.getLoggerManager().printError(Constants.DEBUG_LOG_MESSAGING_FAILED_VERSION
						.replace("{current}", plugin.getVersion())
						.replace("{version}", foundVersion));
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(Constants.DEBUG_LOG_MESSAGING_FAILED_READ
					.replace("{message}", ex.getMessage()));
		}
		return ret;
	}
	
	public OAPacket setMaterialName(String materialName) {
		this.materialName = materialName;
		return this;
	}
	
	public OAPacket setType(PacketType type) {
		this.type = type;
		return this;
	}
	
	public OAPacket setMessageUsers(String messageUsers) {
		this.messageUsers = messageUsers;
		return this;
	}
	
	public OAPacket setMessageAdmins(String messageAdmins) {
		this.messageAdmins = messageAdmins;
		return this;
	}
	
	public OAPacket setMessageConsole(String messageConsole) {
		this.messageConsole = messageConsole;
		return this;
	}
	
	public OAPacket setPlayerUuid(UUID playerUuid) {
		this.playerUuid = playerUuid.toString();
		return this;
	}
	
	public OAPacket setDestroyCount(int destroyCount) {
		this.destroyCount = destroyCount;
		return this;
	}
	
	public enum PacketType {
		ALERT, ALERT_TNT, ALERT_COUNT, DESTROY
	}
}
