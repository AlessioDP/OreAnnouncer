package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.addons.internal.JsonHandler;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCoordinateUtils {
	private static CoordinateUtils coordinateUtils;
	private static ADPLocation location;
	private final static String COORDINATES = "%x%,%y%,%z%";
	
	@BeforeAll
	public static void setUp() {
		OreAnnouncerPlugin mockPlugin = mock(OreAnnouncerPlugin.class);
		coordinateUtils = new CoordinateUtils(mockPlugin);
		location = new ADPLocation(
				"world",
				1,
				-2,
				3,
				4,
				5
		);
		
		JsonHandler mockJsonHandler = mock(JsonHandler.class);
		when(mockPlugin.getJsonHandler()).thenReturn(mockJsonHandler);
		when(mockJsonHandler.isJson(any())).thenReturn(false);
	}
	
	@BeforeEach
	public void setUpEach() {
		// Global configuration
		ConfigMain.ALERTS_COORDINATES_ENABLE = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_FORMAT_TEXT = "&k%coordinate%";
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 3;
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_ENABLE = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_CHARACTER = "?";
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_FIXEDLENGTH = 4;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = false;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = false;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = false;
	}
	
	@Test
	public void testCoordinatesDisabled() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_ENABLE = false;
		
		// Test
		assertEquals(COORDINATES, coordinateUtils.replaceCoordinates(COORDINATES, location, false));
	}
	
	@Test
	public void testCoordinatesNormal() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE = false;
		
		// Test
		assertEquals("1,-2,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
	}
	
	@Test
	public void testCoordinatesObfuscate() {
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		assertEquals("&k????,-2,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		assertEquals("&k????,&k????,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		assertEquals("&k????,&k????,&k????", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 4
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_FIXEDLENGTH = 0;
		assertEquals("&k?,&k?,&k?", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
	}
	
	@Test
	public void testCoordinatesObfuscateRandomize() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 1;
		assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 1);
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 2;
		assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 2);
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 3;
		assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 3);
	}
	
	private int countObfuscations(String result) {
		int ret = 0;
		for (String str : result.split(",")) {
			if ("&k????".equals(str))
				ret++;
		}
		return ret;
	}
}
