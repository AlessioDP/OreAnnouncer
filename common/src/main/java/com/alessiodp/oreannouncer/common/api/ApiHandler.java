package com.alessiodp.oreannouncer.common.api;

import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayerDataBlock;
import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class ApiHandler implements OreAnnouncerAPI {
	@NonNull private final OreAnnouncerPlugin plugin;
	
	@Override
	public void reloadOreAnnouncer() {
		plugin.reloadConfiguration();
	}
	
	@Override
	public void updatePlayer(OAPlayer player) {
		((OAPlayerImpl) player).updatePlayer();
	}
	
	@Override
	public OAPlayer getOAPlayer(UUID uuid) {
		return plugin.getPlayerManager().getPlayer(uuid);
	}
	
	@Override
	public Set<OAPlayerDataBlock> getPlayerBlocks(UUID uuid) {
		Set<OAPlayerDataBlock> ret = new HashSet<>();
		OAPlayerImpl player = (OAPlayerImpl) getOAPlayer(uuid);
		if (player != null) {
			ret.addAll(player.getDataBlocks().values());
		}
		return ret;
	}
	
	@Override
	public void updatePlayerDataBlock(OAPlayerDataBlock block) {
		plugin.getDatabaseManager().updateDataBlock((PlayerDataBlock) block);
	}
	
	@Override
	public Set<OAPlayer> getTopPlayersByDestroy(int numberOfPlayers) {
		return new HashSet<>(plugin.getDatabaseManager().getTopPlayersDestroyed(OADatabaseManager.TopOrderBy.DESTROY, numberOfPlayers, 0));
	}
	
	@Override
	public Set<OAPlayer> getTopPlayersByFound(int numberOfPlayers) {
		return new HashSet<>(plugin.getDatabaseManager().getTopPlayersDestroyed(OADatabaseManager.TopOrderBy.FOUND, numberOfPlayers, 0));
	}
}
