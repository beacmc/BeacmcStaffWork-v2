package com.beacmc.beacmcstaffwork.manager.player;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class StaffPlayer {


    private final LuckPerms luckPerms = BeacmcStaffWork.getLuckPerms();
    private final Player player;
    private static User user;
    private final Database database;



    public StaffPlayer(Player player) {
        this.player = player;
        database = BeacmcStaffWork.getDatabase();
        try {
            user = database.getUserDao().queryForId(player.getName().toLowerCase());
        } catch (SQLException e) { }
    }

    public String getName() {
        return player.getName();
    }

    public boolean isWork() {
        if(!isModerator())
            return false;
        return user.isWork();
    }

    public void stopWork() {
        if(!this.hasPermission("beacmcstaffwork.use") && !this.isWork())
            return;

        try {
            long time = (System.currentTimeMillis() - user.getWorkStart()) / 1000;
            database.getUserDao().update(user.setTime(time + user.getTime()).setWork(false).setWorkStart(0));
            BeacmcStaffWork.getUsers().remove(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startWork() {
        if(!this.hasPermission("beacmcstaffwork.use") || this.isWork())
            return;

        try {

            if(!isModerator()) {
                user = new User(player.getName().toLowerCase(), null, true, 0, 0, 0, 0, 0, 0);
                database.getUserDao().createOrUpdate(user);
            }
            database.getUserDao().update(user.setWorkStart(System.currentTimeMillis()).setWork(true));
            BeacmcStaffWork.getUsers().add(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isModerator() {
        return user != null;
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

    public Set<String> getUserGroups() {
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return Collections.emptySet();
        }

        return user.getInheritedGroups(user.getQueryOptions())
                .stream()
                .map(net.luckperms.api.model.group.Group::getName)
                .collect(Collectors.toSet());
    }


    public void sendMessage(String path) {
        if(Config.getString(path) == null || Config.getString(path).isEmpty())
            return;

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

    public static StaffPlayer getStaffPlayerByName(String name) {
        return new StaffPlayer(Bukkit.getPlayer(name));
    }

    public User getUser() {
        return user;
    }
}
