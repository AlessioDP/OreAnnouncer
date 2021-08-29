package com.alessiodp.oreannouncer.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.commands.sub.CommandDebug;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitCommandDebug extends CommandDebug {
	
	public BukkitCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected ADPLocation getPlayerLocation(OAPlayerImpl player) {
		Player bukkitPlayer = Bukkit.getPlayer(player.getPlayerUUID());
		if (bukkitPlayer != null)
			return new ADPLocation(
					bukkitPlayer.getLocation().getWorld() != null ? bukkitPlayer.getLocation().getWorld().getName() : "",
					bukkitPlayer.getLocation().getX(),
					bukkitPlayer.getLocation().getY(),
					bukkitPlayer.getLocation().getZ(),
					bukkitPlayer.getLocation().getYaw(),
					bukkitPlayer.getLocation().getPitch()
			);
		return super.getPlayerLocation(player);
	}
	
	@Override
	protected int getPlayerLightLevel(OAPlayerImpl player) {
		Player bukkitPlayer = Bukkit.getPlayer(player.getPlayerUUID());
		if (bukkitPlayer != null)
			return bukkitPlayer.getLocation().getBlock().getLightLevel();
		return super.getPlayerLightLevel(player);
	}
}
