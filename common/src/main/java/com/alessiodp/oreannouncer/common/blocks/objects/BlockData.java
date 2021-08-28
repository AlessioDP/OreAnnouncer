package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class BlockData implements Serializable {
	private final OAPlayerImpl player;
	private final OABlockImpl block;
	private int number;
	
	private boolean alertUsers;
	private boolean alertAdmins;
	private ADPLocation location;
	private int lightLevel;
	private int heightLevel;
	private long elapsed = -1L;
	
	public BlockData(OAPlayerImpl player, OABlockImpl block, int number) {
		this.player = player;
		this.block = block;
		this.number = number;
	}
}
