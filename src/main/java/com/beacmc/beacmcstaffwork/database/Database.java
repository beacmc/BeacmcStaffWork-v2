package com.beacmc.beacmcstaffwork.database;

import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.impl.UserDaoImpl;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {

    private ConnectionSource connectionSource;
    private UserDao userDao;

    public Database() {}

    public void connect() {
        assert connectionSource == null;
        try {
            DatabaseType databaseType = DatabaseType.valueOf(Config.getString("settings.data.type").toUpperCase());
            connectionSource = new JdbcConnectionSource(databaseType.getUrl(), Config.getString("settings.data.username"), Config.getString("settings.data.password"));
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            userDao = new UserDaoImpl(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
