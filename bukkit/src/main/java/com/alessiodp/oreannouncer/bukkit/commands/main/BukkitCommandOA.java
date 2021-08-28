package com.alessiodp.oreannouncer.bukkit.commands.main;

import com.alessiodp.oreannouncer.bukkit.commands.sub.BukkitCommandDebug;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.main.CommandOA;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;

public class BukkitCommandOA extends CommandOA {
	public BukkitCommandOA(OreAnnouncerPlugin plugin) {
		super(plugin);
		
		description = BukkitConfigMain.COMMANDS_MAIN_OA_DESCRIPTION;
		
		if (ConfigMain.OREANNOUNCER_DEBUG_COMMAND)
			register(new BukkitCommandDebug(plugin, this));
	}
}
