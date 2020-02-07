package com.alessiodp.oreannouncer.bungeecord.addons.external;

import com.alessiodp.core.bungeecord.addons.external.bstats.bungeecord.Metrics;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMetricsHandler extends MetricsHandler {
	public BungeeMetricsHandler(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		new Metrics((Plugin) plugin.getBootstrap(), plugin.getBstatsId());
	}
}
