package com.beacmc.beacmcstaffwork.util;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class Message {

    public static String fromConfig(String path) {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        return of(config.getRealConfig().getString(path));
    }

    public static String getMessageFromConfig(String key) {
        return fromConfig("settings.messages." + key);
    }

    public static String of(String message) {
        final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();

        return Color.compile(message
                .replace("{PREFIX}", settings.getString("prefix")));
    }
}
