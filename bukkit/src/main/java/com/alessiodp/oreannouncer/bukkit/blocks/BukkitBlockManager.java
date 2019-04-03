package com.alessiodp.oreannouncer.bukkit.blocks;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.bukkit.bootstrap.BukkitOreAnnouncerBootstrap;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

public class BukkitBlockManager extends BlockManager {
	
	public BukkitBlockManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean existsMaterial(String materialName) {
		return Material.getMaterial(materialName.toUpperCase()) != null;
	}
	
	@Override
	public boolean markBlock(ADPLocation blockLocation, String material) {
		boolean ret = false;
		Block block = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (block != null && block.getType().toString().equalsIgnoreCase(material)) {
			if (!block.hasMetadata(OAConstants.BLOCK_METADATA)) {
				block.setMetadata(OAConstants.BLOCK_METADATA, new FixedMetadataValue((BukkitOreAnnouncerBootstrap) plugin.getBootstrap(), true));
				ret = true;
			}
		}
		return ret;
	}
	
	@Override
	public void unmarkBlock(ADPLocation blockLocation) {
		Block block = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (block != null && block.hasMetadata(OAConstants.BLOCK_METADATA)) {
			block.removeMetadata(OAConstants.BLOCK_METADATA, (BukkitOreAnnouncerBootstrap) plugin.getBootstrap());
			
		}
	}
}
