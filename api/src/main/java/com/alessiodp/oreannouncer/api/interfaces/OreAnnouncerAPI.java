package com.alessiodp.oreannouncer.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;

public interface OreAnnouncerAPI {
	
	/**
	 * Reload OreAnnouncer configuration files
	 */
	void reloadPlugin();
	
	/**
	 * Reload OreAnnouncer configuration files
	 * @deprecated Use reloadPlugin() instead
	*/
	@Deprecated
	default void reloadOreAnnouncer() {
		reloadPlugin();
	}
	
	/**
	 * Check if the plugin have BungeeCord option enabled
	 *
	 * @return True if BungeeCord support is enabled
	 */
	boolean isBungeeCordEnabled();
	
	/**
	 * Get the player by his {@link UUID}
	 *
	 * @param uuid The {@link UUID} of the player
	 * @return Returns the {@link OAPlayer} of the relative player
	 */
	OAPlayer getOAPlayer(UUID uuid);
	
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
	@Nullable
	OABlock getBlock(@NotNull String materialName);
	
	/**
	 * Add a block to the configuration
	 *
	 * @param materialName The block name to add
	 * @return Returns a new block or null if already exists
	 */
	@Nullable
	OABlock addBlock(@NotNull String materialName);
	
	/**
	 * Remove the block from the configuration
	 *
	 * @param block The block to remove
	 */
	void removeBlock(@NotNull OABlock block);
	
	/**
	 * Make a new {@link OABlockDestroy}
	 *
	 * @param playerUuid The player UUID
	 * @param block The block
	 * @param destroyCount The number of destroyed blocks
	 * @return Returns a new instance of {@link OABlockDestroy}
	 */
	OABlockDestroy makeBlockDestroy(@NotNull UUID playerUuid, @NotNull OABlock block, int destroyCount);
}
