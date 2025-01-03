package com.beacmc.beacmcstaffwork.util.metrics;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bstats.charts.SimplePie;

public class MetricBuilder {

    public void build(int pluginId) {
        Metrics metrics = new Metrics(BeacmcStaffWork.getInstance(), pluginId);

        metrics.addCustomChart(new SimplePie("discord_enabled", () ->
            BeacmcStaffWork.getDiscordBot() != null ? "true" : "false"
        ));

        metrics.addCustomChart(new SimplePie("proxy_mode", () -> {
            String result = BeacmcStaffWork.getMainConfig().getSettings().getString("proxy");
            return result != null ? result : "false";
        }));
    }
}
