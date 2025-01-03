package com.beacmc.beacmcstaffwork.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfiguration {

    protected final File file;
    protected YamlConfiguration config;

    public MainConfiguration(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigurationSection getSettings() {
        return config.getConfigurationSection("settings");
    }

    public ConfigurationSection getMessages() {
        return getSettings().getConfigurationSection("messages");
    }

    public ConfigurationSection getDatabaseSettings() {
        return getSettings().getConfigurationSection("data");
    }

    public ConfigurationSection getDiscordSettings() {
        return getSettings().getConfigurationSection("discord");
    }

    public ConfigurationSection getStaffPanelSettings() {
        return getSettings().getConfigurationSection("menu");
    }

    public ConfigurationSection getWorkLimitsSettings() {
        return getSettings().getConfigurationSection("work");
    }

    public ConfigurationSection getPlaceholderAPISettings() {
        return getSettings().getConfigurationSection("placeholderapi");
    }

    public ConfigurationSection getActions() {
        return getSettings().getConfigurationSection("actions");
    }

    public ConfigurationSection getWarnsSettings() {
        return getSettings().getConfigurationSection("warns");
    }

    public ConfigurationSection getVerbalWarnSettings() {
        return getWarnsSettings().getConfigurationSection("verbal");
    }

    public ConfigurationSection getSevereWarnSettings() {
        return getWarnsSettings().getConfigurationSection("severe");
    }

    public YamlConfiguration getRealConfig() {
        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
