package com.alessiodp.oreannouncer.common.storage.interfaces;

import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IOADatabase {
	void updatePlayer(OAPlayerImpl player);
	OAPlayerImpl getPlayer(UUID playerUuid);
	
	void updateBlockDestroy(BlockDestroy blockDestroy);
	void setBlockDestroy(BlockDestroy blockDestroy);
	BlockDestroy getBlockDestroy(UUID player, OABlock block);
	Set<BlockDestroy> getAllBlockDestroy(UUID player);
	
	void insertBlockFound(BlockFound blockFound);
	BlocksFoundResult getBlockFound(UUID player, OABlock block, long rangeTime);
	List<BlockFound> getLogBlocks(@Nullable OAPlayerImpl player, @Nullable OABlock block, int limit, int offset);
	int getLogBlocksNumber(@Nullable OAPlayerImpl player, @Nullable OABlock block);
	
	LinkedHashMap<UUID, Integer> getTopPlayers(OADatabaseManager.ValueType orderBy, @Nullable OABlockImpl block, int limit, int offset);
	int getTopPlayersNumber(OADatabaseManager.ValueType orderBy, @Nullable OABlockImpl block);
	
	LinkedHashMap<OABlockImpl, Integer> getStatsPlayer(OADatabaseManager.ValueType valueType, UUID player);
}
