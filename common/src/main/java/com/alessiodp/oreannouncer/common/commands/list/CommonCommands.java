package com.alessiodp.oreannouncer.common.commands.list;

import com.alessiodp.core.common.commands.list.ADPCommand;

public enum CommonCommands implements ADPCommand {
	OA,
	
	ALERTS,
	HELP,
	DEBUG,
	LOG,
	RELOAD,
	STATS,
	TOP,
	VERSION,
	WHITELIST;
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
