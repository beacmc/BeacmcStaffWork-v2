package com.beacmc.beacmcstaffwork.database.dao;

import com.beacmc.beacmcstaffwork.database.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserDao {

    CompletableFuture<User> queryForIdAsync(String id);

    CompletableFuture<User> updateAsync(User user);

    CompletableFuture<Void> createOrUpdateAsync(User user);

    CompletableFuture<List<User>> queryForAllAsync();

    CompletableFuture<List<User>> queryForEqAsync(String fieldName, Object value);

    CompletableFuture<Void> executeRawAsync(String var1, String... var2);

    TableInfo<User, String> getTableInfo();

    CompletableFuture<Void> deleteAsync(User user);

    QueryBuilder<User, String> queryBuilder();
}
