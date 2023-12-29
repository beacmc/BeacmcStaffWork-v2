package com.beacmc.beacmcstaffwork.manager;

import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Data {

    private static User user;

    public Data(User user) {
        this.user = user;
    }




    public void addBan() {
        if(!user.isWork())
            return;

        String sql = "UPDATE staff SET bans = ? WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            int i = user.getBans();
            preparedStatement.setInt(1, i + 1);
            preparedStatement.setString(2, user.getName().toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMute() {
        if(!user.isWork())
            return;

        String sql = "UPDATE staff SET mutes = ? WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            int i = user.getMutes();
            preparedStatement.setInt(1, i + 1);
            preparedStatement.setString(2, user.getName().toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addKick() {
        if(!user.isWork())
            return;

        String sql = "UPDATE staff SET kicks = ? WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            int i = user.getKicks();
            preparedStatement.setInt(1, i + 1);
            preparedStatement.setString(2, user.getName().toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
