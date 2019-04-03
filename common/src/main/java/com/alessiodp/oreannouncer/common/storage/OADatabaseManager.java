package com.alessiodp.oreannouncer.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.NoneDispatcher;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.dispatchers.OASQLDispatcher;
import com.alessiodp.oreannouncer.common.storage.interfaces.IOADatabaseDispatcher;

import java.util.ArrayList;
import java.util.UUID;

public class OADatabaseManager extends DatabaseManager {
	public OADatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	public IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case NONE:
				ret = new NoneDispatcher();
				break;
			case MYSQL:
			case SQLITE:
				ret = new OASQLDispatcher(plugin);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED
						.replace("{type}", ConfigMain.STORAGE_TYPE_DATABASE));
		}
		
		if (ret != null) {
			ret.init(storageType);
			if (ret.isFailed())
				return null;
		}
		return ret;
	}
	
	public void updatePlayer(OAPlayerImpl player) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_UPDATEPLAYER
					.replace("{player}", player.getName())
					.replace("{uuid}", player.getPlayerUUID().toString()), true);
			
			((IOADatabaseDispatcher) database).updatePlayer(player);
		}).join();
	}
	
	public OAPlayerImpl getPlayer(UUID playerUuid) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GETPLAYER
					.replace("{uuid}", playerUuid.toString()), true);
			
			return ((IOADatabaseDispatcher) database).getPlayer(playerUuid);
		}).join();
	}
	
	public OAPlayerImpl getPlayerByName(String playerName) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_GETPLAYER_BYNAME
					.replace("{player}", playerName), true);
			
			return ((IOADatabaseDispatcher) database).getPlayerByName(playerName);
		}).join();
	}
	
	public ArrayList<OAPlayerImpl> getTopPlayersDestroyed(int limit, int offset) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_TOP_PLAYERBLOCKS
					.replace("{limit}", Integer.toString(limit))
					.replace("{offset}", Integer.toString(offset)), true);
			
			return ((IOADatabaseDispatcher) database).getTopPlayersDestroyed(limit, offset);
		}).join();
	}
	
	public Integer getTopPlayersNumber() {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_TOP_NUMBER, true);
			
			return ((IOADatabaseDispatcher) database).getTopPlayersNumber();
		}).join();
	}
	
	public void updateDataBlock(PlayerDataBlock playerDataBlock) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(OAConstants.DEBUG_DB_UPDATEDATABLOCK
					.replace("{uuid}", playerDataBlock.getPlayer().toString())
					.replace("{block}", playerDataBlock.getMaterialName()), true);
			
			((IOADatabaseDispatcher) database).updateDataBlock(playerDataBlock);
		}).join();
	}
}
