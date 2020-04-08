package com.alessiodp.oreannouncer.common.api;

import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OABlockDestroy;
import com.alessiodp.oreannouncer.api.interfaces.OABlockFound;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayerDataBlock;
import com.alessiodp.oreannouncer.api.interfaces.OreAnnouncerAPI;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;
import com.alessiodp.oreannouncer.common.storage.OADatabaseManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
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
		OAPlayer player = getOAPlayer(uuid);
		if (player != null) {
			player.getAllBlockDestroy().forEach((block) -> ret.add(new PlayerDataBlock(block.getMaterialName(), block.getPlayer(), block.getDestroyCount())));
		}
		return ret;
	}
	
	@Override
	public void updatePlayerDataBlock(OAPlayerDataBlock block) {
		OAPlayer player = getOAPlayer(block.getPlayer());
		if (player != null) {
			OABlock b = getBlock(block.getMaterialName());
			if (b != null) {
				player.setBlockDestroy(makeBlockDestroy(player.getPlayerUUID(), b, block.getDestroyCount()));
			}
		}
	}
	
	@Override
	public LinkedHashMap<OAPlayer, Integer> getTopPlayersByDestroy(int numberOfPlayers, OABlock block, int offset) {
		return getTopPlayers(OADatabaseManager.TopOrderBy.DESTROY, numberOfPlayers, block, offset);
	}
	
	@Override
	public LinkedHashMap<OAPlayer, Integer> getTopPlayersByFound(int numberOfPlayers, OABlock block, int offset) {
		return getTopPlayers(OADatabaseManager.TopOrderBy.FOUND, numberOfPlayers, block, offset);
	}
	
	private LinkedHashMap<OAPlayer, Integer> getTopPlayers(OADatabaseManager.TopOrderBy order, int numberOfPlayers, OABlock block, int offset) {
		LinkedHashMap<OAPlayer, Integer> ret = new LinkedHashMap<>();
		HashMap<UUID, Integer> players = plugin.getDatabaseManager().getTopPlayers(order, (OABlockImpl) block, numberOfPlayers, offset);
		for (Map.Entry<UUID, Integer> e : players.entrySet()) {
			OAPlayerImpl player = plugin.getPlayerManager().getPlayer(e.getKey());
			if (player != null)
				ret.put(player, e.getValue());
		}
		return ret;
	}
	
	@Override
	public LinkedList<OABlockFound> getLogBlocks(int limit, OAPlayer player, OABlock block, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getLogBlocks((OAPlayerImpl) player, block, limit, offset));
	}
	
	@Override
	public @Nullable OABlock getBlock(@org.checkerframework.checker.nullness.qual.NonNull String materialName) {
		return Blocks.LIST.get(materialName);
	}
	
	@Override
	public OABlock addBlock(@org.checkerframework.checker.nullness.qual.NonNull String materialName) {
		OABlockImpl ret = null;
		if (!((OAConfigurationManager) plugin.getConfigurationManager()).getBlocks().existsBlock(materialName)) {
			ret = new OABlockImpl(plugin, materialName.toUpperCase(Locale.ENGLISH));
			ret.updateBlock();
		}
		return ret;
	}
	
	@Override
	public void removeBlock(@org.checkerframework.checker.nullness.qual.NonNull OABlock block) {
		((OABlockImpl) block).removeBlock();
	}
	
	@Override
	public OABlockDestroy makeBlockDestroy(UUID playerUuid, OABlock block, int destroyCount) {
		return new BlockDestroy(playerUuid, block.getMaterialName(), destroyCount);
	}
}
