package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.migrations.Migrator;
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
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static com.alessiodp.oreannouncer.common.jpa.Tables.BLOCKS;
import static com.alessiodp.oreannouncer.common.jpa.Tables.BLOCKS_FOUND;
import static com.alessiodp.oreannouncer.common.jpa.Tables.PLAYERS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
	private OreAnnouncerPlugin mockPlugin;
	private OASQLDispatcher dispatcher;
	
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
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		doAnswer((args) -> {
			((Exception) args.getArgument(1)).printStackTrace();
			return null;
		}).when(mockLoggerManager).printErrorStacktrace(any(), any());
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "oreannouncer_";
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_CHARSET = "UTF-8";
		
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_test.db";
		try {
			Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
		} catch (Exception ignored) {}
		dispatcher = new OASQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
	}
	
	@After
	public void tearDown() throws IOException {
		dispatcher.stop();
		Files.delete(mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
	}
	
	@Test
	public void testPlayer() {
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
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(PLAYERS), 0);
		dispatcher.updatePlayer(player);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(PLAYERS), 1);
		
		OAPlayerImpl newPlayer = dispatcher.getPlayer(player.getPlayerUUID());
		
		assertEquals(player, newPlayer);
		
		// Player remove
		player.setAccessible(true);
		player.setAlertsOn(true);
		player.setAccessible(false);
		dispatcher.updatePlayer(player);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(PLAYERS), 0);
	}
	
	@Test
	public void testTopPlayersDestroyed() {
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
		
		BlockDestroy bd1player1 = new BlockDestroy(player1, block1.getMaterialName(), 5);
		BlockDestroy bd2player1 = new BlockDestroy(player1, block2.getMaterialName(), 10);
		BlockDestroy bd3player1 = new BlockDestroy(player1, block3.getMaterialName(), 20);
		
		BlockDestroy bd1player2 = new BlockDestroy(player2, block1.getMaterialName(), 1);
		BlockDestroy bd2player2 = new BlockDestroy(player2, block2.getMaterialName(), 5);
		BlockDestroy bd3player2 = new BlockDestroy(player2, block3.getMaterialName(), 10);
		
		BlockDestroy bd1player3 = new BlockDestroy(player3, block1.getMaterialName(), 20);
		
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS), 0);
		dispatcher.updateBlockDestroy(bd1player1);
		dispatcher.updateBlockDestroy(bd2player1);
		dispatcher.updateBlockDestroy(bd3player1);
		dispatcher.updateBlockDestroy(bd1player2);
		dispatcher.updateBlockDestroy(bd2player2);
		dispatcher.updateBlockDestroy(bd3player2);
		dispatcher.updateBlockDestroy(bd1player3);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.singletonList(block2.getMaterialName());
		HashMap<UUID, Integer> players = dispatcher.getTopPlayers(OADatabaseManager.TopOrderBy.DESTROY, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		players = dispatcher.getTopPlayers(OADatabaseManager.TopOrderBy.DESTROY, new OABlockImpl(mockPlugin, block1.getMaterialName()), 10, 0);
		
		assertEquals(players.get(player1).intValue(), 5);
		assertEquals(players.get(player2).intValue(), 1);
		assertEquals(players.get(player3).intValue(), 20);
		
		// Counts
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.TopOrderBy.DESTROY, null), 3);
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.TopOrderBy.DESTROY, new OABlockImpl(mockPlugin, block3.getMaterialName())), 2);
	}
	
	@Test
	public void testBlockFound() {
		long time = System.currentTimeMillis() / 1000L;
		BlockFound bf = new BlockFound(
				UUID.randomUUID(),
				"testMaterial",
				time - 100,
				5
		);
		
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 0);
		dispatcher.insertBlockFound(bf);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 1);
		dispatcher.insertBlockFound(bf);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 2);
		
		bf = new BlockFound(
				bf.getPlayer(),
				bf.getMaterialName(),
				time,
				10
		);
		dispatcher.insertBlockFound(bf);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 3);
		
		OABlockImpl block = new OABlockImpl(mockPlugin, bf.getMaterialName());
		
		BlocksFoundResult bfr = dispatcher.getBlockFound(bf.getPlayer(), block, time - 5);
		assertEquals(bfr.getTotal(), 10);
		
		bfr = dispatcher.getBlockFound(bf.getPlayer(), block, time - 200);
		assertEquals(bfr.getTotal(), 20);
	}
	
	@Test
	public void testTopPlayersFound() {
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
		
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 0);
		dispatcher.insertBlockFound(bf1player1);
		dispatcher.insertBlockFound(bf2player1);
		dispatcher.insertBlockFound(bf3player1);
		dispatcher.insertBlockFound(bf1player2);
		dispatcher.insertBlockFound(bf2player2);
		dispatcher.insertBlockFound(bf3player2);
		dispatcher.insertBlockFound(bf1player3);
		assertEquals(dispatcher.getDatabase().getQueryBuilder().fetchCount(BLOCKS_FOUND), 7);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.singletonList(block2.getMaterialName());
		HashMap<UUID, Integer> players = dispatcher.getTopPlayers(OADatabaseManager.TopOrderBy.FOUND, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_TOP = Collections.emptyList();
		block2.setEnabled(false);
		players = dispatcher.getTopPlayers(OADatabaseManager.TopOrderBy.FOUND, null, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 25);
		assertEquals(players.get(player2).intValue(), 11);
		assertEquals(players.get(player3).intValue(), 20);
		
		players = dispatcher.getTopPlayers(OADatabaseManager.TopOrderBy.FOUND, block1, 10, 0);
		
		assertEquals(players.get(player1).intValue(), 5);
		assertEquals(players.get(player2).intValue(), 1);
		assertEquals(players.get(player3).intValue(), 20);
		
		// Counts
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.TopOrderBy.FOUND, null), 3);
		assertEquals(dispatcher.getTopPlayersNumber(OADatabaseManager.TopOrderBy.FOUND, block3), 2);
	}
	
	@Test
	public void testGetLogBlocks() {
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
		LinkedList<BlockFound> blocks = dispatcher.getLogBlocks(null, null, 10,0);
		assertEquals(dispatcher.getLogBlocksNumber(null, null), 6);
		
		assertEquals(blocks.size(), 6);
		assertEquals(blocks.get(0), bf4player2);
		assertEquals(blocks.get(3), bf4player1);
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_LOG = Collections.emptyList();
		blocks = dispatcher.getLogBlocks(oaPlayer1, null, 10,0);
		assertEquals(dispatcher.getLogBlocksNumber(oaPlayer1, null), 4);
		
		assertEquals(blocks.size(), 4);
		assertEquals(blocks.get(0), bf4player1);
		assertEquals(blocks.get(3), bf1player1);
		
		blocks = dispatcher.getLogBlocks(null, block3, 10,0);
		assertEquals(dispatcher.getLogBlocksNumber(null, block3), 4);
		
		assertEquals(blocks.size(), 4);
		assertEquals(blocks.get(0), bf4player2);
		assertEquals(blocks.get(3), bf3player1);
		
		blocks = dispatcher.getLogBlocks(oaPlayer1, block3, 10,0);
		assertEquals(dispatcher.getLogBlocksNumber(oaPlayer1, block3), 2);
		
		assertEquals(blocks.size(), 2);
		assertEquals(blocks.get(0), bf4player1);
		assertEquals(blocks.get(1), bf3player1);
	}
}
