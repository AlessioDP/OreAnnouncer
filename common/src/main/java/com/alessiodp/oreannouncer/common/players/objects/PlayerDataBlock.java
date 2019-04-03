package com.alessiodp.oreannouncer.common.players.objects;

import com.alessiodp.oreannouncer.api.interfaces.OAPlayerDataBlock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerDataBlock implements OAPlayerDataBlock {
	@Getter @Setter private String materialName;
	@Getter @Setter private UUID player;
	@Getter @Setter private int destroyCount;
	
	@Override
	public int hashCode() {
		return Objects.hash(materialName, player, destroyCount);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this || other instanceof PlayerDataBlock) {
			return Objects.equals(materialName, ((PlayerDataBlock) other).materialName)
					&& Objects.equals(player, ((PlayerDataBlock) other).player)
					&& destroyCount == ((PlayerDataBlock) other).destroyCount;
		}
		return false;
	}
}
