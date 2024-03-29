package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.bukkit.addons.external.bstats.bukkit.Metrics;
import com.alessiodp.core.bukkit.addons.external.bstats.charts.SimplePie;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMetricsHandler extends MetricsHandler {
	public BukkitMetricsHandler(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		Metrics metrics = new Metrics((JavaPlugin) plugin.getBootstrap(), plugin.getBstatsId());
		
		metrics.addCustomChart(new SimplePie("type_of_announce_used", () -> {
			if (ConfigMain.ALERTS_ENABLE) {
				if (ConfigMain.ALERTS_COORDINATES_ENABLE)
					return "Coordinates";
				return "Normal";
			}
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("use_randomizer", () -> {
			if (ConfigMain.ALERTS_COORDINATES_ENABLE && ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("use_statistics", () -> {
			if (ConfigMain.STATS_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("type_of_database_used", () -> plugin.getDatabaseManager().getDatabaseType().getFormattedName()));
	}
}
