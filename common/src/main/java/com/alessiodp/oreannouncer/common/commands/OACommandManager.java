package com.alessiodp.oreannouncer.common.commands;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.CommandManager;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;

import java.util.LinkedList;

public abstract class OACommandManager extends CommandManager {
	protected OACommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void prepareCommands() {
		commandOrder = new LinkedList<>();
		commandOrder.addAll(ConfigMain.COMMANDS_ORDER);
	}
	
	@Override
	public CommandData initializeCommandData() {
		return new OACommandData();
	}
}
