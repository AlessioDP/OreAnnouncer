package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.api.interfaces.OABlockFound;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@ToString
public class BlockFound implements OABlockFound {
	@Getter private final UUID player;
	@Getter private final String materialName;
	@Getter private final long timestamp;
	@Getter private final int found;
	
	public BlockFound(UUID player, String materialName, int found) {
		this(player, materialName, System.currentTimeMillis() / 1000, found);
	}
	
	public BlockFound(UUID player, OABlock block, int found) {
		this(player, block, System.currentTimeMillis() / 1000, found);
	}
	
	public BlockFound(UUID player, OABlock block, long timestamp, int found) {
		this(player, CommonUtils.toUpperCase(block.getMaterialName()), timestamp, found);
	}
}
