package com.beacmc.beacmcstaffwork.manager.configuration;


import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    private static FileConfiguration config;

    public static void reload() {
        config = BeacmcStaffWork.getInstance().getConfig();
    }
    public static String getString(String path) {
        assert config.getString(path) != null;
        return config.getString(path);
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public static List<String> getStringList(String path) {
        assert config.getStringList(path) != null;
        return config.getStringList(path);
    }
    public static Integer getInteger(String path) {
        return config.getInt(path);
    }

    public static int getInt(String path) {
        assert config.getString(path) != null;
        return config.getInt(path);
    }

    static {
           config = BeacmcStaffWork.getInstance().getConfig();
    }
}
