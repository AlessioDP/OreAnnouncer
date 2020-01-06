package com.alessiodp.oreannouncer.bungeecord.blocks;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;

import java.util.UUID;

public class BungeeBlockManager extends BlockManager {
	
	public BungeeBlockManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean existsMaterial(String materialName) {
		// BungeeCord cannot check if a block is valid
		// Return true if not empty (used on block list setup)
		return !materialName.isEmpty();
	}
	
	@Override
	public boolean isBlockMarked(ADPLocation blockLocation, String material, MarkType markType) {
		// Nothing to do
		return false;
	}
	
	@Override
	public boolean markBlock(ADPLocation blockLocation, String material, MarkType markType) {
		// Nothing to do
		return false;
	}
	
	@Override
	public void unmarkBlock(ADPLocation blockLocation, MarkType markType) {
		// Nothing to do
	}
	
	@Override
	public String parsePAPI(UUID playerUuid, String message) {
		return message;
	}
}
