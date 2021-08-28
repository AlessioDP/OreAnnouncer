package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.core.common.utils.CommonUtils;
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
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OADatabaseManager extends DatabaseManager {
	
	public OADatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case MARIADB:
			case MYSQL:
			case POSTGRESQL:
			case SQLITE:
			case H2:
				ret = new OASQLDispatcher(plugin, storageType);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(String.format(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED, ConfigMain.STORAGE_TYPE_DATABASE));
				break;
		}
		
		return ret;
	}
	
	public CompletableFuture<Void> updatePlayer(OAPlayerImpl player) {
		return executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_UPDATEPLAYER, player.getName(), player.getPlayerUUID().toString()), true);
			
			((IOADatabase) database).updatePlayer(player);
		});
	}
	
	public OAPlayerImpl getPlayer(UUID playerUuid) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_GETPLAYER, playerUuid.toString()), true);
			
			return ((IOADatabase) database).getPlayer(playerUuid);
		}).join();
	}
	
	public void updateBlockDestroy(BlockDestroy blockDestroy) {
		executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_UPDATE_BLOCK_DESTROY, blockDestroy.getPlayer().toString(), blockDestroy.getMaterialName()), true);
			
			((IOADatabase) database).updateBlockDestroy(blockDestroy);
		});
	}
	
	public void setBlockDestroy(BlockDestroy blockDestroy) {
		executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_SET_BLOCK_DESTROY, blockDestroy.getPlayer().toString(), blockDestroy.getMaterialName()), true);
			
			((IOADatabase) database).setBlockDestroy(blockDestroy);
		});
	}
	
	public BlockDestroy getBlockDestroy(UUID player, @Nullable OABlock block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_GET_BLOCK_DESTROY, player.toString(), block != null ? block.getMaterialName() : "*"), true);
			
			return ((IOADatabase) database).getBlockDestroy(player, block);
		}).join();
	}
	
	public Set<BlockDestroy> getAllBlockDestroy(UUID player) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_GET_ALL_BLOCK_DESTROY, player.toString()), true);
			
			return ((IOADatabase) database).getAllBlockDestroy(player);
		}).join();
	}
	
	public void insertBlockFound(BlockFound blockFound) {
		executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_INSERT_BLOCK_FOUND, blockFound.getPlayer().toString(), blockFound.getMaterialName()), true);
			
			((IOADatabase) database).insertBlockFound(blockFound);
		});
	}
	
	public BlocksFoundResult getBlockFound(UUID player, @Nullable OABlock block, long rangeTime) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_GET_BLOCK_FOUND, player.toString(), block != null ? block.getMaterialName() : "*"), true);
			
			return ((IOADatabase) database).getBlockFound(player, block, (System.currentTimeMillis() / 1000L) - rangeTime);
		}).join();
	}
	
	public List<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(
					OAConstants.DEBUG_DB_LOG_BLOCKS,
					player != null ? player.getPlayerUUID().toString() : "",
					block != null ? block.getMaterialName() : ""
			), true);
			
			return ((IOADatabase) database).getLogBlocks(player, block, limit, offset);
		}).join();
	}
	
	public int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(
					OAConstants.DEBUG_DB_LOG_BLOCKS_NUMBER,
					player != null ? player.getPlayerUUID().toString() : "",
					block != null ? block.getMaterialName() : ""
			), true);
			
			return ((IOADatabase) database).getLogBlocksNumber(player, block);
		}).join();
	}
	
	public LinkedHashMap<UUID, Integer> getTopPlayers(ValueType orderBy, @Nullable OABlockImpl block, int limit, int offset) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_TOP_PLAYERS, orderBy.name(), block != null ? block.getMaterialName() : "none", limit, offset), true);
			
			return ((IOADatabase) database).getTopPlayers(orderBy, block, limit, offset);
		}).join();
	}
	
	public int getTopPlayersNumber(ValueType orderBy, @Nullable OABlockImpl block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_TOP_PLAYERS_NUMBER, orderBy.name(), block != null ? block.getMaterialName() : "none"), true);
			
			return ((IOADatabase) database).getTopPlayersNumber(orderBy, block);
		}).join();
	}
	
	public int getTopPlayerPosition(UUID player, OADatabaseManager.ValueType orderBy, @Nullable OABlockImpl block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_TOP_PLAYER_POSITION, orderBy.name(), block != null ? block.getMaterialName() : "none"), true);
			
			return ((IOADatabase) database).getTopPlayerPosition(player, orderBy, block);
		}).join();
	}
	
	public int getTotalDestroy(@Nullable OABlock block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_TOTAL_DESTROY, block != null ? block.getMaterialName() : "none"), true);
			
			return ((IOADatabase) database).getTotalDestroy(block);
		}).join();
	}
	
	public int getTotalFound(@Nullable OABlock block) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_TOTAL_FOUND, block != null ? block.getMaterialName() : "none"), true);
			
			return ((IOADatabase) database).getTotalFound(block);
		}).join();
	}
	
	public LinkedHashMap<OABlockImpl, Integer> getStatsPlayer(OADatabaseManager.ValueType valueType, UUID player) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(OAConstants.DEBUG_DB_STATS_PLAYER, player.toString(), valueType.name()), true);
			
			return ((IOADatabase) database).getStatsPlayer(valueType, player);
		}).join();
	}
	
	public enum ValueType {
		DESTROY, FOUND;
		
		public static ValueType getType(String type) {
			switch (CommonUtils.toLowerCase(type)) {
				case "destroy":
					return DESTROY;
				case "found":
					return FOUND;
				default:
					return null;
			}
		}
		
		public static ValueType parse(String type) {
			if (type.equalsIgnoreCase(Messages.OREANNOUNCER_SYNTAX_DESTROY))
				return DESTROY;
			else if (type.equalsIgnoreCase(Messages.OREANNOUNCER_SYNTAX_FOUND))
				return FOUND;
			return null;
		}
	}
}
