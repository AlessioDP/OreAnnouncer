package com.alessiodp.oreannouncer.bukkit.blocks;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.bukkit.addons.external.DiscordSRVHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.ItemModsHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.MMOItemsHandler;
import com.alessiodp.oreannouncer.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.oreannouncer.bukkit.bootstrap.BukkitOreAnnouncerBootstrap;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import com.alessiodp.oreannouncer.common.blocks.objects.OABlockImpl;
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
	public void sendGlobalAlert(BlockData data, AlertType type) {
		super.sendGlobalAlert(data, type);
		DiscordSRVHandler.dispatchAlerts(data, type);
	}
	
	@Override
	public boolean existsMaterial(String materialName) {
		return Material.getMaterial(CommonUtils.toUpperCase(materialName)) != null
				|| CommonUtils.toUpperCase(materialName).startsWith("ITEMMODS_")
				|| CommonUtils.toUpperCase(materialName).startsWith("MMOITEMS_");
	}
	
	public String getBlockType(Block block) {
		if (ItemModsHandler.isPluginBlock(block)) {
			return ItemModsHandler.getNameByBlock(block);
		} else if (MMOItemsHandler.isPluginBlock(block)) {
			return MMOItemsHandler.getNameByBlock(block);
		}
		return block.getType().name();
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean isBlockMarked(ADPLocation blockLocation, MarkType markType) {
		boolean ret = false;
		Block bukkitBlock = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (bukkitBlock != null
				&& bukkitBlock.hasMetadata(markType.getMark())) {
			ret = true;
		}
		return ret;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean markBlock(ADPLocation blockLocation, OABlockImpl block, MarkType markType) {
		boolean ret = false;
		Block bukkitBlock = new Location(
				Bukkit.getWorld(blockLocation.getWorld()),
				blockLocation.getX(),
				blockLocation.getY(),
				blockLocation.getZ(),
				blockLocation.getYaw(),
				blockLocation.getPitch()).getBlock();
		if (bukkitBlock != null
				&& (block.getMaterialName().equalsIgnoreCase(getBlockType(bukkitBlock)) || block.getVariants().contains(getBlockType(bukkitBlock)))
				&& !bukkitBlock.hasMetadata(markType.getMark())) {
			bukkitBlock.setMetadata(markType.getMark(), new FixedMetadataValue((BukkitOreAnnouncerBootstrap) plugin.getBootstrap(), true));
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
