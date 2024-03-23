package com.beacmc.beacmcstaffwork.database.impl;

import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao {

    public UserDaoImpl(ConnectionSource source) throws SQLException {
        super(source, User.class);
    }

    public CreateOrUpdateStatus createOrUpdate(User user) throws SQLException {
        User existingUser = queryForId(user.getNickname());
        if (existingUser == null) {
            super.create(user);
        } else {
            super.update(user);
        }
        return null;
    }
}
