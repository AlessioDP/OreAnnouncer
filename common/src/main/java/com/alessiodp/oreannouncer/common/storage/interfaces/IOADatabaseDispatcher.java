package com.alessiodp.oreannouncer.common.storage.interfaces;

import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import com.alessiodp.oreannouncer.common.players.objects.PlayerDataBlock;

import java.util.ArrayList;
import java.util.UUID;

public interface IOADatabaseDispatcher extends IDatabaseDispatcher {
	void updatePlayer(OAPlayerImpl player);
	OAPlayerImpl getPlayer(UUID playerUuid);
	OAPlayerImpl getPlayerByName(String playerName);
	ArrayList<OAPlayerImpl> getTopPlayersDestroyed(int limit, int offset);
	int getTopPlayersNumber();
	
	void updateDataBlock(PlayerDataBlock playerDataBlock);
}
