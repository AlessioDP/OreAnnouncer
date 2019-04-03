package com.alessiodp.oreannouncer.common.commands.utils;

import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.Getter;
import lombok.Setter;

public class OACommandData extends CommandData {
	@Getter @Setter private OAPlayerImpl player;
	
	public OACommandData() {
		super();
	}
}
