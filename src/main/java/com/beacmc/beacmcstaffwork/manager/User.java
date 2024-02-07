package com.beacmc.beacmcstaffwork.manager;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class User {


    private final LuckPerms luckPerms = BeacmcStaffWork.getLuckPerms();


    private final Player player;



    public User(Player player) {
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }

    public boolean isWork() {
        String sql = "SELECT worked FROM staff WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, this.getName().toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("worked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stopWork() {
        if(!this.hasPermission("beacmcstaffwork.use") && !this.isWork())
            return;


        String sql = "UPDATE staff SET worked = false, time = ? WHERE nickname = ?";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(2, player.getName().toLowerCase());

            long time = (System.currentTimeMillis() - this.getStartWork()) / 1000;

            preparedStatement.setLong(1, time + this.getTime());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startWork() {
        if(!this.hasPermission("beacmcstaffwork.use") && this.isWork())
            return;

        try {
            if(!isModerator()) {
                PreparedStatement preparedStatement = SQLBuilder.getConnection()
                        .prepareStatement("INSERT INTO staff (nickname, worked, work_start, time, bans, mutes, kicks) VALUES (?, false, ?, 0, 0, 0, 0)");
                preparedStatement.setString(1, player.getName().toLowerCase());
                preparedStatement.setLong(2, System.currentTimeMillis());
                preparedStatement.execute();
            }
            PreparedStatement pr = SQLBuilder.getConnection()
                    .prepareStatement("UPDATE staff SET worked = true, work_start = ? WHERE nickname = ?");
            pr.setString(2, player.getName().toLowerCase());
            pr.setLong(1, System.currentTimeMillis());
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isModerator() {
        String sql = "SELECT COUNT(*) FROM staff WHERE nickname = ?";

        if(!this.hasPermission("beacmcstaffwork.use"))
            return false;
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, player.getName().toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }


    public UUID getID() {
        return player.getUniqueId();
    }


    public Player getPlayer() {
        return player;
    }


    public boolean hasPermission(String perm) {
        return player.hasPermission(perm);
    }



    public boolean isOnline() {
        return player.isOnline();
    }



    public String getPrimaryGroup() {
        net.luckperms.api.model.user.User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        return user.getPrimaryGroup();
    }


    public void sendMessage(String path) {
        if(Config.getString(path) == null) return;
        player.sendMessage(Color.compile(Config.getString(path)
                .replace("{PREFIX}", Config.getString("settings.prefix"))
        ));
    }



    public void sendTitle(String title, String subtitle) {
        player.sendTitle(Color.compile(Config.getString(title)), Color.compile(Config.getString(subtitle)),
        10, 10, 10);
    }


    public void sendMessageList(String path) {
        ArrayList<String> lines = new ArrayList<>(Config.getStringList(path));
        for (String execute : lines) {
            player.sendMessage(Color.compile(execute));
        }
    }

    public static User getUserByName(String name) {
        return new User(Bukkit.getPlayer(name));
    }














    public long getTime() {
        String sql = "SELECT time FROM staff WHERE nickname = '" + this.getName().toLowerCase() + "'";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getLong("time");
            }
        } catch (SQLException e) { }
        return 0;
    }

    public long getStartWork() {
        String sql = "SELECT work_start FROM staff WHERE nickname = '" + this.getName().toLowerCase() + "'";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getLong("work_start");
            }
        } catch (SQLException e) { }
        return 0;
    }

    public int getBans() {
        String sql = "SELECT bans FROM staff WHERE nickname = '" + this.getName().toLowerCase() + "'";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("bans");
            }
        } catch (SQLException e) { }
        return 0;
    }

    public int getMutes() {
        String sql = "SELECT mutes FROM staff WHERE nickname = '" + this.getName().toLowerCase() + "'";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("mutes");
            }
        } catch (SQLException e) { }
        return 0;
    }

    public int getKicks() {
        String sql = "SELECT kicks FROM staff WHERE nickname = '" + this.getName().toLowerCase() + "'";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("kicks");
            }
        } catch (SQLException e) { }
        return 0;
    }
}
