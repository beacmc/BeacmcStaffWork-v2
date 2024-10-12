package com.beacmc.beacmcstaffwork.database;

import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.impl.UserDaoImpl;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableInfo;
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
            userDao = new UserDaoImpl(connectionSource);
            TableUtils.createTableIfNotExists(connectionSource, User.class);

            if (!columnExists("unbans"))
                userDao.executeRaw("ALTER TABLE `users` ADD COLUMN unbans INTEGER DEFAULT 0;");
            if (!columnExists("unmutes"))
                userDao.executeRaw("ALTER TABLE `users` ADD COLUMN unmutes INTEGER DEFAULT 0;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean columnExists(String columnName) {
        final TableInfo<User, String> tableInfo = userDao.getTableInfo();
        final FieldType[] types = tableInfo.getFieldTypes();

        for (FieldType type : types) {
            if (type.getColumnName().equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
