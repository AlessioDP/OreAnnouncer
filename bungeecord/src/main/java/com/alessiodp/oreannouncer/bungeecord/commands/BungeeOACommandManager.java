package com.alessiodp.oreannouncer.bungeecord.commands;

import com.alessiodp.core.bungeecord.commands.utils.BungeeCommandUtils;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.bungeecord.commands.main.BungeeCommandOA;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.commands.OACommandManager;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;

import java.util.ArrayList;

public class BungeeOACommandManager extends OACommandManager {
	
	public BungeeOACommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void prepareCommands() {
		commandOrder = null; // Command order disabled
		commandUtils = new BungeeCommandUtils(plugin, ConfigMain.COMMANDS_SUB_ON, ConfigMain.COMMANDS_SUB_OFF);
		super.prepareCommands();
	}
	
	@Override
	protected void registerCommands() {
		mainCommands = new ArrayList<>();
		mainCommands.add(new BungeeCommandOA((OreAnnouncerPlugin) plugin));
	}
}
