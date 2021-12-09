package com.alessiodp.oreannouncer.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class OAPlaceholderTest {
	private static final OreAnnouncerPlugin mockPlugin = mock(OreAnnouncerPlugin.class);
	private static final OADatabaseManager mockDatabaseManager = mock(OADatabaseManager.class);
	private static MockedStatic<ADPPlugin> staticPlugin;
	
	private OABlockImpl block1;
	private OABlockImpl block2;
	private OABlockImpl block3;
	private OAPlayerImpl player1;
	private OAPlayerImpl player2;
	
	@BeforeAll
	public static void setUp() {
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getDatabaseManager()).thenReturn(mockDatabaseManager);
		
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		staticPlugin = mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
	}
	
	@AfterAll
	public static void tearDown() {
		staticPlugin.close();
	}
	
	@BeforeEach
	public void setUpEach() {
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.emptyList();
		
		block1 = new OABlockImpl(mockPlugin, "matA");
		block2 = new OABlockImpl(mockPlugin, "matB");
		block3 = new OABlockImpl(mockPlugin, "matC");
		Blocks.LIST = new HashMap<>();
		Blocks.addBlock(block1);
		Blocks.addBlock(block2);
		Blocks.addBlock(block3);
		
		player1 = new TestOAPlayerImpl(mockPlugin, UUID.randomUUID());
		player2 = new TestOAPlayerImpl(mockPlugin, UUID.randomUUID());
		
		TestPlayerManager playerManager = spy(new TestPlayerManager(mockPlugin));
		when(mockPlugin.getPlayerManager()).thenReturn(playerManager);
		when(playerManager.getPlayer(any())).then(uuid -> {
			if (uuid.getArgument(0).equals(player1.getPlayerUUID()))
				return player1;
			else if (uuid.getArgument(0).equals(player2.getPlayerUUID()))
				return player2;
			return null;
		});
	}
	
	@Test
	public void testPlaceholderPlayerDestroy() {
		LinkedHashMap<OABlockImpl, Integer> statsPlayer = new LinkedHashMap<>();
		statsPlayer.put(block1, 30);
		statsPlayer.put(block2, 20);
		
		when(mockDatabaseManager.getStatsPlayer(any(), eq(player1.getPlayerUUID()))).thenReturn(statsPlayer);
		
		String identifier = "player_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "50");
		
		identifier = "player_destroy_" + block2.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
	}
	
	@Test
	public void testPlaceholderPlayerFound() {
		doAnswer((mock) -> {
			if (mock.getArgument(1) != null && mock.getArgument(1) instanceof OABlockImpl) {
				if (block1.equals(mock.getArgument(1))) {
					return new BlocksFoundResult(0, 10);
				} else if (block2.equals(mock.getArgument(1))) {
					return new BlocksFoundResult(0, 20);
				}
			}
			return new BlocksFoundResult(0, 30);
		}).when(mockDatabaseManager).getBlockFound(eq(player1.getPlayerUUID()), any(), anyLong());
		
		assertEquals(mockDatabaseManager.getBlockFound(player1.getPlayerUUID(), null, 0), new BlocksFoundResult(0, 30));
		
		String identifier = "player_found";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUND);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "30");
		
		identifier = "player_found_" + block2.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUND_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
	}
	
	@Test
	public void testPlaceholderPlayerFoundIn() {
		doAnswer((mock) -> {
			if (mock.getArgument(1) == null) {
				if ((Long) mock.getArgument(2) > 100) {
					return new BlocksFoundResult(0, 100);
				} else if ((Long) mock.getArgument(2) > 50) {
					return new BlocksFoundResult(0, 200);
				}
				return new BlocksFoundResult(0, 300);
			}
			
			if ((Long) mock.getArgument(2) > 100) {
				return new BlocksFoundResult(0, 400);
			} else if ((Long) mock.getArgument(2) > 50) {
				return new BlocksFoundResult(0, 500);
			}
			return new BlocksFoundResult(0, 600);
		}).when(mockDatabaseManager).getBlockFound(eq(player1.getPlayerUUID()), any(), anyLong());
		
		assertEquals(mockDatabaseManager.getBlockFound(player1.getPlayerUUID(), null, 0), new BlocksFoundResult(0, 300));
		
		String identifier = "player_foundin_60";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "200");
		
		identifier = "player_foundin_150";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "100");
		
		identifier = "player_foundin_60_" + block1.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "500");
		
		identifier = "player_foundin_150_" + block1.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "400");
	}
	
	@Test
	public void testPlaceholderTopPlayerDestroy() {
		LinkedHashMap<OABlockImpl, Integer> statsPlayer = new LinkedHashMap<>();
		statsPlayer.put(block1, 30);
		statsPlayer.put(block2, 20);
		
		when(mockDatabaseManager.getStatsPlayer(any(), eq(player1.getPlayerUUID()))).thenReturn(statsPlayer);
		
		String identifier = "player_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "50");
		
		identifier = "player_destroy_" + block2.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
	}
	
	@Test
	public void testPlaceholderTopPlayerByDestroy() {
		LinkedHashMap<UUID, Integer> res = new LinkedHashMap<>();
		res.put(player1.getPlayerUUID(), 20);
		when(mockDatabaseManager.getTopPlayers(eq(OADatabaseManager.ValueType.DESTROY), any(), anyInt(), eq(0))).thenReturn(res);
		LinkedHashMap<UUID, Integer> res2 = new LinkedHashMap<>();
		res2.put(player2.getPlayerUUID(), 10);
		when(mockDatabaseManager.getTopPlayers(eq(OADatabaseManager.ValueType.DESTROY), any(), anyInt(), eq(1))).thenReturn(res2);
		
		String identifier = "top_player_by_destroy_1_player_id";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_DESTROY_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player1.getPlayerUUID().toString());
		
		identifier = "top_player_by_destroy_" + block2.getMaterialName() + "_1_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player1.getPlayerUUID().toString());
		
		identifier = "top_player_by_destroy_2_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_DESTROY_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player2.getPlayerUUID().toString());
		
		identifier = "top_player_by_destroy_" + block2.getMaterialName() + "_2_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_DESTROY_BLOCK_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player2.getPlayerUUID().toString());
	}
	
	@Test
	public void testPlaceholderTopPlayerByFound() {
		LinkedHashMap<UUID, Integer> res = new LinkedHashMap<>();
		res.put(player1.getPlayerUUID(), 20);
		when(mockDatabaseManager.getTopPlayers(eq(OADatabaseManager.ValueType.FOUND), any(), anyInt(), eq(0))).thenReturn(res);
		LinkedHashMap<UUID, Integer> res2 = new LinkedHashMap<>();
		res2.put(player2.getPlayerUUID(), 10);
		when(mockDatabaseManager.getTopPlayers(eq(OADatabaseManager.ValueType.FOUND), any(), anyInt(), eq(1))).thenReturn(res2);
		
		String identifier = "top_player_by_found_1_player_id";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_FOUND_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player1.getPlayerUUID().toString());
		
		identifier = "top_player_by_found_" + block2.getMaterialName() + "_1_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_FOUND_BLOCK_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player1.getPlayerUUID().toString());
		
		identifier = "top_player_by_found_2_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_FOUND_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player2.getPlayerUUID().toString());
		
		identifier = "top_player_by_found_" + block2.getMaterialName() + "_2_player_id";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYER_BY_FOUND_BLOCK_NUMBER_PLACEHOLDER);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), player2.getPlayerUUID().toString());
	}
	
	@Test
	public void testPlaceholderTopPlayersTotalByDestroy() {
		when(mockDatabaseManager.getTopPlayersNumber(eq(OADatabaseManager.ValueType.DESTROY), isNull())).thenReturn(20);
		when(mockDatabaseManager.getTopPlayersNumber(eq(OADatabaseManager.ValueType.DESTROY), eq(block1))).thenReturn(10);
		
		String identifier = "top_players_total_by_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYERS_TOTAL_BY_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
		
		identifier = "top_players_total_" + block1.getMaterialName() + "_by_destroy";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYERS_TOTAL_BLOCK_BY_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "10");
	}
	
	@Test
	public void testPlaceholderTopPlayersTotalByFound() {
		when(mockDatabaseManager.getTopPlayersNumber(eq(OADatabaseManager.ValueType.FOUND), isNull())).thenReturn(20);
		when(mockDatabaseManager.getTopPlayersNumber(eq(OADatabaseManager.ValueType.FOUND), eq(block1))).thenReturn(10);
		
		String identifier = "top_players_total_by_found";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYERS_TOTAL_BY_FOUND);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
		
		identifier = "top_players_total_" + block1.getMaterialName() + "_by_found";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOP_PLAYERS_TOTAL_BLOCK_BY_FOUND);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "10");
	}
	
	@Test
	public void testPlaceholderPlayerTopByDestroy() {
		when(mockDatabaseManager.getTopPlayerPosition(eq(player1.getPlayerUUID()), eq(OADatabaseManager.ValueType.DESTROY), isNull())).thenReturn(1);
		when(mockDatabaseManager.getTopPlayerPosition(eq(player1.getPlayerUUID()), eq(OADatabaseManager.ValueType.DESTROY), eq(block1))).thenReturn(2);
		
		String identifier = "player_top_by_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_TOP_BY_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "1");
		
		identifier = "player_top_by_destroy_" + block1.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_TOP_BY_DESTROY_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "2");
	}
	
	@Test
	public void testPlaceholderTotalByDestroy() {
		when(mockDatabaseManager.getTotalDestroy(isNull())).thenReturn(20);
		when(mockDatabaseManager.getTotalDestroy(eq(block1))).thenReturn(10);
		
		String identifier = "total_by_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOTAL_BY_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
		
		identifier = "total_" + block1.getMaterialName() + "_by_destroy";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOTAL_BLOCK_BY_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "10");
	}
	
	@Test
	public void testPlaceholderTotalByFound() {
		when(mockDatabaseManager.getTotalFound(isNull())).thenReturn(20);
		when(mockDatabaseManager.getTotalFound(eq(block1))).thenReturn(10);
		
		String identifier = "total_by_found";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOTAL_BY_FOUND);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "20");
		
		identifier = "total_" + block1.getMaterialName() + "_by_found";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.TOTAL_BLOCK_BY_FOUND);
		assertEquals(placeholder.formatPlaceholder(player1, identifier), "10");
	}
	
	private static class TestOAPlayerImpl extends OAPlayerImpl {
		
		protected TestOAPlayerImpl(OreAnnouncerPlugin plugin, UUID uuid) {
			super(plugin, uuid);
		}
	}
	
	private static class TestPlayerManager extends PlayerManager {
		
		public TestPlayerManager(OreAnnouncerPlugin plugin) {
			super(plugin);
		}
		
		@Override
		public OAPlayerImpl initializePlayer(UUID playerUUID) {
			return null;
		}
	}
}
