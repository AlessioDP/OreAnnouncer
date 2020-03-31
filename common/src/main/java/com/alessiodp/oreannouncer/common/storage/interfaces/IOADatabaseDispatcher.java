package com.alessiodp.oreannouncer.common.storage.interfaces;

import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public interface IOADatabaseDispatcher extends IDatabaseDispatcher {
	void updatePlayer(OAPlayerImpl player);
	OAPlayerImpl getPlayer(UUID playerUuid);
	HashMap<UUID, Integer> getTopPlayers(OADatabaseManager.TopOrderBy orderBy, @Nullable OABlockImpl block, int limit, int offset);
	int getTopPlayersNumber(OADatabaseManager.TopOrderBy orderBy, @Nullable OABlockImpl block);
	
	void updateDataBlock(PlayerDataBlock playerDataBlock);
	
	void insertBlockFound(BlockFound blockFound);
	BlocksFoundResult getLatestBlocksFound(UUID player, OABlock block, long rangeTime);
	LinkedList<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset);
	int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block);
}
