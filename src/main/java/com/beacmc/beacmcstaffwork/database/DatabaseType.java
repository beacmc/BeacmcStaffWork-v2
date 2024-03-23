package com.beacmc.beacmcstaffwork.database;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;

public enum DatabaseType {

    SQLITE("jdbc:sqlite:" + BeacmcStaffWork.getInstance().getDataFolder().getAbsolutePath() + "/beacmcstaffwork.db"),

    MYSQL("jdbc:mysql://" + Config.getString("settings.data.host") + "/" + Config.getString("settings.data.database")),

    MARIADB("jdbc:mariadb://" + Config.getString("settings.data.host") + "/" + Config.getString("settings.data.database")),
    POSTGRESQL("jdbc:postgresql://" + Config.getString("settings.data.host") + "/" + Config.getString("settings.data.database"));


    DatabaseType(String url) {
        this.url = url;
    }

    private final String url;

    public String getUrl() {
        return url;
    }
}
