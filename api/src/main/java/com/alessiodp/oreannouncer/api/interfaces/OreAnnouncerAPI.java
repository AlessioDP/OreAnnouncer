package com.alessiodp.oreannouncer.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

public interface OreAnnouncerAPI {
	
	/**
	 * Reload OreAnnouncer configuration files
	 */
	void reloadOreAnnouncer();
	
	/**
	 * Send changes to the database. Used to save player data.
	 *
	 * @param player The {@link OAPlayer} to save
	 */
	@Deprecated
	void updatePlayer(OAPlayer player);
	
	/**
	 * Get the player by his {@link UUID}
	 *
	 * @param uuid The {@link UUID} of the player
	 * @return Returns the {@link OAPlayer} of the relative player
	 */
	OAPlayer getOAPlayer(UUID uuid);
	
	/**
	 * Get a list of destroyed blocks by the player
	 *
	 * @param uuid The {@link UUID} of the player
	 * @return Returns a set of data blocks
	 */
	@Deprecated
	Set<OAPlayerDataBlock> getPlayerBlocks(UUID uuid);
	
	/**
	 * Send changes to the database. Used to save the data block.
	 *
	 * @param block The {@link OAPlayerDataBlock} to save
	 */
	@Deprecated
	void updatePlayerDataBlock(OAPlayerDataBlock block);
	
	/**
	 * Get top players
	 * @deprecated Use getTopPlayersByDestroy(int) or getTopPlayersByFound(int)
	 */
	@Deprecated
	default Set<OAPlayer> getTopPlayers(int numberOfPlayers) {
		return new LinkedHashSet<>(getTopPlayersByDestroy(numberOfPlayers).keySet());
	}
	
	/**
	 * Get top players ordered by destroy
	 *
	 * @param numberOfPlayers Number of players to get
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByDestroy(int numberOfPlayers) {
		return getTopPlayersByDestroy(numberOfPlayers, null, 0);
	}
	
	/**
	 * Get top players ordered by given block destroy
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param block The block to use as filter
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByDestroy(int numberOfPlayers, OABlock block) {
		return getTopPlayersByDestroy(numberOfPlayers, block, 0);
	}
	
	/**
	 * Get top players ordered by destroy
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param offset Offset of players list
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByDestroy(int numberOfPlayers, int offset) {
		return getTopPlayersByDestroy(numberOfPlayers, null, offset);
	}
	
	/**
	 * Get top players ordered by block destroy
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param block The block to use as filter
	 * @param offset Offset of players list
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	LinkedHashMap<OAPlayer, Integer> getTopPlayersByDestroy(int numberOfPlayers, OABlock block, int offset);
	
	/**
	 * Get top players ordered by found
	 *
	 * @param numberOfPlayers Number of players to get
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByFound(int numberOfPlayers) {
		return getTopPlayersByFound(numberOfPlayers, null, 0);
	}
	
	/**
	 * Get top players ordered by block found
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param block The block to use as filter
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByFound(int numberOfPlayers, OABlock block) {
		return getTopPlayersByFound(numberOfPlayers, block, 0);
	}
	
	/**
	 * Get top players ordered by found
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param offset Offset of players list
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	default LinkedHashMap<OAPlayer, Integer> getTopPlayersByFound(int numberOfPlayers, int offset) {
		return getTopPlayersByFound(numberOfPlayers, null, offset);
	}
	
	/**
	 * Get top players ordered by block found
	 *
	 * @param numberOfPlayers Number of players to get
	 * @param block The block to use as filter
	 * @param offset Offset the result list
	 * @return Returns a map of {@link OAPlayer} and {@link Integer}
	 */
	LinkedHashMap<OAPlayer, Integer> getTopPlayersByFound(int numberOfPlayers, OABlock block, int offset);
	
	/**
	 * Get latest blocks found
	 *
	 * @param limit The result number limit
	 * @return Returns a set of {@link OABlockFound}
	 */
	default LinkedList<OABlockFound> getLogBlocks(int limit) {
		return getLogBlocks(limit, null, null, 0);
	}
	
	/**
	 * Get latest blocks found by {@code player}
	 *
	 * @param limit The result number limit
	 * @param player The player to use as filter
	 * @return Returns a set of {@link OABlockFound}
	 */
	default LinkedList<OABlockFound> getLogBlocks(int limit, OAPlayer player) {
		return getLogBlocks(limit, player, null, 0);
	}
	
	/**
	 * Get latest blocks of {@code block} found
	 *
	 * @param limit The result number limit
	 * @param block The block to use as filter
	 * @return Returns a set of {@link OABlockFound}
	 */
	default LinkedList<OABlockFound> getLogBlocks(int limit, OABlock block) {
		return getLogBlocks(limit, null, block, 0);
	}
	
	/**
	 * Get latest blocks of {@code block} found by {@code player}
	 *
	 * @param limit The result number limit
	 * @param player The player to use as filter
	 * @param block The block to use as filter
	 * @return Returns a set of {@link OABlockFound}
	 */
	default LinkedList<OABlockFound> getLogBlocks(int limit, OAPlayer player, OABlock block) {
		return getLogBlocks(limit, player, block, 0);
	}
	
	/**
	 * Get latest blocks of {@code block} found by {@code player}
	 *
	 * @param limit The result number limit
	 * @param player The player to use as filter
	 * @param block The block to use as filter
	 * @param offset Offset the result list
	 * @return Returns a set of {@link OABlockFound}
	 */
	LinkedList<OABlockFound> getLogBlocks(int limit, OAPlayer player, OABlock block, int offset);
	
	/**
	 * Get the block from the configuration
	 *
	 * @param materialName The block to get
	 * @return Returns the block or null if not exists
	 */
	@Nullable OABlock getBlock(@NonNull String materialName);
	
	/**
	 * Add a block to the configuration
	 *
	 * @param materialName The block name to add
	 * @return Returns a new block or null if already exists
	 */
	@Nullable OABlock addBlock(@NonNull String materialName);
	
	/**
	 * Remove the block from the configuration
	 *
	 * @param block The block to remove
	 */
	void removeBlock(@NonNull OABlock block);
	
	/**
	 * Make a new {@link OABlockDestroy}
	 *
	 * @param playerUuid The player UUID
	 * @param block The block
	 * @param destroyCount The number of destroyed blocks
	 * @return Returns a new instance of {@link OABlockDestroy}
	 */
	OABlockDestroy makeBlockDestroy(@NonNull UUID playerUuid, @NonNull OABlock block, int destroyCount);
}
