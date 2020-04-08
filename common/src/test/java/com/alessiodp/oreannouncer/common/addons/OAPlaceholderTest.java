package com.alessiodp.oreannouncer.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.PlayerManager;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.mockito.internal.util.collections.Sets;
import org.powermock.core.MockRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;

/* Unstable test case. Run it manually.
@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ConfigMain.class,
		OreAnnouncerPlugin.class,
		OADatabaseManager.class,
		OAPlaceholder.class,
		PlayerManager.class
})*/
public class OAPlaceholderTest {
	private OreAnnouncerPlugin mockPlugin;
	private OADatabaseManager mockDatabaseManager;
	
	private OABlockImpl block1;
	private OABlockImpl block2;
	private OABlockImpl block3;
	
	//@Before
	public void setUp() {
		MockRepository.clear();
		mockPlugin = mock(OreAnnouncerPlugin.class);
		// Mock logger
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		
		// Mock getInstance
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		// Mock player manager
		TestPlayerManager playerManager = spy(new TestPlayerManager(mockPlugin));
		when(mockPlugin.getPlayerManager()).thenReturn(playerManager);
		
		// Mock database manager
		mockDatabaseManager = mock(OADatabaseManager.class);
		when(mockPlugin.getDatabaseManager()).thenReturn(mockDatabaseManager);
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		ConfigMain.STATS_BLACKLIST_BLOCKS_STATS = Collections.emptyList();
		
		block1 = new OABlockImpl(mockPlugin, "matA");
		block2 = new OABlockImpl(mockPlugin, "matB");
		block3 = new OABlockImpl(mockPlugin, "matC");
		Blocks.LIST = new HashMap<>();
		Blocks.LIST.put(block1.getMaterialName(), block1);
		Blocks.LIST.put(block2.getMaterialName(), block2);
		Blocks.LIST.put(block3.getMaterialName(), block3);
	}
	
	//@Test
	public void testPlaceholderPlayerDestroy() {
		OAPlayerImpl player = new OAPlayerImpl(mockPlugin, UUID.randomUUID()) {};
		
		doAnswer((mock) -> {
			if (mock.getArgument(1) != null && mock.getArgument(1) instanceof OABlockImpl) {
				if (block1.equals(mock.getArgument(1))) {
					return new BlockDestroy(player.getPlayerUUID(), block1.getMaterialName(), 10);
				} else if (block2.equals(mock.getArgument(1))) {
					return new BlockDestroy(player.getPlayerUUID(), block2.getMaterialName(), 20);
				}
			}
			return new BlockDestroy(player.getPlayerUUID(), block3.getMaterialName(), 30);
		}).when(mockDatabaseManager).getBlockDestroy(eq(player.getPlayerUUID()), any());
		
		doAnswer((mock) -> Sets.newSet(
						new BlockDestroy(player.getPlayerUUID(), block1.getMaterialName(), 20),
						new BlockDestroy(player.getPlayerUUID(), block2.getMaterialName(), 30)
				)).when(mockDatabaseManager).getAllBlockDestroy(eq(player.getPlayerUUID()));
		
		String identifier = "player_destroy";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "50");
		
		identifier = "player_destroy_" + block2.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_DESTROY_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "20");
	}
	
	//@Test
	public void testPlaceholderPlayerFound() {
		OAPlayerImpl player = new OAPlayerImpl(mockPlugin, UUID.randomUUID()) {};
		
		doAnswer((mock) -> {
			if (mock.getArgument(1) != null && mock.getArgument(1) instanceof OABlockImpl) {
				if (block1.equals(mock.getArgument(1))) {
					return new BlocksFoundResult(0, 10);
				} else if (block2.equals(mock.getArgument(1))) {
					return new BlocksFoundResult(0, 20);
				}
			}
			return new BlocksFoundResult(0, 30);
		}).when(mockDatabaseManager).getBlockFound(eq(player.getPlayerUUID()), any(), anyLong());
		
		assertEquals(mockDatabaseManager.getBlockFound(player.getPlayerUUID(), null, 0), new BlocksFoundResult(0, 30));
		
		String identifier = "player_found";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUND);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "30");
		
		identifier = "player_found_" + block2.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUND_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "20");
	}
	
	//@Test
	public void testPlaceholderPlayerFoundIn() {
		OAPlayerImpl player = new OAPlayerImpl(mockPlugin, UUID.randomUUID()) {};
		
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
		}).when(mockDatabaseManager).getBlockFound(eq(player.getPlayerUUID()), any(), anyLong());
		
		assertEquals(mockDatabaseManager.getBlockFound(player.getPlayerUUID(), null, 0), new BlocksFoundResult(0, 300));
		
		String identifier = "player_foundin_60";
		OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "200");
		
		identifier = "player_foundin_150";
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "100");
		
		identifier = "player_foundin_60_" + block1.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "500");
		
		identifier = "player_foundin_150_" + block1.getMaterialName();
		placeholder = OAPlaceholder.getPlaceholder(identifier);
		assertEquals(placeholder, OAPlaceholder.PLAYER_FOUNDIN_RANGE_BLOCK);
		assertEquals(placeholder.formatPlaceholder(player, identifier), "400");
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
