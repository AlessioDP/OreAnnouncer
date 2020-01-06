package com.alessiodp.oreannouncer.bukkit.blocks;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.oreannouncer.bukkit.bootstrap.BukkitOreAnnouncerBootstrap;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class BukkitBlockManager extends BlockManager {
	
	public BukkitBlockManager(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean existsMaterial(String materialName) {
		return Material.getMaterial(materialName.toUpperCase()) != null;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean isBlockMarked(ADPLocation blockLocation, String material, MarkType markType) {
		boolean ret = false;
		Block block = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (block != null
				&& block.getType().toString().equalsIgnoreCase(material)
				&& block.hasMetadata(markType.getMark())) {
			ret = true;
		}
		return ret;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean markBlock(ADPLocation blockLocation, String material, MarkType markType) {
		boolean ret = false;
		Block block = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (block != null
				&& block.getType().toString().equalsIgnoreCase(material)
				&& !block.hasMetadata(markType.getMark())) {
			block.setMetadata(markType.getMark(), new FixedMetadataValue((BukkitOreAnnouncerBootstrap) plugin.getBootstrap(), true));
			ret = true;
		}
		return ret;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void unmarkBlock(ADPLocation blockLocation, MarkType markType) {
		Block block = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (block != null && block.hasMetadata(markType.getMark())) {
			block.removeMetadata(markType.getMark(), (BukkitOreAnnouncerBootstrap) plugin.getBootstrap());
			
		}
	}
	
	@Override
	protected String parsePAPI(UUID playerUuid, String message) {
		return PlaceholderAPIHandler.getPlaceholders(playerUuid, message);
	}
}
