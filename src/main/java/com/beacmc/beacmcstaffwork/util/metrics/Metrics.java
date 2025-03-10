package com.beacmc.beacmcstaffwork.util.metrics;

import org.bstats.MetricsBase;
import org.bstats.charts.CustomChart;
import org.bstats.json.JsonObjectBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Metrics {

    private final Plugin plugin;
    private final MetricsBase metricsBase;

    public Metrics(Plugin plugin, int serviceId) {
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            config.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);

            try {
                config.save(configFile);
            } catch (IOException var14) {
            }
        }

        boolean enabled = config.getBoolean("enabled", true);
        String serverUUID = config.getString("serverUuid");
        boolean logErrors = config.getBoolean("logFailedRequests", false);
        boolean logSentData = config.getBoolean("logSentData", false);
        boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        boolean isFolia = false;

        try {
            isFolia = Class.forName("io.papermc.paper.threadedregions.RegionizedServer") != null;
        } catch (Exception var13) {
        }

        Objects.requireNonNull(plugin);
        this.metricsBase = new MetricsBase("bukkit", serverUUID, serviceId, enabled, this::appendPlatformData, this::appendServiceData, isFolia ? null : (submitDataTask) -> Bukkit.getScheduler().runTask(plugin, submitDataTask), plugin::isEnabled, (message, error) -> {
            this.plugin.getLogger().log(Level.WARNING, message, error);
        }, (message) -> {
            this.plugin.getLogger().log(Level.INFO, message);
        }, logErrors, logSentData, logResponseStatusText, true);
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", this.getPlayerAmount());
        builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", Bukkit.getVersion());
        builder.appendField("bukkitName", Bukkit.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }

    private int getPlayerAmount() {
        try {
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            return onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer())).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer())).length;
        } catch (Exception var2) {
            return Bukkit.getOnlinePlayers().size();
        }
    }
}
