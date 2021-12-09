package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.dao.SchemaHistorySQLiteDao;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.storage.dispatchers.OASQLDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MigrationsTest {
	private static final OreAnnouncerPlugin mockPlugin = mock(OreAnnouncerPlugin.class);
	private final Path testingPath = Paths.get("../testing/");
	
	@BeforeAll
	public static void setUp(@TempDir Path tempDir) {
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getPluginFallbackName()).thenReturn("oreannouncer");
		when(mockPlugin.getFolder()).thenReturn(tempDir);
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> ClassLoader.getSystemResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		
		// Mock managers for player/party initialization
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> SQLDispatcherTest.initializePlayer(mockPlugin, mock.getArgument(0)));
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
	}
	
	private void prepareDatabase(String database) throws IOException {
		Files.copy(
				testingPath.resolve(database),
				mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE),
				StandardCopyOption.REPLACE_EXISTING
		);
	}
	
	
	@Test
	public void testDatabase2_4_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_4_X.db";
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
		
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>players");
			handle.execute("SELECT 1 FROM <prefix>blocks");
			handle.execute("SELECT 1 FROM <prefix>blocks_found");
		});
		
		int schemaHistoryCount = dispatcher.getConnectionFactory().getJdbi().withHandle(handle -> handle.attach(SchemaHistorySQLiteDao.class).countVersions());
		assertEquals(schemaHistoryCount, 2);
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabase2_5_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_5_X.db";
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
		
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>players");
			handle.execute("SELECT 1 FROM <prefix>blocks");
			handle.execute("SELECT 1 FROM <prefix>blocks_found");
		});
		
		int schemaHistoryCount = dispatcher.getConnectionFactory().getJdbi().withHandle(handle -> handle.attach(SchemaHistorySQLiteDao.class).countVersions());
		assertEquals(schemaHistoryCount, 2);
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabase2_8_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_8_X.db";
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
		
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>players");
			handle.execute("SELECT 1 FROM <prefix>blocks");
			handle.execute("SELECT 1 FROM <prefix>blocks_found");
		});
		
		int schemaHistoryCount = dispatcher.getConnectionFactory().getJdbi().withHandle(handle -> handle.attach(SchemaHistorySQLiteDao.class).countVersions());
		assertEquals(schemaHistoryCount, 2);
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshSQLite() {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_sqlite.db";
		
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>players");
			handle.execute("SELECT 1 FROM <prefix>blocks");
			handle.execute("SELECT 1 FROM <prefix>blocks_found");
		});
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshH2() {
		ConfigMain.STORAGE_SETTINGS_H2_DBFILE = "database_h2.db";
		
		OASQLDispatcher dispatcher = new OASQLDispatcher(mockPlugin, StorageType.H2);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>players");
			handle.execute("SELECT 1 FROM <prefix>blocks");
			handle.execute("SELECT 1 FROM <prefix>blocks_found");
		});
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshMySQL() {
		OASQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>players");
				handle.execute("SELECT 1 FROM <prefix>blocks");
				handle.execute("SELECT 1 FROM <prefix>blocks_found");
			});
			
			dispatcher.stop();
		}
	}
	
	@Test
	public void testDatabaseFreshMariaDB() {
		OASQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>players");
				handle.execute("SELECT 1 FROM <prefix>blocks");
				handle.execute("SELECT 1 FROM <prefix>blocks_found");
			});
			
			dispatcher.stop();
		}
	}
	
	@Test
	public void testDatabaseFreshPostgreSQL() {
		OASQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>players");
				handle.execute("SELECT 1 FROM <prefix>blocks");
				handle.execute("SELECT 1 FROM <prefix>blocks_found");
			});
			
			dispatcher.stop();
		}
	}
}