package com.alessiodp.oreannouncer.common.messaging;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OAPacketTest {
	private static final OreAnnouncerPlugin mockPlugin = mock(OreAnnouncerPlugin.class);
	private static final String VERSION = "1";
	
	@BeforeAll
	public static void setUp() {
		when(mockPlugin.getVersion()).thenReturn(VERSION);
	}
	
	@Test
	public void testBuildBasic() throws IOException {
		makePacket()
				.setType(OAPacket.PacketType.values()[0])
				.build();
	}
	
	@Test
	public void testBuildAdvanced() throws IOException {
		byte[] raw = ((OAPacket) makePacket()
				.setType(OAPacket.PacketType.values()[0]))
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
		OAPacket packet = (OAPacket) makePacket()
				.setType(OAPacket.PacketType.values()[0]);
		
		OAPacket newPacket = OAPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadBasicFail() throws IOException {
		OAPacket packet = (OAPacket) makePacket()
				.setType(OAPacket.PacketType.values()[0]);
		
		OAPacket newPacket = OAPacket.read(mockPlugin, packet.build());
		packet = packet.setSource("different");
		assertNotEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvanced() throws IOException {
		OAPacket packet = ((OAPacket) makePacket()
				.setType(OAPacket.PacketType.values()[0]))
				.setDataRaw(new OAPacket.Data(
						UUID.randomUUID(),
						"material",
						1,
						new ADPLocation("world", 0, 0, 0, 0, 0),
						1,
						0
				));
		
		OAPacket newPacket = OAPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvancedFail() throws IOException {
		OAPacket packet = ((OAPacket) makePacket()
				.setType(OAPacket.PacketType.values()[0]))
				.setDataRaw(new OAPacket.Data(
						UUID.randomUUID(),
						"material",
						1,
						new ADPLocation("world", 0, 0, 0, 0, 0),
						1,
						0
				));
		
		OAPacket newPacket = OAPacket.read(mockPlugin, packet.build());
		
		packet.setSource("another");
		
		assertNotEquals(packet, newPacket);
	}
	
	private OAPacket makePacket() {
		return (OAPacket) new OAPacket().setVersion(VERSION);
	}
}
