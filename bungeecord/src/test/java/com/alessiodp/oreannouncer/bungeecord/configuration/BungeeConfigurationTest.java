package com.alessiodp.oreannouncer.bungeecord.configuration;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.file.YamlFile;
import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		OreAnnouncerPlugin.class,
		ConfigurationFile.class,
		BungeeConfigMain.class,
		BungeeMessages.class
})
public class BungeeConfigurationTest {
	private OreAnnouncerPlugin mockPlugin;
	
	@Before
	public void setUp() {
		mockPlugin = mock(OreAnnouncerPlugin.class);
	}
	
	@Test
	public void testConfigMain() throws IllegalAccessException {
		BungeeConfigMain configMain = new BungeeConfigMain(mockPlugin);
		
		List<String> skipPaths = Arrays.asList(
				"alerts.sound",
				"alerts.coordinates",
				"blocks.bypass",
				"blocks.warn",
				"blocks.light-level",
				"blocks.height-level",
				"blocks.tnt",
				"stats.blacklist",
				"stats.advanced-count.default",
				"execute-commands"
		);
		
		testConfiguration(configMain, skipPaths);
	}
	
	@Test
	public void testMessages() throws IllegalAccessException {
		BungeeMessages messages = new BungeeMessages(mockPlugin);
		
		List<String> skipPaths = Collections.singletonList(
				"alerts."
		);
		
		testConfiguration(messages, skipPaths);
	}
	
	private void testConfiguration(ConfigurationFile configurationFile, List<String> skipPaths) throws IllegalAccessException {
		Field[] fields = PowerMockito.fields(configurationFile.getClass());
		
		// Initialize YAML
		YamlFile yf = YamlFile.loadConfiguration(new InputStreamReader(getClass().getResourceAsStream("/" + configurationFile.getResourceName())));
		
		// Check fields
		for (Field f : fields) {
			ConfigOption co = f.getAnnotation(ConfigOption.class);
			if (co != null && !skippablePath(co.path(), skipPaths)) {
				Object value = yf.get(co.path());
				Assert.assertNotNull("The " + configurationFile.getClass().getSimpleName() + " path '" + co.path() + "' is null.", value);
				f.set(null, value);
			}
		}
	}
	
	private boolean skippablePath(String path, List<String> skipPaths) {
		for (String sp : skipPaths) {
			if (path.startsWith(sp))
				return true;
		}
		return false;
	}
}
