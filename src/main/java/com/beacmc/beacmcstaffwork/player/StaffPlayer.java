package com.beacmc.beacmcstaffwork.player;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StaffPlayer {


    private final LuckPerms luckPerms;
    private final StaffWorkManager staffWorkManager;
    private final Player player;
    private final Database database;
    private User user;

    public StaffPlayer(Player player, User user) {
        this.database = BeacmcStaffWork.getDatabase();
        this.staffWorkManager = BeacmcStaffWork.getStaffWorkManager();
        this.luckPerms = BeacmcStaffWork.getLuckPerms();
        this.player = player;
        this.user = user;
    }

    public String getName() {
        return player.getName();
    }

    public boolean isWork() {
        return user.isWork() && isModerator();
    }

    public String getIPAddress() {
        return player.getAddress() != null ? player.getAddress().getHostName() : null;
    }

    public void stopWork() {
        if (!this.isWork())
            return;

        long time = (System.currentTimeMillis() - user.getWorkStart()) / 1000;
        database.getUserDao().updateAsync(user.setTime(time + user.getTime()).setWork(false).setWorkStart(0));

        staffWorkManager.updatePlayer(this);
    }

    public void startWork() {
        if (!this.hasPermission("beacmcstaffwork.use") || this.isWork())
            return;

        if (!isModerator()) {
            user = new User(player.getName().toLowerCase(), null, true, 0, 0, 0, 0, 0, 0, 0, 0);
            database.getUserDao().createOrUpdateAsync(user);
        }
        database.getUserDao().updateAsync(user.setWorkStart(System.currentTimeMillis()).setWork(true));
        staffWorkManager.getStaffPlayers().add(this);
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
        return luckPerms.getPlayerAdapter(Player.class)
                .getUser(player)
                .getPrimaryGroup();
    }

    public void sendMessage(String message) {
        if (message != null && !message.isEmpty())
            player.sendMessage(message);
    }

    public void sendTitle(String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 10, 10);
    }

    public void sendMessage(List<String> message) {
        if (message != null && !message.isEmpty())
            message.forEach(player::sendMessage);
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StaffPlayer that = (StaffPlayer) obj;
        return Objects.equals(getID(), that.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
