package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.dispatchers.OASQLDispatcher;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.BlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.H2BlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.PostgreSQLBlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.SQLiteBlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound.BlocksFoundDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound.PostgreSQLBlocksFoundDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.PostgreSQLPlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.SQLitePlayersDao;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class SQLDispatcherTest {
	private static final OreAnnouncerPlugin mockPlugin = mock(OreAnnouncerPlugin.class);
	private static MockedStatic<ADPPlugin> staticPlugin;
	
	@BeforeAll
	public static void setUp(@TempDir Path tempDir) {
		ADPBootstrap mockBootstrap = mock(ADPBootstrap.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getPluginFallbackName()).thenReturn("oreannouncer");
		when(mockPlugin.getFolder()).thenReturn(Paths.get("../testing/"));
		when(mockPlugin.getBootstrap()).thenReturn(mockBootstrap);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getVersion()).thenReturn("1.0.0");
		
		// Mock debug methods
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> ClassLoader.getSystemResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		doAnswer((args) -> {
			System.out.println((String) args.getArgument(0));
			return null;
		}).when(mockLoggerManager).logDebug(anyString(), anyBoolean());
		doAnswer((args) -> {
			((Exception) args.getArgument(1)).printStackTrace();
			return null;
		}).when(mockLoggerManager).printErrorStacktrace(any(), any());
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		
		staticPlugin = mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
	}
	
	@AfterAll
	public static void tearDown() {
		staticPlugin.close();
	}
	
	private OASQLDispatcher getSQLDispatcherH2() {
		ConfigMain.STORAGE_SETTINGS_H2_DBFILE = "";
		OASQLDispatcher ret = new OASQLDispatcher(mockPlugin, StorageType.H2) {
			@Override
			public ConnectionFactory initConnectionFactory() {
				ConnectionFactory ret = super.initConnectionFactory();
				ret.setDatabaseUrl("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
				return ret;
			}
		};
		ret.init();
		return ret;
	}
	
	private OASQLDispatcher getSQLDispatcherSQLite(Path temporaryDirectory) {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "";
		OASQLDispatcher ret = new OASQLDispatcher(mockPlugin, StorageType.SQLITE) {
			@Override
			public ConnectionFactory initConnectionFactory() {
				ConnectionFactory ret = super.initConnectionFactory();
				ret.setDatabaseUrl("jdbc:sqlite:" + temporaryDirectory.resolve("database.db"));
				return ret;
			}
		};
		ret.init();
		return ret;
	}
	
	public static OASQLDispatcher getSQLDispatcherMySQL(OreAnnouncerPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "3306";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "oreannouncer";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "root";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		OASQLDispatcher ret = new OASQLDispatcher(plugin, StorageType.MYSQL);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(BlocksDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PlayersDao.class).deleteAll();
		return ret;
		 */
		return null;
	}
	
	public static OASQLDispatcher getSQLDispatcherMariaDB(OreAnnouncerPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "3306";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "oreannouncer";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "root";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		OASQLDispatcher ret = new OASQLDispatcher(plugin, StorageType.MARIADB);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(BlocksDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PlayersDao.class).deleteAll();
		return ret;
		 */
		return null;
	}
	
	public static OASQLDispatcher getSQLDispatcherPostgreSQL(OreAnnouncerPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "5432";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "oreannouncer";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "postgres";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		OASQLDispatcher ret = new OASQLDispatcher(plugin, StorageType.POSTGRESQL);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksFoundDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class).deleteAll();
		return ret;
		 */
		return null;
	}
	
	@Test
	public void testPlayer(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class));
			dispatcher.stop();
		}
	}
	
	
	private void player(OASQLDispatcher dispatcher, PlayersDao dao) {
		OAPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
		OAPlayerImpl mockPlayer = mock(player.getClass());
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mockPlugin, mock.getArgument(0)));
		
		
		player.setAccessible(true);
		player.setAlertsOn(false);
		player.setAccessible(false);
		assertEquals(dao.countAll(), 0);
		dispatcher.updatePlayer(player);
		assertEquals(dao.countAll(), 1);
		
		OAPlayerImpl newPlayer = dispatcher.getPlayer(player.getPlayerUUID());
		
		assertEquals(player, newPlayer);
		
		// Player remove
		player.setAccessible(true);
		player.setAlertsOn(true);
		player.setAccessible(false);
		dispatcher.updatePlayer(player);
		assertEquals(dao.countAll(), 0);
	}
	
	@Test
	public void testBlockFound(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksFoundDao.class));
			dispatcher.stop();
		}
	}
	
	private void blockFound(OASQLDispatcher dispatcher, BlocksFoundDao dao) {
		long time = System.currentTimeMillis() / 1000L;
		BlockFound bf = new BlockFound(
				UUID.randomUUID(),
				"testMaterial",
				time - 100,
				5
		);
		
		assertEquals(dao.countAll(), 0);
		dispatcher.insertBlockFound(bf);
		assertEquals(dao.countAll(), 1);
		dispatcher.insertBlockFound(bf);
		assertEquals(dao.countAll(), 2);
		
		bf = new BlockFound(
				bf.getPlayer(),
				bf.getMaterialName(),
				time,
				10
		);
		dispatcher.insertBlockFound(bf);
		assertEquals(dao.countAll(), 3);
		
		OABlockImpl block = new OABlockImpl(mockPlugin, bf.getMaterialName());
		
		BlocksFoundResult bfr = dispatcher.getBlockFound(bf.getPlayer(), block, time - 5);
		assertEquals(bfr.getTotal(), 10);
		
		bfr = dispatcher.getBlockFound(bf.getPlayer(), block, time - 200);
		assertEquals(bfr.getTotal(), 20);
		
		// Blocks found total
		
		// Check nulls
		assertNotNull(dispatcher.getBlockFound(bf.getPlayer(), null, time - 1000));
		assertNull(dispatcher.getBlockFound(UUID.randomUUID(), null, time - 1000));
		
		bfr = dispatcher.getBlockFound(bf.getPlayer(), null, time - 100);
		assertEquals(bfr.getTotal(), 20);
		assertEquals(bfr.getTimestamp(), time - 100);
		
		bfr = dispatcher.getBlockFound(bf.getPlayer(), null, time);
		assertEquals(bfr.getTotal(), 10);
		assertEquals(bfr.getTimestamp(), time);
	}
	
	@Test
	public void testTopPlayersDestroyed(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2BlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLiteBlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksDao.class));
			dispatcher.stop();
		}
	}
	
	private void topPlayersDestroyed(OASQLDispatcher dispatcher, BlocksDao dao) {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "MAT1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "MAT2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "MAT3");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		BlockDestroy bd1player1 = new BlockDestroy(player1, block1.getMaterialName(), 0);
		BlockDestroy bd1player1overwrite = new BlockDestroy(player1, block1.getMaterialName(), 5);
		BlockDestroy bd2player1 = new BlockDestroy(player1, block2.getMaterialName(), 5);
		BlockDestroy bd3player1 = new BlockDestroy(player1, block3.getMaterialName(), 20);
		
		BlockDestroy bd1player2 = new BlockDestroy(player2, block1.getMaterialName(), 1);
		BlockDestroy bd2player2 = new BlockDestroy(player2, block2.getMaterialName(), 5);
		BlockDestroy bd3player2 = new BlockDestroy(player2, block3.getMaterialName(), 10);
		
		BlockDestroy bd1player3 = new BlockDestroy(player3, block1.getMaterialName(), 20);
		
		assertEquals(dao.countAll(), 0);
		dispatcher.setBlockDestroy(bd1player1);
		dispatcher.setBlockDestroy(bd1player1overwrite); // Check overwrite
		dispatcher.setBlockDestroy(bd2player1);
		dispatcher.updateBlockDestroy(bd2player1); // Check sum
		dispatcher.setBlockDestroy(bd3player1);
		dispatcher.setBlockDestroy(bd1player2);
		dispatcher.updateBlockDestroy(bd2player2);
		dispatcher.updateBlockDestroy(bd3player2);
		dispatcher.updateBlockDestroy(bd1player3);
		assertEquals(dao.countAll(), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.singletonList(block2.getMaterialName());
		HashMap<UUID, Integer> players = dispatcher.getTopPlayers(OADatabaseManager.ValueType.DESTROY, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		players = dispatcher.getTopPlayers(OADatabaseManager.ValueType.DESTROY, new OABlockImpl(mockPlugin, block1.getMaterialName()), 10, 0);
		
		assertEquals(players.get(player1).intValue(), 5);
		assertEquals(players.get(player2).intValue(), 1);
		assertEquals(players.get(player3).intValue(), 20);
		
		// Counts
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.ValueType.DESTROY, null), 3);
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.ValueType.DESTROY, new OABlockImpl(mockPlugin, block3.getMaterialName())), 2);
		
		// Player position
		assertEquals(dispatcher.getTopPlayerPosition(bd1player1.getPlayer(), OADatabaseManager.ValueType.DESTROY, null), 1);
		assertEquals(dispatcher.getTopPlayerPosition(bd1player2.getPlayer(), OADatabaseManager.ValueType.DESTROY, null), 3);
		assertEquals(dispatcher.getTopPlayerPosition(bd1player3.getPlayer(), OADatabaseManager.ValueType.DESTROY, null), 2);
		assertEquals(dispatcher.getTopPlayerPosition(UUID.randomUUID(), OADatabaseManager.ValueType.DESTROY, null), 0);
		
		assertEquals(dispatcher.getTopPlayerPosition(bd1player1.getPlayer(), OADatabaseManager.ValueType.DESTROY, block2), 1);
		assertEquals(dispatcher.getTopPlayerPosition(bd1player2.getPlayer(), OADatabaseManager.ValueType.DESTROY, block2), 2);
		assertEquals(dispatcher.getTopPlayerPosition(bd1player3.getPlayer(), OADatabaseManager.ValueType.DESTROY, block2), 0);
	}
	
	@Test
	public void testTopPlayersFound(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksFoundDao.class));
			dispatcher.stop();
		}
	}
	
	private void topPlayersFound(OASQLDispatcher dispatcher, BlocksFoundDao dao) {
		long time = System.currentTimeMillis() / 1000L;
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		BlockFound bf1player1 = new BlockFound(player1, block1.getMaterialName(), time, 5);
		BlockFound bf2player1 = new BlockFound(player1, block2.getMaterialName(), time, 10);
		BlockFound bf3player1 = new BlockFound(player1, block3.getMaterialName(), time, 20);
		
		BlockFound bf1player2 = new BlockFound(player2, block1.getMaterialName(), time, 1);
		BlockFound bf2player2 = new BlockFound(player2, block2.getMaterialName(), time, 5);
		BlockFound bf3player2 = new BlockFound(player2, block3.getMaterialName(), time, 10);
		
		BlockFound bf1player3 = new BlockFound(player3, block1.getMaterialName(), time, 20);
		
		assertEquals(dao.countAll(), 0);
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf2player1);
		dispatcher.insertBlockFound(bf3player1);
		dispatcher.insertBlockFound(bf1player2);
		dispatcher.insertBlockFound(bf2player2);
		dispatcher.insertBlockFound(bf3player2);
		dispatcher.insertBlockFound(bf1player3);
		assertEquals(dao.countAll(), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.singletonList(block2.getMaterialName());
		HashMap<UUID, Integer> players = dispatcher.getTopPlayers(OADatabaseManager.ValueType.FOUND, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.emptyList();
		block2.setEnabled(false);
		players = dispatcher.getTopPlayers(OADatabaseManager.ValueType.FOUND, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		players = dispatcher.getTopPlayers(OADatabaseManager.ValueType.FOUND, block1, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 5);
		assertEquals(players.get(player2).intValue(), 1);
		assertEquals(players.get(player3).intValue(), 20);
		
		// Counts
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.ValueType.FOUND, null), 3);
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.ValueType.FOUND, block3), 2);
		
		// Player position
		assertEquals(dispatcher.getTopPlayerPosition(bf1player1.getPlayer(), OADatabaseManager.ValueType.FOUND, null), 1);
		assertEquals(dispatcher.getTopPlayerPosition(bf1player2.getPlayer(), OADatabaseManager.ValueType.FOUND, null), 3);
		assertEquals(dispatcher.getTopPlayerPosition(bf1player3.getPlayer(), OADatabaseManager.ValueType.FOUND, null), 2);
		assertEquals(dispatcher.getTopPlayerPosition(UUID.randomUUID(), OADatabaseManager.ValueType.FOUND, null), 0);
		
		assertEquals(dispatcher.getTopPlayerPosition(bf1player1.getPlayer(), OADatabaseManager.ValueType.FOUND, block2), 1);
		assertEquals(dispatcher.getTopPlayerPosition(bf1player2.getPlayer(), OADatabaseManager.ValueType.FOUND, block2), 2);
		assertEquals(dispatcher.getTopPlayerPosition(bf1player3.getPlayer(), OADatabaseManager.ValueType.FOUND, block2), 0);
	}
	
	@Test
	public void testStatsPlayerDestroyed(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2BlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLiteBlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksDao.class));
			dispatcher.stop();
		}
	}
	
	
	private void statsPlayerDestroyed(OASQLDispatcher dispatcher, BlocksDao dao) {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		BlockDestroy bd1player1 = new BlockDestroy(player1, block1.getMaterialName(), 1);
		BlockDestroy bd1player1overwrite = new BlockDestroy(player1, block1.getMaterialName(), 5);
		BlockDestroy bd2player1 = new BlockDestroy(player1, block2.getMaterialName(), 5); // This will be 10 after sum
		BlockDestroy bd3player1 = new BlockDestroy(player1, block3.getMaterialName(), 20);
		
		BlockDestroy bd1player2 = new BlockDestroy(player2, block1.getMaterialName(), 1);
		BlockDestroy bd2player2 = new BlockDestroy(player2, block2.getMaterialName(), 5);
		BlockDestroy bd3player2 = new BlockDestroy(player2, block3.getMaterialName(), 10);
		
		BlockDestroy bd1player3 = new BlockDestroy(player3, block1.getMaterialName(), 20);
		
		assertEquals(dao.countAll(), 0);
		dispatcher.setBlockDestroy(bd1player1);
		dispatcher.setBlockDestroy(bd1player1overwrite); // Check overwrite
		dispatcher.setBlockDestroy(bd2player1);
		dispatcher.updateBlockDestroy(bd2player1); // Check sum
		dispatcher.setBlockDestroy(bd3player1);
		dispatcher.setBlockDestroy(bd1player2);
		dispatcher.updateBlockDestroy(bd2player2);
		dispatcher.updateBlockDestroy(bd3player2);
		dispatcher.updateBlockDestroy(bd1player3);
		assertEquals(dao.countAll(), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.singletonList(block3.getMaterialName());
		HashMap<OABlockImpl, Integer> blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.DESTROY, player1);
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 5);
		assertNotNull(blocks.get(block2));
		assertEquals(blocks.get(block2).intValue(), 10);
		assertNull(blocks.get(block3));
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.emptyList();
		blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.DESTROY, player2);
		
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 1);
		assertNotNull(blocks.get(block2));
		assertEquals(blocks.get(block2).intValue(), 5);
		assertNotNull(blocks.get(block3));
		assertEquals(blocks.get(block3).intValue(), 10);
		
		blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.DESTROY, player3);
		
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 20);
		assertNull(blocks.get(block2));
		assertNull(blocks.get(block3));
	}
	
	@Test
	public void testStatsPlayerFound(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLBlocksFoundDao.class));
			dispatcher.stop();
		}
	}
	
	private void statsPlayerFound(OASQLDispatcher dispatcher, BlocksFoundDao dao) {
		long time = System.currentTimeMillis() / 1000L;
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		BlockFound bf1player1 = new BlockFound(player1, block1.getMaterialName(), time, 5);
		BlockFound bf2player1 = new BlockFound(player1, block2.getMaterialName(), time, 10);
		BlockFound bf3player1 = new BlockFound(player1, block3.getMaterialName(), time, 20);
		
		BlockFound bf1player2 = new BlockFound(player2, block1.getMaterialName(), time, 1);
		BlockFound bf2player2 = new BlockFound(player2, block2.getMaterialName(), time, 5);
		BlockFound bf3player2 = new BlockFound(player2, block3.getMaterialName(), time, 10);
		
		BlockFound bf1player3 = new BlockFound(player3, block1.getMaterialName(), time, 20);
		
		assertEquals(dao.countAll(), 0);
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf2player1);
		dispatcher.insertBlockFound(bf3player1);
		dispatcher.insertBlockFound(bf1player2);
		dispatcher.insertBlockFound(bf2player2);
		dispatcher.insertBlockFound(bf3player2);
		dispatcher.insertBlockFound(bf1player3);
		assertEquals(dao.countAll(), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.singletonList(block2.getMaterialName());
		HashMap<OABlockImpl, Integer> blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.FOUND, player1);
		
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 5);
		assertNull(blocks.get(block2));
		assertNotNull(blocks.get(block3));
		assertEquals(blocks.get(block3).intValue(), 20);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.emptyList();
		block2.setEnabled(false);
		blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.FOUND, player2);
		
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 1);
		assertNull(blocks.get(block2));
		assertNotNull(blocks.get(block3));
		assertEquals(blocks.get(block3).intValue(), 10);
		
		blocks = dispatcher.getStatsPlayer(OADatabaseManager.ValueType.FOUND, player3);
		
		assertNotNull(blocks.get(block1));
		assertEquals(blocks.get(block1).intValue(), 20);
		assertNull(blocks.get(block2));
		assertNull(blocks.get(block3));
	}
	
	@Test
	public void testGetLogBlocks(@TempDir Path tempDir) {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		getLogBlocks(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite(tempDir);
		getLogBlocks(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			getLogBlocks(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			getLogBlocks(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			getLogBlocks(dispatcher);
			dispatcher.stop();
		}
	}
	
	private void getLogBlocks(OASQLDispatcher dispatcher) {
		long time = System.currentTimeMillis() / 1000L;
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		OAPlayerImpl oaPlayer1 = new OAPlayerImpl(mockPlugin, player1) {};
		
		BlockFound bf1player1 = new BlockFound(player1, block1.getMaterialName(), time, 5);
		BlockFound bf2player1 = new BlockFound(player1, block2.getMaterialName(), time + 100, 10);
		BlockFound bf3player1 = new BlockFound(player1, block3.getMaterialName(), time + 200, 20);
		BlockFound bf4player1 = new BlockFound(player1, block3.getMaterialName(), time + 300, 5);
		
		BlockFound bf1player2 = new BlockFound(player2, block1.getMaterialName(), time + 1000, 1);
		BlockFound bf2player2 = new BlockFound(player2, block2.getMaterialName(), time + 1100, 5);
		BlockFound bf3player2 = new BlockFound(player2, block3.getMaterialName(), time + 1200, 10);
		BlockFound bf4player2 = new BlockFound(player2, block3.getMaterialName(), time + 1300, 5);
		
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf2player1);
		dispatcher.insertBlockFound(bf3player1);
		dispatcher.insertBlockFound(bf4player1);
		dispatcher.insertBlockFound(bf1player2);
		dispatcher.insertBlockFound(bf2player2);
		dispatcher.insertBlockFound(bf3player2);
		dispatcher.insertBlockFound(bf4player2);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_LOG = Collections.singletonList(block2.getMaterialName());
		
		List<BlockFound> blocks = dispatcher.getLogBlocks(null, null, 10,0);
		assertEquals(dispatcher.getLogBlocksNumber(null, null), 6);
		
		assertEquals(blocks.size(), 6);
		assertEquals(blocks.get(0), bf4player2);
		assertEquals(blocks.get(3), bf4player1);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_LOG = Collections.emptyList();
		blocks = dispatcher.getLogBlocks(oaPlayer1, null, 10,0);
		
		assertEquals(blocks.size(), 4);
		assertEquals(blocks.get(0), bf4player1);
		assertEquals(blocks.get(3), bf1player1);
		assertEquals(dispatcher.getLogBlocksNumber(oaPlayer1, null), 4);
		
		blocks = dispatcher.getLogBlocks(null, block3, 10,0);
		assertEquals(blocks.size(), 4);
		assertEquals(blocks.get(0), bf4player2);
		assertEquals(blocks.get(3), bf3player1);
		assertEquals(dispatcher.getLogBlocksNumber(null, block3), 4);
		
		blocks = dispatcher.getLogBlocks(oaPlayer1, block3, 10,0);
		
		assertEquals(blocks.size(), 2);
		assertEquals(blocks.get(0), bf4player1);
		assertEquals(blocks.get(1), bf3player1);
		assertEquals(dispatcher.getLogBlocksNumber(oaPlayer1, block3), 2);
		
		// Check all merged
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf1player1);
		Set<BlockFound> total = dispatcher.getAllBlockFound(oaPlayer1.getPlayerUUID());
		assertEquals(total.size(), 3);
		assertTrue(total.stream().anyMatch(bf -> bf.getMaterialName().equals(bf1player1.getMaterialName())));
		assertEquals(total.stream().filter(bf -> bf.getMaterialName().equals(bf1player1.getMaterialName())).findFirst().get().getFound(), 20);
	}
	
	public static OAPlayerImpl initializePlayer(OreAnnouncerPlugin mockPlugin, UUID uuid) {
		return new OAPlayerImpl(mockPlugin, uuid) {};
	}
}
