package com.beacmc.beacmcstaffwork.database.dao;

import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.database.model.Warn;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WarnDao {

    CompletableFuture<Warn> queryForIdAsync(Integer id);

    CompletableFuture<Warn> updateAsync(Warn warn);

    CompletableFuture<Void> createOrUpdateAsync(Warn warn);

    CompletableFuture<Void> createAsync(Warn warn);

    CompletableFuture<List<Warn>> queryForAllAsync();

    CompletableFuture<List<Warn>> queryForEqAsync(String fieldName, Object value);

    CompletableFuture<Void> executeRawAsync(String var1, String... var2);

    TableInfo<Warn, Integer> getTableInfo();

    CompletableFuture<Void> deleteAsync(Warn warn);
}
