package com.alessiodp.oreannouncer.api.events.bukkit.common;

import com.alessiodp.oreannouncer.api.events.bukkit.BukkitOreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.events.common.IBlockDestroyEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import org.jetbrains.annotations.NotNull;

public class BukkitOreAnnouncerBlockDestroyEvent extends BukkitOreAnnouncerEvent implements IBlockDestroyEvent {
	private final OAPlayer player;
	private final OABlock block;
	private final BlockLocation blockLocation;
	
	public BukkitOreAnnouncerBlockDestroyEvent(OAPlayer player, OABlock block, BlockLocation blockLocation) {
		super(true);
		this.player = player;
		this.block = block;
		this.blockLocation = blockLocation;
	}
	
	@NotNull
	@Override
	public OAPlayer getPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public OABlock getBlock() {
		return block;
	}
	
	@NotNull
	@Override
	public BlockLocation getBlockLocation() {
		return blockLocation;
	}
}
