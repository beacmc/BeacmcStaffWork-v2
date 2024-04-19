package com.beacmc.beacmcstaffwork.util;

import com.beacmc.beacmcstaffwork.manager.configuration.Config;

public class Message {

    public static String fromConfig(String path) {
        return Color.compile(Config.getString(path)
                .replace("{PREFIX}", Config.getString("settings.prefix")));
    }

    public static String of(String message) {
        return Color.compile(message
                .replace("{PREFIX}", Config.getString("settings.prefix")));
    }
}
