package com.alessiodp.oreannouncer.common;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.utils.CoordinateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPLocation.class,
		ConfigMain.class,
		CoordinateUtils.class,
		Messages.class,
		OreAnnouncerPlugin.class
})
public class TestCoordinateUtils {
	private CoordinateUtils coordinateUtils;
	private ADPLocation location;
	
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
		
		// Global configuration
		ConfigMain.ALERTS_COORDINATES_ENABLE = true;
		ConfigMain.ALERTS_COORDINATES_FORMAT = "%x%,%y%,%z%";
		ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_FORMAT = "&k%coordinate%";
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 3;
		ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATE = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_FIXEDLENGTH = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = false;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = false;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = false;
	}
	
	@Test
	public void testCoordinatesDisabled() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_ENABLE = false;
		
		// Test
		Assert.assertEquals("", coordinateUtils.calculate(location, false));
	}
	
	@Test
	public void testCoordinatesNormal() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE = false;
		
		// Test
		Assert.assertEquals("1,-2,3", coordinateUtils.calculate(location, true));
	}
	
	@Test
	public void testCoordinatesObfuscate() {
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		Assert.assertEquals("&k0000,-2,3", coordinateUtils.calculate(location, true));
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		Assert.assertEquals("&k0000,&k0000,3", coordinateUtils.calculate(location, true));
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		Assert.assertEquals("&k0000,&k0000,&k0000", coordinateUtils.calculate(location, true));
		
		// Test 4
		ConfigMain.ALERTS_COORDINATES_HIDE_FIXEDLENGTH = false;
		Assert.assertEquals("&k0,&k0,&k0", coordinateUtils.calculate(location, true));
	}
	
	@Test
	public void testCoordinatesObfuscateRandomize() {
		// Configuration
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y = true;
		ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z = true;
		
		// Test 1
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 1;
		Assert.assertEquals(countObfuscations(coordinateUtils.calculate(location, true)), 1);
		
		// Test 2
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 2;
		Assert.assertEquals(countObfuscations(coordinateUtils.calculate(location, true)), 2);
		
		// Test 3
		ConfigMain.ALERTS_COORDINATES_HIDE_COUNT = 3;
		Assert.assertEquals(countObfuscations(coordinateUtils.calculate(location, true)), 3);
	}
	
	private int countObfuscations(String result) {
		int ret = 0;
		for (String str : result.split(",")) {
			if (str.equals("&k0000"))
				ret++;
		}
		return ret;
	}
}
