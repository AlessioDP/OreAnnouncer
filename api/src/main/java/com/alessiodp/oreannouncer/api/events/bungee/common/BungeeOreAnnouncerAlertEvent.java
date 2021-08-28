package com.alessiodp.oreannouncer.api.events.bungee.common;

import com.alessiodp.oreannouncer.api.events.bungee.BungeeOreAnnouncerEvent;
import com.alessiodp.oreannouncer.api.events.common.IAlertEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import org.jetbrains.annotations.NotNull;

public class BungeeOreAnnouncerAlertEvent extends BungeeOreAnnouncerEvent implements IAlertEvent {
	private final OAPlayer player;
	private final OABlock block;
	private final int number;
	private final BlockLocation location;
	private final int lightLevel;
	private final int heightLevel;
	
	public BungeeOreAnnouncerAlertEvent(OAPlayer player, OABlock block, int number, BlockLocation location, int lightLevel, int heightLevel) {
		this.player = player;
		this.block = block;
		this.number = number;
		this.location = location;
		this.lightLevel = lightLevel;
		this.heightLevel = heightLevel;
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
	
	@Override
	public int getNumberOfBlocks() {
		return number;
	}
	
	@Override
	public BlockLocation getLocation() {
		return location;
	}
	
	@Override
	public int getLightLevel() {
		return lightLevel;
	}
	
	@Override
	public int getHeightLevel() {
		return heightLevel;
	}
}
