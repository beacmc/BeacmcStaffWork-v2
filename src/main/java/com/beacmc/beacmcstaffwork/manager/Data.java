package com.beacmc.beacmcstaffwork.manager;

import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Data {


    public static void addBan(User user) {
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

    public static void addMute(User user) {
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


    public static void addKick(User user) {
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

    public static void resetTime(String player) {
        if(!isModerator(player))
            return;
        String sql = "UPDATE staff SET time = 0 WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetBans(String player) {
        if(!isModerator(player))
            return;
        String sql = "UPDATE staff SET bans = 0 WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetMutes(String player) {
        if(!isModerator(player))
            return;
        String sql = "UPDATE staff SET mutes = 0 WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetKicks(String player) {
        if(!isModerator(player))
            return;
        String sql = "UPDATE staff SET kicks = 0 WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.toLowerCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isModerator(String player) {
        String sql = "SELECT COUNT(*) FROM staff WHERE nickname = ?";


        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
