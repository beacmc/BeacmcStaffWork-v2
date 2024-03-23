package com.beacmc.beacmcstaffwork.database.dao;

import com.beacmc.beacmcstaffwork.database.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    User queryForId(String id) throws SQLException;

    int update(User user) throws SQLException;

    Dao.CreateOrUpdateStatus createOrUpdate(User user) throws SQLException;

    List<User> queryForAll() throws SQLException;

    List<User> queryForEq(String fieldName, Object value) throws SQLException;


}
