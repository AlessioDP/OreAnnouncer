package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BlockLocationImpl implements BlockLocation {
	@Getter private String world;
	@Getter private double x;
	@Getter private double y;
	@Getter private double z;
	@Getter private float yaw;
	@Getter private float pitch;
	
	public BlockLocationImpl(ADPLocation location) {
		world = location.getWorld();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}
}
