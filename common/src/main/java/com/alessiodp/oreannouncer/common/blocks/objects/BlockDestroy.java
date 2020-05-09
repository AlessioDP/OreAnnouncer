package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.oreannouncer.api.interfaces.OABlockDestroy;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BlockDestroy implements OABlockDestroy {
	@Getter private final UUID player;
	@Getter private final String materialName;
	@Getter private int destroyCount;
}
