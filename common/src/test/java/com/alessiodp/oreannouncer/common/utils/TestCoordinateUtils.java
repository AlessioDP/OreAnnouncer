package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.addons.internal.JsonHandler;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.utils.CoordinateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPLocation.class,
		ConfigMain.class,
		CoordinateUtils.class,
		JsonHandler.class,
		Messages.class,
		OreAnnouncerPlugin.class
})
public class TestCoordinateUtils {
	private CoordinateUtils coordinateUtils;
	private ADPLocation location;
	private final static String COORDINATES = "%x%,%y%,%z%";
	
	@Before
	public void setUp() {
		mockStatic(ConfigMain.class);
		mockStatic(Messages.class);
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
		Assert.assertEquals(COORDINATES, coordinateUtils.replaceCoordinates(COORDINATES, location, false));
	}
	
	@Test
	public void testCoordinatesNormal() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE = false;
		
		// Test
		Assert.assertEquals("1,-2,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
	}
	
	@Test
	public void testCoordinatesObfuscate() {
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		Assert.assertEquals("&k????,-2,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		Assert.assertEquals("&k????,&k????,3", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		Assert.assertEquals("&k????,&k????,&k????", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
		
		// Test 4
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_FIXEDLENGTH = 0;
		Assert.assertEquals("&k?,&k?,&k?", coordinateUtils.replaceCoordinates(COORDINATES, location, true));
	}
	
	@Test
	public void testCoordinatesObfuscateRandomize() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 1;
		Assert.assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 1);
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 2;
		Assert.assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 2);
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 3;
		Assert.assertEquals(countObfuscations(coordinateUtils.replaceCoordinates(COORDINATES, location, true)), 3);
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
