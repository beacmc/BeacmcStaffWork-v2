package com.beacmc.beacmcstaffwork.data.sql;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.Config;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLBuilder {

    private static Connection connection;
    private static Statement statement;

    private static String urlConnection = null;

    public SQLBuilder() {

        String type = Config.getString("settings.data.type").toLowerCase();
        String host = Config.getString("settings.data.host");
        String database = Config.getString("settings.data.database");
        String user = Config.getString("settings.data.username");
        String password = Config.getString("settings.data.password");


        switch (type) {
            case "mysql": {
                urlConnection = "jdbc:mysql://" + host + "/" + database;
                break;
            }
            case "sqlite": {
                urlConnection = "jdbc:sqlite:" + BeacmcStaffWork.getInstance().getDataFolder().getAbsolutePath() + "/database.db";
                break;
            }
            default: {
                Bukkit.getLogger().severe("Неизвестный тип базы данных. Плагин принудительно остановлен.");
                Bukkit.getPluginManager().disablePlugin(BeacmcStaffWork.getInstance());
                return;

            }
        }


        try {
            if(urlConnection == null)
                return;

            if(type == "mysql") {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            connection = DriverManager.getConnection(urlConnection, user, password);

            SQLManager.createTable();
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Statement getStatement() {
        return statement;
    }

}

