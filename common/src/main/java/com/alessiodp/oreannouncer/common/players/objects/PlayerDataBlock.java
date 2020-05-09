package com.alessiodp.oreannouncer.common.players.objects;

import com.alessiodp.oreannouncer.api.interfaces.OAPlayerDataBlock;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerDataBlock implements OAPlayerDataBlock {
	@Getter @Setter private String materialName;
	@Getter @Setter private UUID player;
	@Getter @Setter private int destroyCount;
}
