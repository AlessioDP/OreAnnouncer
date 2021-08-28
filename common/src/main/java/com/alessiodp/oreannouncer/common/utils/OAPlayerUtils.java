package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.IPlayerUtils;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class OAPlayerUtils implements IPlayerUtils {
	private final OreAnnouncerPlugin plugin;
	
	@Override
	public Set<ADPCommand> getAllowedCommands(@NonNull User user) {
		Set<ADPCommand> ret = new HashSet<>();
		OAPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
		if (player != null) {
			ret = player.getAllowedCommands();
		}
		return ret;
	}
}
