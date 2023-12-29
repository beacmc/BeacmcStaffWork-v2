package com.beacmc.beacmcstaffwork.manager;


import com.beacmc.beacmcstaffwork.BeacmcStaffWork;

import java.util.List;

public class Config {


    public static String getString(String path) {
        assert BeacmcStaffWork.getInstance().getConfig().getString(path) != null;
        return BeacmcStaffWork.getInstance().getConfig().getString(path);
    }

    public static boolean getBoolean(String path) {
        return BeacmcStaffWork.getInstance().getConfig().getBoolean(path);
    }

    public static List<String> getStringList(String path) {
        assert BeacmcStaffWork.getInstance().getConfig().getStringList(path) != null;
        return BeacmcStaffWork.getInstance().getConfig().getStringList(path);
    }

    public static int getInt(String path) {
        assert BeacmcStaffWork.getInstance().getConfig().getString(path) != null;
        return BeacmcStaffWork.getInstance().getConfig().getInt(path);
    }

}
