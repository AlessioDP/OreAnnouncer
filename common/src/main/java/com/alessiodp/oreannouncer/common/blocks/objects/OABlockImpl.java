package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

public class OABlockImpl implements OABlock {
	// Interface fields
	@Getter private final String materialName;
	@Getter @Setter private boolean alertingUsers;
	@Getter @Setter private boolean alertingAdmins;
	@Getter @Setter private String singularName;
	@Getter @Setter private String pluralName;
	@Getter @Setter private int lightLevel;
	@Getter @Setter private boolean countingOnDestroy;
	
	public OABlockImpl(@NonNull String materialName) {
		this.materialName = materialName;
		alertingUsers = false;
		alertingAdmins = false;
		singularName = "";
		pluralName = "";
		lightLevel = 15;
		countingOnDestroy = false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(materialName, alertingUsers, alertingAdmins, singularName, pluralName, lightLevel, countingOnDestroy);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this || other instanceof OABlockImpl) {
			return Objects.equals(materialName, ((OABlockImpl) other).materialName)
					&& alertingUsers == ((OABlockImpl) other).alertingUsers
					&& alertingAdmins == ((OABlockImpl) other).alertingAdmins
					&& Objects.equals(singularName, ((OABlockImpl) other).singularName)
					&& Objects.equals(pluralName, ((OABlockImpl) other).pluralName)
					&& lightLevel == ((OABlockImpl) other).lightLevel
					&& countingOnDestroy == ((OABlockImpl) other).countingOnDestroy;
		}
		return false;
	}
}
