package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.migrations.Migrator;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.storage.dispatchers.OASQLDispatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.alessiodp.oreannouncer.common.jpa.Tables.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class,
		ADPBootstrap.class,
		ConfigMain.class,
		LoggerManager.class,
		Migrator.class,
		OASQLDispatcher.class,
		OreAnnouncerPlugin.class
})
public class MigrationsTest {
	private OreAnnouncerPlugin mockPlugin;
	
	@Before
	public void setUp() {
		System.getProperties().setProperty("org.jooq.no-logo", "TRUE");
		mockPlugin = mock(OreAnnouncerPlugin.class);
		ADPBootstrap mockBootstrap = mock(ADPBootstrap.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getPluginFallbackName()).thenReturn("oreannouncer");
		when(mockPlugin.getFolder()).thenReturn(Paths.get("../testing/"));
		when(mockPlugin.getBootstrap()).thenReturn(mockBootstrap);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> getClass().getClassLoader().getResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "oreannouncer_";
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_CHARSET = "UTF-8";
	}
	
	@Test
	public void testDatabase2_4_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_4_X.db_temp";
		Files.copy(
				mockPlugin.getFolder().resolve("database_2_4_X.db"),
				mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE),
				StandardCopyOption.REPLACE_EXISTING
		);
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		assertTrue(dispatcher
				.getDatabase()
				.getQueryBuilder()
				.fetchExists(PLAYERS));
		assertTrue(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS));
		assertTrue(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS_FOUND));
		
		dispatcher.stop();
		Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
	}
	
	@Test
	public void testDatabase2_5_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_5_X.db_temp";
		Files.copy(
				mockPlugin.getFolder().resolve("database_2_5_X.db"),
				mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE),
				StandardCopyOption.REPLACE_EXISTING
				);
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		assertTrue(dispatcher.getDatabase().getQueryBuilder().fetchExists(PLAYERS));
		assertTrue(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS));
		assertTrue(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS_FOUND));
		
		dispatcher.stop();
		Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
	}
	
	@Test
	public void testDatabaseFresh() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_new.db_temp";
		try {
			Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
		} catch (Exception ignored) {}
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		assertFalse(dispatcher.getDatabase().getQueryBuilder().fetchExists(PLAYERS));
		assertFalse(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS));
		assertFalse(dispatcher.getDatabase().getQueryBuilder().fetchExists(BLOCKS_FOUND));
		
		dispatcher.stop();
		Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
	}
}
