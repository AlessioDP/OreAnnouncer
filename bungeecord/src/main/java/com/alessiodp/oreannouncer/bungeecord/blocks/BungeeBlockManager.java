package com.alessiodp.oreannouncer.bungeecord.blocks;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;

import java.util.UUID;

public class BungeeBlockManager extends BlockManager {
	
	public BungeeBlockManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void reload() {
		super.reload();
		allowedBlocks = BungeeConfigMain.BLOCKS_LISTALLOWED;
	}
	
	@Override
	public boolean existsMaterial(String materialName) {
		// BungeeCord cannot check if a block is valid
		// Return true if not empty (used on block list setup)
		return !materialName.isEmpty();
	}
	
	@Override
	public boolean markBlock(ADPLocation blockLocation, String material) {
		// Nothing to do
		return false;
	}
	
	@Override
	public void unmarkBlock(ADPLocation blockLocation) {
		// Nothing to do
	}
	
	@Override
	public String parsePAPI(UUID playerUuid, String message) {
		return message;
	}
}
