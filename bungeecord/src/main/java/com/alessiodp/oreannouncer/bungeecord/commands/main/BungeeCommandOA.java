package com.alessiodp.oreannouncer.bungeecord.commands.main;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.main.CommandOA;
import com.alessiodp.oreannouncer.common.commands.sub.CommandDebug;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;

public class BungeeCommandOA extends CommandOA {
	public BungeeCommandOA(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		if (ConfigMain.OREANNOUNCER_DEBUG_COMMAND)
			register(new CommandDebug(plugin, this));
	}
}
