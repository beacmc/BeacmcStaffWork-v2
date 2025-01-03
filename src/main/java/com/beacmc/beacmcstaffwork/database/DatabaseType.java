package com.beacmc.beacmcstaffwork.database;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.configuration.ConfigurationSection;

public enum DatabaseType {

    SQLITE {

       final BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();

        @Override
        public String getUrl() {
            return "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/beacmcstaffwork.db";
        }
    },

    MYSQL {

        final ConfigurationSection databaseSettings = BeacmcStaffWork.getMainConfig().getDatabaseSettings();

        @Override
        public String getUrl() {
            return "jdbc:mysql://" + databaseSettings.getString("host") + "/" + databaseSettings.getString("database");
        }
    },

    MARIADB {

        final ConfigurationSection databaseSettings = BeacmcStaffWork.getMainConfig().getDatabaseSettings();

        @Override
        public String getUrl() {
            return "jdbc:mariadb://" + databaseSettings.getString("host") + "/" + databaseSettings.getString("database");
        }
    },
    POSTGRESQL {

        final ConfigurationSection databaseSettings = BeacmcStaffWork.getMainConfig().getDatabaseSettings();

        @Override
        public String getUrl() {
            return "jdbc:postgresql://" + databaseSettings.getString("host") + "/" + databaseSettings.getString("database");
        }
    };


    public abstract String getUrl();
}
