package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import lombok.Getter;

import java.util.UUID;

public class BlockFound {
	@Getter private final UUID player;
	@Getter private final String materialName;
	@Getter private final long timestamp;
	@Getter private int found;
	
	public BlockFound(UUID player, OABlock block, int found) {
		this(player, block, System.currentTimeMillis() / 1000, found);
	}
	
	public BlockFound(UUID player, OABlock block, long timestamp, int found) {
		this(player, block.getMaterialName().toUpperCase(), timestamp, found);
	}
	
	public BlockFound(UUID player, String materialName, long timestamp, int found) {
		this.player = player;
		this.materialName = materialName;
		this.timestamp = timestamp;
		this.found = found;
	}
	
	public BlockFound merge(BlockFound blockFound) {
		if (materialName.equals(blockFound.getMaterialName()))
			return new BlockFound(
					player,
					materialName,
					Math.min(timestamp, blockFound.getTimestamp()),
					found + blockFound.getFound());
		return this;
	}
}
