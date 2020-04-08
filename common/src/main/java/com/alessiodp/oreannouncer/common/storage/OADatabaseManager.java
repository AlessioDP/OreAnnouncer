package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.dispatchers.OASQLDispatcher;
import com.alessiodp.oreannouncer.common.storage.interfaces.IOADatabase;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

public class OADatabaseManager extends DatabaseManager implements IOADatabase {
	public OADatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case MYSQL:
			case SQLITE:
			case H2:
				ret = new OASQLDispatcher(plugin, storageType);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED
						.replace("{type}", ConfigMain.STORAGE_TYPE_DATABASE));
				break;
		}
		return ret;
	}
	
	@Override
	public void updatePlayer(OAPlayerImpl player) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_UPDATEPLAYER
					.replace("{player}", player.getName())
					.replace("{uuid}", player.getPlayerUUID().toString()), true);
			
			((IOADatabase) database).updatePlayer(player);
		});
	}
	
	@Override
	public OAPlayerImpl getPlayer(UUID playerUuid) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GETPLAYER
					.replace("{uuid}", playerUuid.toString()), true);
			
			return ((IOADatabase) database).getPlayer(playerUuid);
		}).join();
	}
	
	@Override
	public LinkedHashMap<UUID, Integer> getTopPlayers(TopOrderBy orderBy, @Nullable OABlockImpl block, int limit, int offset) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_TOP_BLOCKS_LIST
					.replace("{order}", orderBy.name())
					.replace("{limit}", Integer.toString(limit))
					.replace("{offset}", Integer.toString(offset)), true);
			
			return ((IOADatabase) database).getTopPlayers(orderBy, block, limit, offset);
		}).join();
	}
	
	@Override
	public int getTopPlayersNumber(TopOrderBy orderBy, @Nullable OABlockImpl block) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_TOP_BLOCKS_NUMBER
					.replace("{order}", orderBy.name()), true);
			
			return ((IOADatabase) database).getTopPlayersNumber(orderBy, block);
		}).join();
	}
	
	@Override
	public void updateBlockDestroy(BlockDestroy blockDestroy) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_UPDATE_BLOCK_DESTROY
					.replace("{uuid}", blockDestroy.getPlayer().toString())
					.replace("{block}", blockDestroy.getMaterialName()), true);
			
			((IOADatabase) database).updateBlockDestroy(blockDestroy);
		});
	}
	
	@Override
	public void setBlockDestroy(BlockDestroy blockDestroy) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_SET_BLOCK_DESTROY
					.replace("{uuid}", blockDestroy.getPlayer().toString())
					.replace("{block}", blockDestroy.getMaterialName()), true);
			
			((IOADatabase) database).setBlockDestroy(blockDestroy);
		});
	}
	
	@Override
	public BlockDestroy getBlockDestroy(UUID player, OABlock block) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GET_BLOCK_DESTROY
					.replace("{uuid}", player.toString())
					.replace("{block}", block.getMaterialName()), true);
			
			return ((IOADatabase) database).getBlockDestroy(player, block);
		}).join();
	}
	
	@Override
	public Set<BlockDestroy> getAllBlockDestroy(UUID player) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GET_ALL_BLOCK_DESTROY
					.replace("{uuid}", player.toString()), true);
			
			return ((IOADatabase) database).getAllBlockDestroy(player);
		}).join();
	}
	
	@Override
	public void insertBlockFound(BlockFound blockFound) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_INSERT_BLOCK_FOUND
					.replace("{uuid}", blockFound.getPlayer().toString())
					.replace("{block}", blockFound.getMaterialName()), true);
			
			((IOADatabase) database).insertBlockFound(blockFound);
		});
	}
	
	@Override
	public BlocksFoundResult getBlockFound(UUID player, OABlock block, long rangeTime) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GET_BLOCK_FOUND
					.replace("{uuid}", player.toString())
					.replace("{block}", block.getMaterialName()), true);
			
			return ((IOADatabase) database).getBlockFound(player, block, (System.currentTimeMillis() / 1000L) - rangeTime);
		}).join();
	}
	
	@Override
	public LinkedList<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_LOG_BLOCKS
					.replace("{uuid}", player != null ? player.getPlayerUUID().toString() : "")
					.replace("{block}", block != null ? block.getMaterialName() : ""), true);
			
			return ((IOADatabase) database).getLogBlocks(player, block, limit, offset);
		}).join();
	}
	
	@Override
	public int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_LOG_BLOCKS_NUMBER
					.replace("{uuid}", player != null ? player.getPlayerUUID().toString() : "")
					.replace("{block}", block != null ? block.getMaterialName() : ""), true);
			
			return ((IOADatabase) database).getLogBlocksNumber(player, block);
		}).join();
	}
	
	public enum TopOrderBy {
		DESTROY, FOUND;
		
		public static TopOrderBy parse(String order) {
			if (order.equalsIgnoreCase(Messages.CMD_TOP_WORD_DESTROY))
				return DESTROY;
			else if (order.equalsIgnoreCase(Messages.CMD_TOP_WORD_FOUND))
				return FOUND;
			return null;
		}
	}
}
