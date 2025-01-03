package com.beacmc.beacmcstaffwork.database.dao.impl;

import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao {

    public UserDaoImpl(ConnectionSource source) throws SQLException {
        super(source, User.class);
    }

    public CompletableFuture<User> updateAsync(User data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                update(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        });
    }

    public CompletableFuture<User> queryForIdAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<List<User>> queryForEqAsync(String field, Object value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryForEq(field, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        });
    }

    public CompletableFuture<Void> createOrUpdateAsync(User data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                createOrUpdate(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> deleteAsync(User data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                delete(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> executeRawAsync(String arg, String... args) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                executeRaw(arg, args);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<List<User>> queryForAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
