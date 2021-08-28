package com.alessiodp.oreannouncer.common.messaging;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class OAPacketTest {
	private static final String VERSION = "1";
	
	@Test
	public void testBuildBasic() throws IOException {
		makePacket()
				.setType(OAPacket.PacketType.values()[0])
				.build();
	}
	
	@Test
	public void testBuildAdvanced() throws IOException {
		byte[] raw = makePacket()
				.setType(OAPacket.PacketType.values()[0])
				.setDataRaw(new OAPacket.Data(
						UUID.randomUUID(),
						"material",
						1,
						new ADPLocation("world", 0, 0, 0, 0, 0),
						1,
						0
				))
				.build();
		assertNotNull(raw);
	}
	
	@Test
	public void testReadBasic() throws IOException {
		OreAnnouncerPlugin plugin = mockPlugin();
		OAPacket packet = makePacket()
				.setType(OAPacket.PacketType.values()[0]);
		
		OAPacket newPacket = OAPacket.read(plugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadBasicFail() throws IOException {
		OreAnnouncerPlugin plugin = mockPlugin();
		OAPacket packet = makePacket()
				.setType(OAPacket.PacketType.values()[0]);
		
		OAPacket newPacket = OAPacket.read(plugin, packet.build());
		packet = packet.setServerId("different");
		assertNotEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvanced() throws IOException {
		OreAnnouncerPlugin plugin = mockPlugin();
		OAPacket packet = makePacket()
				.setType(OAPacket.PacketType.values()[0])
				.setDataRaw(new OAPacket.Data(
						UUID.randomUUID(),
						"material",
						1,
						new ADPLocation("world", 0, 0, 0, 0, 0),
						1,
						0
				));
		
		OAPacket newPacket = OAPacket.read(plugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvancedFail() throws IOException {
		OreAnnouncerPlugin plugin = mockPlugin();
		
		OAPacket packet = makePacket()
				.setType(OAPacket.PacketType.values()[0])
				.setDataRaw(new OAPacket.Data(
						UUID.randomUUID(),
						"material",
						1,
						new ADPLocation("world", 0, 0, 0, 0, 0),
						1,
						0
				));
		
		OAPacket newPacket = OAPacket.read(plugin, packet.build());
		
		packet.setServerName("another");
		
		assertNotEquals(packet, newPacket);
	}
	
	private OreAnnouncerPlugin mockPlugin() {
		OreAnnouncerPlugin ret = mock(OreAnnouncerPlugin.class);
		when(ret.getVersion()).thenReturn(VERSION);
		return ret;
	}
	
	private OAPacket makePacket() {
		return new OAPacket(VERSION);
	}
}
