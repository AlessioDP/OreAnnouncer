package com.alessiodp.oreannouncer.common.messaging;

import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;

public interface OAMessageDispatcher {
	void sendUpdatePlayer(OAPlayerImpl player);
	
	void sendAlert(BlockData blockData, String messageUser, String messageAdmin, String messageConsole);
	
	void sendAlertCount(BlockData blockData, String messageUser, String messageAdmin, String messageConsole);
	
	void sendAlertTNT(BlockData blockData, String messageUser, String messageAdmin, String messageConsole);
	
	void sendBlockDestroy(BlockData blockData);
	
	void sendBlockFound(BlockData blockData);
}
