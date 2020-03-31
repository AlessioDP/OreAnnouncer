package com.alessiodp.oreannouncer.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.interfaces.IDatabaseSQL;
import com.alessiodp.core.common.storage.sql.mysql.MySQLDao;
import com.alessiodp.core.common.storage.sql.mysql.MySQLHikariConfiguration;
import com.alessiodp.core.common.storage.sql.sqlite.SQLiteDao;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.jpa.tables.records.BlocksFoundRecord;
import com.alessiodp.oreannouncer.common.jpa.tables.records.BlocksRecord;
import com.alessiodp.oreannouncer.common.jpa.tables.records.PlayersRecord;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.storage.interfaces.IOADatabaseDispatcher;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectForUpdateStep;
import org.jooq.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.alessiodp.oreannouncer.common.jpa.Tables.*;
import static org.jooq.impl.DSL.*;

public class OASQLDispatcher extends SQLDispatcher implements IOADatabaseDispatcher {
	
	public OASQLDispatcher(ADPPlugin plugin, StorageType storageType) {
		super(plugin, storageType);
	}
	
	@Override
	public IDatabaseSQL initDao() {
		IDatabaseSQL ret = null;
		switch (storageType) {
			case MYSQL:
				MySQLHikariConfiguration hikari = new MySQLHikariConfiguration(
						plugin.getPluginFallbackName(),
						ConfigMain.STORAGE_SETTINGS_MYSQL_ADDRESS,
						ConfigMain.STORAGE_SETTINGS_MYSQL_PORT,
						ConfigMain.STORAGE_SETTINGS_MYSQL_DATABASE,
						ConfigMain.STORAGE_SETTINGS_MYSQL_USERNAME,
						ConfigMain.STORAGE_SETTINGS_MYSQL_PASSWORD
				);
				hikari.setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_MYSQL_POOLSIZE);
				hikari.setMaxLifetime(ConfigMain.STORAGE_SETTINGS_MYSQL_CONNLIFETIME);
				hikari.setUseSSL(ConfigMain.STORAGE_SETTINGS_MYSQL_USESSL);
				
				
				ret = new MySQLDao(plugin, hikari);
				break;
			case SQLITE:
				ret = new SQLiteDao(plugin, ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
				break;
			default:
				// Unsupported storage type
		}
		return ret;
	}
	
	@Override
	protected List<Table<?>> getTables() {
		List<Table<?>> ret = super.getTables();
		ret.add(BLOCKS);
		ret.add(BLOCKS_FOUND);
		ret.add(PLAYERS);
		return ret;
	}
	
	@Override
	protected String initPrefix() {
		return ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX;
	}
	
	@Override
	protected String initCharset() {
		return ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_CHARSET;
	}
	
