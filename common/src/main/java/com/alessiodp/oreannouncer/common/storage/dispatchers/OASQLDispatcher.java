package com.alessiodp.oreannouncer.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.H2ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.MySQLConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.SQLiteConnectionFactory;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.storage.interfaces.IOADatabase;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.BlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.H2BlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocks.SQLiteBlocksDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound.BlocksFoundDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.oreannouncer.common.storage.sql.dao.players.SQLitePlayersDao;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OASQLDispatcher extends SQLDispatcher implements IOADatabase, IDatabaseDispatcher {
	
	protected Class<? extends PlayersDao> playersDao = PlayersDao.class;
	protected Class<? extends BlocksDao> blocksDao = BlocksDao.class;
	protected Class<? extends BlocksFoundDao> blocksFoundDao = BlocksFoundDao.class;
	
	public OASQLDispatcher(ADPPlugin plugin, StorageType storageType) {
		super(plugin, storageType);
	}
	
	@Override
	public ConnectionFactory initConnectionFactory() {
		ConnectionFactory ret = null;
		switch (storageType) {
			case MYSQL:
				ret = new MySQLConnectionFactory();
				((MySQLConnectionFactory) ret).setCharset(ConfigMain.STORAGE_SETTINGS_MYSQL_CHARSET);
				((MySQLConnectionFactory) ret).setServerName(ConfigMain.STORAGE_SETTINGS_MYSQL_ADDRESS);
				((MySQLConnectionFactory) ret).setPort(ConfigMain.STORAGE_SETTINGS_MYSQL_PORT);
				((MySQLConnectionFactory) ret).setDatabaseName(ConfigMain.STORAGE_SETTINGS_MYSQL_DATABASE);
				((MySQLConnectionFactory) ret).setUsername(ConfigMain.STORAGE_SETTINGS_MYSQL_USERNAME);
				((MySQLConnectionFactory) ret).setPassword(ConfigMain.STORAGE_SETTINGS_MYSQL_PASSWORD);
				((MySQLConnectionFactory) ret).setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_MYSQL_POOLSIZE);
				((MySQLConnectionFactory) ret).setMaxLifetime(ConfigMain.STORAGE_SETTINGS_MYSQL_CONNLIFETIME);
				((MySQLConnectionFactory) ret).setUseSSL(ConfigMain.STORAGE_SETTINGS_MYSQL_USESSL);
				break;
			case SQLITE:
				ret = new SQLiteConnectionFactory(plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
				((SQLiteConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				playersDao = SQLitePlayersDao.class;
				blocksDao = SQLiteBlocksDao.class;
				break;
			case H2:
				ret = new H2ConnectionFactory(plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_H2_DBFILE));
				((H2ConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				playersDao = H2PlayersDao.class;
				blocksDao = H2BlocksDao.class;
				break;
			default:
				// Unsupported storage type
		}
		return ret;
	}
	
	@Override
	public void updatePlayer(OAPlayerImpl player) {
		if (!player.haveAlertsOn()) {
			this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(playersDao).insert(player.getPlayerUUID().toString(), player.haveAlertsOn()));
		} else {
			this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(playersDao).remove(player.getPlayerUUID().toString()));
		}
	}
	
	@Override
	public OAPlayerImpl getPlayer(UUID playerUuid) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).getPlayer(playerUuid.toString())).orElse(null);
	}
	
	@Override
	public void updateBlockDestroy(BlockDestroy blockDestroy) {
		this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(blocksDao).update(
				blockDestroy.getPlayer().toString(),
				blockDestroy.getMaterialName(),
				blockDestroy.getDestroyCount()
		));
	}
	
	@Override
	public void setBlockDestroy(BlockDestroy blockDestroy) {
		this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(blocksDao).set(
				blockDestroy.getPlayer().toString(),
				blockDestroy.getMaterialName(),
				blockDestroy.getDestroyCount()
		));
	}
	
	@Override
	public BlockDestroy getBlockDestroy(UUID player, OABlock block) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksDao).get(player.toString(), block.getMaterialName())).orElse(null);
	}
	
	@Override
	public Set<BlockDestroy> getAllBlockDestroy(UUID player) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksDao).getAll(player.toString()));
	}
	
	@Override
	public void insertBlockFound(BlockFound blockFound) {
		this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(blocksFoundDao).insert(
				blockFound.getPlayer().toString(),
				blockFound.getMaterialName(),
				blockFound.getTimestamp(),
				blockFound.getFound()
		));
	}
	
	@Override
	public BlocksFoundResult getBlockFound(UUID player, OABlock block, long sinceTimestamp) {
		if (block != null) {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getLatestBlock(
					player.toString(),
					sinceTimestamp,
					block.getMaterialName()
			)).orElse(null);
		} else {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getLatestTotal(
					player.toString(),
					sinceTimestamp
			)).orElse(null);
		}
	}
	
	@Override
	public List<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset) {
		List<String> materials = getEnabledMaterials(block, ConfigMain.STATS_BLACKLIST_BLOCKS_LOG);
		
		if (player != null) {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getAll(
					materials,
					player.getPlayerUUID().toString(),
					limit,
					offset
			));
		} else {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getAll(
					materials,
					limit,
					offset
			));
		}
	}
	
	@Override
	public int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block) {
		List<String> materials = getEnabledMaterials(block, ConfigMain.STATS_BLACKLIST_BLOCKS_LOG);
		
		if (player != null) {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getAllNumber(
					materials,
					player.getPlayerUUID().toString()
			));
		} else {
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getAllNumber(
					materials
			));
		}
	}
	
	public Set<BlockFound> getAllBlockFound(UUID player) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getAllMerged(player.toString()));
	}
	
	@Override
	public LinkedHashMap<UUID, Integer> getTopPlayers(OADatabaseManager.ValueType orderBy, @Nullable OABlockImpl block, int limit, int offset) {
		List<String> materials = getEnabledMaterials(block, ConfigMain.STATS_BLACKLIST_BLOCKS_TOP);
		List<Pair<String, Integer>> result;
		if (orderBy.equals(OADatabaseManager.ValueType.FOUND)) {
			// Found order
			result = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getTopPlayers(materials, limit, offset));
			
		} else {
			// Destroy order
			result = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksDao).getTopPlayers(materials, limit, offset));
		}
		
		LinkedHashMap<UUID, Integer> ret = new LinkedHashMap<>();
		for (Pair<String, Integer> pair : result) {
			ret.put(UUID.fromString(pair.getKey()), pair.getValue());
		}
		return ret;
	}
	
	@Override
	public int getTopPlayersNumber(OADatabaseManager.ValueType orderBy, @Nullable OABlockImpl block) {
		List<String> materials = getEnabledMaterials(block, ConfigMain.STATS_BLACKLIST_BLOCKS_TOP);
		
		if (orderBy.equals(OADatabaseManager.ValueType.FOUND)) {
			// Found order
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getTopPlayersNumber(materials));
			
		} else {
			// Destroy order
			return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksDao).getTopPlayersNumber(materials));
		}
	}
	
	@Override
	public LinkedHashMap<OABlockImpl, Integer> getStatsPlayer(OADatabaseManager.ValueType orderBy, UUID player) {
		List<String> materials = getEnabledMaterials(null, ConfigMain.STATS_BLACKLIST_BLOCKS_STATS);
		List<Pair<String, Integer>> result;
		if (orderBy.equals(OADatabaseManager.ValueType.FOUND)) {
			// Found order
			result = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksFoundDao).getStatsPlayer(materials, player.toString()));
			
		} else {
			// Destroy order
			result = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(blocksDao).getStatsPlayer(materials, player.toString()));
		}
		
		LinkedHashMap<OABlockImpl, Integer> ret = new LinkedHashMap<>();
		for (Pair<String, Integer> pair : result) {
			OABlockImpl block = Blocks.LIST.get(pair.getKey());
			if (block != null && block.isEnabled()) {
				ret.put(block, pair.getValue());
			}
		}
		return ret;
	}
	
	@Override
	protected int getBackwardMigration() {
		return storageType == StorageType.H2 ? -1 : 0;
	}
	
	private List<String> getEnabledMaterials(OABlock block, List<String> materialsBlacklist) {
		List<String> ret = new ArrayList<>();
		if (block != null)
			ret.add(block.getMaterialName());
		else {
			for (OABlockImpl b : Blocks.LIST.values()) {
				if (b.isEnabled() && !materialsBlacklist.contains(b.getMaterialName())) {
					ret.add(b.getMaterialName());
				}
			}
		}
		return ret;
	}
}
