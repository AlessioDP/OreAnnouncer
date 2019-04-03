package com.alessiodp.oreannouncer.common.commands;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.CommandManager;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.oreannouncer.common.commands.list.CommonCommands;
import com.alessiodp.oreannouncer.common.commands.utils.OACommandData;

public abstract class OACommandManager extends CommandManager {
	protected OACommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void prepareCommands() {
		CommonCommands.setup();
	}
	
	@Override
	public CommandData initializeCommandData() {
		return new OACommandData();
	}
}
