package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.storage.sql.migrator.Migrator;
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
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.SQLiteBlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound.BlocksFoundDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.SQLitePlayersDao;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class,
		ADPBootstrap.class,
		Blocks.class,
		BlocksFoundResult.class,
		ConfigMain.class,
		LoggerManager.class,
		Migrator.class,
		OASQLDispatcher.class,
		OfflineUser.class,
		OreAnnouncerPlugin.class,
		PlayerManager.class
})
public class SQLDispatcherTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	private OreAnnouncerPlugin mockPlugin;
	
	@Before
	public void setUp() {
		mockPlugin = mock(OreAnnouncerPlugin.class);
		ADPBootstrap mockBootstrap = mock(ADPBootstrap.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getPluginFallbackName()).thenReturn("oreannouncer");
		when(mockPlugin.getFolder()).thenReturn(Paths.get("../testing/"));
		when(mockPlugin.getBootstrap()).thenReturn(mockBootstrap);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getVersion()).thenReturn("1.0.0");
		
		// Mock static ADPPlugin, used in DAOs
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		// Mock debug methods
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> getClass().getClassLoader().getResourceAsStream(mock.getArgument(0)));
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
	
	private OASQLDispatcher getSQLDispatcherSQLite() {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "";
		OASQLDispatcher ret = new OASQLDispatcher(mockPlugin, StorageType.SQLITE) {
			@Override
			public ConnectionFactory initConnectionFactory() {
				ConnectionFactory ret = super.initConnectionFactory();
				try {
					ret.setDatabaseUrl("jdbc:sqlite:" + testFolder.newFile("database.db").toPath().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ret;
			}
		};
		ret.init();
		return ret;
	}
	
	@Test
	public void testPlayer() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class));
		dispatcher.stop();
	}
	
	
	private void player(OASQLDispatcher dispatcher, PlayersDao dao) {
		OAPlayerImpl player = new OAPlayerImpl(mockPlugin, UUID.randomUUID()) {};
		OAPlayerImpl mockPlayer = mock(player.getClass());
		doNothing().when(mockPlayer).updatePlayer();
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) ->
				new OAPlayerImpl(mockPlugin, mock.getArgument(0)) {}
		);
		
		
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
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> handle.execute("DROP TABLE `<prefix>players`"));
	}
	
	@Test
	public void testBlockFound() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		blockFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
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
	public void testTopPlayersDestroyed() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2BlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		topPlayersDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLiteBlocksDao.class));
		dispatcher.stop();
	}
	
	private void topPlayersDestroyed(OASQLDispatcher dispatcher, BlocksDao dao) {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
		
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
	}
	
	@Test
	public void testTopPlayersFound() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		topPlayersFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
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
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
		
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
	}
	
	@Test
	public void testStatsPlayerDestroyed() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2BlocksDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		statsPlayerDestroyed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLiteBlocksDao.class));
		dispatcher.stop();
	}
	
	private void statsPlayerDestroyed(OASQLDispatcher dispatcher, BlocksDao dao) {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID player3 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
		
		BlockDestroy bd1player1 = new BlockDestroy(player1, block1.getMaterialName(), 0);
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
	public void testStatsPlayerFound() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		statsPlayerFound(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(BlocksFoundDao.class));
		dispatcher.stop();
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
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
		
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
	public void testGetLogBlocks() {
		OASQLDispatcher dispatcher = getSQLDispatcherH2();
		getLogBlocks(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		getLogBlocks(dispatcher);
		dispatcher.stop();
	}
	
	private void getLogBlocks(OASQLDispatcher dispatcher) {
		long time = System.currentTimeMillis() / 1000L;
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		
		OABlockImpl block1 = new OABlockImpl(mockPlugin, "mat1");
		OABlockImpl block2 = new OABlockImpl(mockPlugin, "mat2");
		OABlockImpl block3 = new OABlockImpl(mockPlugin, "mat3");
		Blocks.LIST = new HashMap<>();
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
		
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
}
