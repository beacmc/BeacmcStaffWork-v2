package com.beacmc.beacmcstaffwork.data.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS staff (nickname TEXT NOT NULL, worked BOOLEAN, work_start BIGINT, time BIGINT, bans INT, mutes INT, kicks INT);";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            SQLBuilder.getConnection().close();
        }  catch (SQLException e) { }
    }



}
