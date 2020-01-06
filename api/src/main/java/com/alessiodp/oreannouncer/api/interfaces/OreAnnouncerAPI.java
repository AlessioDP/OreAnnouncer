package com.alessiodp.oreannouncer.api.interfaces;

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
	Set<OAPlayerDataBlock> getPlayerBlocks(UUID uuid);
	
	/**
	 * Send changes to the database. Used to save the data block.
	 *
	 * @param block The {@link OAPlayerDataBlock} to save
	 */
	void updatePlayerDataBlock(OAPlayerDataBlock block);
	
	/**
	 * Get top players
	 * @deprecated Use getTopPlayersByDestroy(int) or getTopPlayersByFound(int)
	 */
	@Deprecated
	default Set<OAPlayer> getTopPlayers(int numberOfPlayers) {
		return getTopPlayersByDestroy(numberOfPlayers);
	}
	
	/**
	 * Get top players ordered by destroy
	 *
	 * @param numberOfPlayers Number of players to get
	 * @return Returns a set of players
	 */
	Set<OAPlayer> getTopPlayersByDestroy(int numberOfPlayers);
	
	/**
	 * Get top players ordered by found
	 *
	 * @param numberOfPlayers Number of players to get
	 * @return Returns a set of players
	 */
	Set<OAPlayer> getTopPlayersByFound(int numberOfPlayers);
}
