package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.bukkit.addons.external.bstats.bukkit.Metrics;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class BukkitMetricsHandler extends MetricsHandler {
	public BukkitMetricsHandler(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void registerMetrics() {
		Metrics metrics = new Metrics((Plugin) plugin.getBootstrap());
		
		metrics.addCustomChart(new Metrics.SimplePie("type_of_announce_used", () -> {
			if (ConfigMain.ALERTS_ENABLE) {
				if (ConfigMain.ALERTS_COORDINATES_ENABLE)
					return "Coordinates";
				return "Normal";
			}
			return "Disabled";
		}));
		
		metrics.addCustomChart(new Metrics.SimplePie("use_randomizer", () -> {
			if (ConfigMain.ALERTS_COORDINATES_ENABLE && ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new Metrics.SimplePie("use_statistics", () -> {
			if (ConfigMain.STATS_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new Metrics.SimplePie("type_of_database_used", () -> plugin.getDatabaseManager().getDatabaseType().getFormattedName()));
	}
}
