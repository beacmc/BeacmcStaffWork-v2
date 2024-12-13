package com.beacmc.beacmcstaffwork.util.metrics;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.Config;
import org.bstats.charts.SimplePie;

public class MetricBuilder {

    public void build(int pluginId) {
        Metrics metrics = new Metrics(BeacmcStaffWork.getInstance(), pluginId);

        metrics.addCustomChart(new SimplePie("discord_enabled", () ->
            BeacmcStaffWork.getDiscordBot() != null ? "true" : "false"
        ));

        metrics.addCustomChart(new SimplePie("proxy_mode", () -> {
            String result = Config.getString("settings.proxy");
            return result != null ? result : "false";
        }));
    }
}
