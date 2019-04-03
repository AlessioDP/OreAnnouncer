package com.alessiodp.oreannouncer.bukkit.commands;

import com.alessiodp.core.bukkit.commands.utils.BukkitCommandUtils;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bukkit.commands.main.BukkitCommandOA;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.OACommandManager;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;

import java.util.ArrayList;

public class BukkitOACommandManager extends OACommandManager {
	
	public BukkitOACommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void prepareCommands() {
		commandOrder = null; // Command order disabled
		commandUtils = new BukkitCommandUtils(plugin, ConfigMain.COMMANDS_SUB_ON, ConfigMain.COMMANDS_SUB_OFF);
		super.prepareCommands();
	}
	
	@Override
	protected void registerCommands() {
		mainCommands = new ArrayList<>();
		mainCommands.add(new BukkitCommandOA((OreAnnouncerPlugin) plugin));
	}
}