	@Override
	public void updatePlayer(OAPlayerImpl player) {
		try {
			boolean existData = false;
			if (!player.haveAlertsOn() || player.getDataBlocks().size() > 0)
				existData = true;
			
			if (existData) {
				// Save data
				database.getQueryBuilder()
						.insertInto(PLAYERS,
								PLAYERS.UUID,
								PLAYERS.ALERTS
						).values(
								player.getPlayerUUID().toString(),
								player.haveAlertsOn()
						).onDuplicateKeyUpdate()
						.set(PLAYERS.ALERTS, player.haveAlertsOn())
						.execute();
			} else {
				// Remove data
				database.getQueryBuilder()
						.deleteFrom(PLAYERS)
						.where(PLAYERS.UUID.eq(player.getPlayerUUID().toString()))
						.execute();
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public OAPlayerImpl getPlayer(UUID playerUuid) {
		OAPlayerImpl ret = null;
		try {
			PlayersRecord pr = database.getQueryBuilder()
					.selectFrom(PLAYERS)
					.where(PLAYERS.UUID.eq(playerUuid.toString()))
					.fetchAny();
			if (pr != null) {
				String uuid = pr.getUuid();
				try {
					ret = ((OreAnnouncerPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(uuid));
					ret.setAlertsOn(pr.getAlerts());
					
					ArrayList<PlayerDataBlock> blocks = new ArrayList<>();
					Result<BlocksRecord> blocksResult = database.getQueryBuilder()
							.selectFrom(BLOCKS)
							.where(BLOCKS.PLAYER.eq(playerUuid.toString())).fetch();
					
					if (blocksResult != null) {
						for (BlocksRecord br : blocksResult) {
							try {
								PlayerDataBlock pdb = new PlayerDataBlock();
								pdb.setPlayer(UUID.fromString(br.getPlayer()));
								pdb.setMaterialName(br.getMaterialName());
								pdb.setDestroyCount(br.getDestroyed());
								
								blocks.add(pdb);
							} catch (IllegalArgumentException ex) {
								plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR_UUID
										.replace("{uuid}", uuid), ex);
							} catch (Exception ex) {
								plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
							}
							
						}
					}
					ret.loadBlocks(blocks);
				} catch (IllegalArgumentException ex) {
					plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR_UUID
							.replace("{uuid}", uuid.isEmpty() ? "empty" : uuid), ex);
				}
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	private SelectForUpdateStep<?> getTopPlayersByDestroy(OABlock block, int limit, int offset, boolean count) {
		Condition condition = null;
		if (block != null) {
			condition = BLOCKS.MATERIAL_NAME.eq(block.getMaterialName());
		} else {
			for (OABlockImpl b : Blocks.LIST.values()) {
				if (b.isEnabled() && !ConfigMain.STATS_BLACKLIST_BLOCKS_TOP.contains(b.getMaterialName())) {
					condition = condition != null ? condition.or(BLOCKS.MATERIAL_NAME.eq(b.getMaterialName())) : BLOCKS.MATERIAL_NAME.eq(b.getMaterialName());
				}
			}
		}
		
		if (count) {
			return database.getQueryBuilder().select(countDistinct(BLOCKS.PLAYER).as("total"))
					.from(BLOCKS)
					.where(condition);
		}
		return database.getQueryBuilder()
				.select(BLOCKS.PLAYER.as("player"), sum(BLOCKS.DESTROYED).as("total"))
				.from(BLOCKS)
				.where(condition)
				.groupBy(field("player"))
				.orderBy(field("total").desc())
				.limit(limit).offset(offset);
	}
	
	private SelectForUpdateStep<?> getTopPlayersByFound(OABlock block, int limit, int offset, boolean count) {
		Condition condition = null;
		if (block != null) {
			condition = BLOCKS_FOUND.MATERIAL_NAME.eq(block.getMaterialName());
		} else {
			for (OABlockImpl b : Blocks.LIST.values()) {
				if (b.isEnabled() && !ConfigMain.STATS_BLACKLIST_BLOCKS_TOP.contains(b.getMaterialName())) {
					condition = condition != null ? condition.or(BLOCKS_FOUND.MATERIAL_NAME.eq(b.getMaterialName())) : BLOCKS_FOUND.MATERIAL_NAME.eq(b.getMaterialName());
				}
			}
		}
		
		if (count) {
			return database.getQueryBuilder().select(countDistinct(BLOCKS_FOUND.PLAYER).as("total"))
					.from(BLOCKS_FOUND)
					.where(condition);
		}
		return database.getQueryBuilder()
				.select(BLOCKS_FOUND.PLAYER.as("player"), sum(BLOCKS_FOUND.FOUND).as("total"))
				.from(BLOCKS_FOUND)
				.where(condition)
				.groupBy(field("player"))
				.orderBy(field("total"))
				.limit(limit).offset(offset);
	}
	
	@Override
	public HashMap<UUID, Integer> getTopPlayers(OADatabaseManager.TopOrderBy orderBy, @Nullable OABlockImpl block, int limit, int offset) {
		HashMap<UUID, Integer> ret = new HashMap<>();
		try {
			Result<?> r;
			if (orderBy.equals(OADatabaseManager.TopOrderBy.FOUND)) {
				// Found order
				r = getTopPlayersByFound(block, limit, offset, false).fetch();
				
			} else {
				// Destroy order
				r = getTopPlayersByDestroy(block, limit, offset, false).fetch();
			}
			
			for (Record rec : r) {
				try {
					ret.put(
							UUID.fromString((String) rec.get("player")),
							((BigDecimal) rec.get("total")).intValue()
					);
				} catch (IllegalArgumentException ex) {
					plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR_UUID
							.replace("{uuid}", rec.get(1).toString()), ex);
				}
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	@Override
	public int getTopPlayersNumber(OADatabaseManager.TopOrderBy orderBy, @Nullable OABlockImpl block) {
		int ret = 0;
		try {
			Record rec;
			if (orderBy.equals(OADatabaseManager.TopOrderBy.FOUND)) {
				// Found order
				rec = getTopPlayersByFound(block, 0, 0, true).fetchAny();
				
			} else {
				// Destroy order
				rec = getTopPlayersByDestroy(block, 0, 0, true).fetchAny();
			}
			
			ret = (int) rec.get("total");
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	@Override
	public void updateDataBlock(PlayerDataBlock playerDataBlock) {
		try {
			database.getQueryBuilder()
					.insertInto(BLOCKS,
							BLOCKS.PLAYER,
							BLOCKS.MATERIAL_NAME,
							BLOCKS.DESTROYED
					).values(
							playerDataBlock.getPlayer().toString(),
							playerDataBlock.getMaterialName(),
							playerDataBlock.getDestroyCount()
					).onDuplicateKeyUpdate()
					.set(BLOCKS.DESTROYED, playerDataBlock.getDestroyCount())
					.execute();
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public void insertBlockFound(BlockFound blockFound) {
		try {
			database.getQueryBuilder()
					.insertInto(BLOCKS_FOUND,
							BLOCKS_FOUND.PLAYER,
							BLOCKS_FOUND.MATERIAL_NAME,
							BLOCKS_FOUND.TIMESTAMP,
							BLOCKS_FOUND.FOUND
					).values(
							blockFound.getPlayer().toString(),
							blockFound.getMaterialName(),
							BigDecimal.valueOf(blockFound.getTimestamp()),
							blockFound.getFound()
					).execute();
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public BlocksFoundResult getLatestBlocksFound(UUID player, OABlock block, long sinceTimestamp) {
		BlocksFoundResult ret = null;
		try {
			Condition blockCondition = block != null ? BLOCKS_FOUND.MATERIAL_NAME.eq(block.getMaterialName()) : null;
			
			Record2<BigDecimal, BigDecimal> rec = database.getQueryBuilder()
					.select(min(BLOCKS_FOUND.TIMESTAMP), sum(BLOCKS_FOUND.FOUND))
					.from(BLOCKS_FOUND)
					.where(
							BLOCKS_FOUND.PLAYER.eq(player.toString()),
							BLOCKS_FOUND.TIMESTAMP.greaterOrEqual(BigDecimal.valueOf(sinceTimestamp)),
							blockCondition
							)
					.fetchAny();
			
			if (rec != null && rec.value1() != null && rec.value2() != null) {
				ret = new BlocksFoundResult(rec.value1().longValue(), rec.value2().intValue());
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	private SelectForUpdateStep<?> getLogBlocks(OAPlayerImpl player, OABlock block, int limit, int offset, boolean count) {
		Condition condition = null;
		if (block != null) {
			condition = BLOCKS_FOUND.MATERIAL_NAME.eq(block.getMaterialName());
		} else {
			for (OABlockImpl b : Blocks.LIST.values()) {
				if (b.isEnabled() && !ConfigMain.STATS_BLACKLIST_BLOCKS_LOG.contains(b.getMaterialName())) {
					condition = condition != null ? condition.or(BLOCKS_FOUND.MATERIAL_NAME.eq(b.getMaterialName())) : BLOCKS_FOUND.MATERIAL_NAME.eq(b.getMaterialName());
				}
			}
		}
		
		if (player != null)
			condition = condition != null ? condition.and(BLOCKS_FOUND.PLAYER.eq(player.getPlayerUUID().toString())) : BLOCKS_FOUND.PLAYER.eq(player.getPlayerUUID().toString());
		
		if (count) {
			return database.getQueryBuilder()
					.select(count().as("total"))
					.from(BLOCKS_FOUND)
					.where(condition);
		}
		return database.getQueryBuilder()
				.selectFrom(BLOCKS_FOUND)
				.where(condition)
				.orderBy(BLOCKS_FOUND.TIMESTAMP)
				.limit(limit).offset(offset);
	}
	
	@Override
	public LinkedList<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset) {
		LinkedList<BlockFound> ret = new LinkedList<>();
		try {
			Result<?> r = getLogBlocks(player, block, limit, offset, false).fetch();
			
			for (Record rec : r) {
				try {
					ret.add(new BlockFound(
							UUID.fromString(((BlocksFoundRecord) rec).getPlayer()),
							((BlocksFoundRecord) rec).getMaterialName(),
							((BlocksFoundRecord) rec).getTimestamp().longValue(),
							((BlocksFoundRecord) rec).getFound()
					));
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	@Override
	public int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block) {
		int ret = 0;
		try {
			Record rec = getLogBlocks(player, block, 0, 0, true).fetchAny();
			
			ret = (int) rec.get("total");
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
}
