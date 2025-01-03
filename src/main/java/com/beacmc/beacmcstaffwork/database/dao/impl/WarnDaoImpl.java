package com.beacmc.beacmcstaffwork.database.dao.impl;

import com.beacmc.beacmcstaffwork.database.dao.WarnDao;
import com.beacmc.beacmcstaffwork.database.model.Warn;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarnDaoImpl extends BaseDaoImpl<Warn, Integer> implements WarnDao {


    public WarnDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Warn.class);
    }

    public CompletableFuture<Warn> updateAsync(Warn data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                update(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        });
    }

    public CompletableFuture<Warn> queryForIdAsync(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<List<Warn>> queryForEqAsync(String field, Object value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryForEq(field, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        });
    }

    public CompletableFuture<Void> createOrUpdateAsync(Warn data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                createOrUpdate(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> createAsync(Warn data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                create(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> deleteAsync(Warn data) {
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

    public CompletableFuture<List<Warn>> queryForAllAsync() {
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
