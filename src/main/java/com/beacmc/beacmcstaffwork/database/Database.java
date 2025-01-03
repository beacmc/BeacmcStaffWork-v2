package com.beacmc.beacmcstaffwork.database;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.dao.WarnDao;
import com.beacmc.beacmcstaffwork.database.dao.impl.UserDaoImpl;
import com.beacmc.beacmcstaffwork.database.dao.impl.WarnDaoImpl;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableInfo;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {

    private ConnectionSource connectionSource;
    private UserDao userDao;
    private WarnDao warnDao;

    public Database() {}

    public void connect() {
        if (connectionSource == null)
            return;

        final MainConfiguration config = BeacmcStaffWork.getMainConfig();

        try {
            DatabaseType databaseType = DatabaseType.valueOf(config.getDatabaseSettings().getString("type", "SQLITE").toUpperCase());
            connectionSource = new JdbcConnectionSource(databaseType.getUrl(), config.getDatabaseSettings().getString("username"), config.getDatabaseSettings().getString("password"));
            userDao = new UserDaoImpl(connectionSource);
            warnDao = new WarnDaoImpl(connectionSource);
            TableUtils.createTableIfNotExists(connectionSource, User.class);

            migrate();
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

    private void migrate() throws SQLException {
        if (!columnExists("unbans"))
            userDao.executeRawAsync("ALTER TABLE `users` ADD COLUMN unbans INTEGER DEFAULT 0;");
        if (!columnExists("unmutes"))
            userDao.executeRawAsync("ALTER TABLE `users` ADD COLUMN unmutes INTEGER DEFAULT 0;");
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public WarnDao getWarnDao() {
        return warnDao;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
