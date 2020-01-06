package com.alessiodp.oreannouncer.common.storage.interfaces;

import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;

import java.util.ArrayList;
import java.util.UUID;

public interface IOADatabaseDispatcher extends IDatabaseDispatcher {
	void updatePlayer(OAPlayerImpl player);
	OAPlayerImpl getPlayer(UUID playerUuid);
	ArrayList<OAPlayerImpl> getTopPlayers(OADatabaseManager.TopOrderBy orderBy, int limit, int offset);
	int getTopPlayersNumber(OADatabaseManager.TopOrderBy orderBy);
	
	void updateDataBlock(PlayerDataBlock playerDataBlock);
	
	void insertBlocksFound(BlockFound blockFound);
	BlockFound getLatestBlocksFound(UUID player, OABlock block, long rangeTime);
}
