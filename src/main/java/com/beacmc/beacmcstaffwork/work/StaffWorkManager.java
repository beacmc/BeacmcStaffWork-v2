package com.beacmc.beacmcstaffwork.work;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class StaffWorkManager {

    private final Set<StaffPlayer> staffPlayers;
    private final ActionManager action;
    private final UserDao userDao;

    public StaffWorkManager() {
        staffPlayers = new HashSet<>();
        userDao = BeacmcStaffWork.getDatabase().getUserDao();
        action = BeacmcStaffWork.getActionManager();
    }

    public void onJoin(Player player) {
        StaffPlayer staffPlayer = findStaffPlayer(player);
        if(staffPlayer != null)
            staffPlayers.add(staffPlayer);
    }

    public void onLeave(Player player) {
        final ConfigurationSection workLimitsSettings = BeacmcStaffWork.getMainConfig().getWorkLimitsSettings();
        final ConfigurationSection actions = BeacmcStaffWork.getMainConfig().getActions();

        if(!workLimitsSettings.getBoolean("disable-work-on-quit"))
            return;

        if(workLimitsSettings.getBoolean("enable-bypass-permission") && player.hasPermission("beacmcstaffwork.work-limits.bypass"))
            return;

        if (isWork(player)) {
            StaffPlayer staffPlayer = getStaffPlayerByPlayer(player);
            PlayerDisableWorkEvent playerDisableWorkEvent = new PlayerDisableWorkEvent(staffPlayer);
            Bukkit.getPluginManager().callEvent(playerDisableWorkEvent);
            if (!playerDisableWorkEvent.isCancelled()) {
                action.execute(staffPlayer.getPlayer(), actions.getStringList(staffPlayer.getPrimaryGroup() + ".disable-work"));
                staffPlayer.stopWork();
                staffPlayers.remove(staffPlayer);
            }
        } else if (isOnline(player)) {
            StaffPlayer staffPlayer = getStaffPlayerByPlayer(player);
            staffPlayers.remove(staffPlayer);
        }
    }

    public StaffPlayer getStaffPlayerByPlayer(Player player) {
        return player == null ? null : staffPlayers.stream()
                .filter(staffPlayer -> staffPlayer.getName().equals(player.getName()))
                .findFirst()
                .orElse(null);
    }

    public Set<StaffPlayer> getWorkedPlayers() {
        return staffPlayers.stream()
                .filter(StaffPlayer::isWork)
                .collect(Collectors.toSet());
    }

    public void updatePlayer(StaffPlayer player) {
        if (player != null && isOnline(player)) {
            staffPlayers.remove(player);
            staffPlayers.add(player);
        }
    }

    public StaffPlayer findStaffPlayer(Player player) {
        try {
            User user = userDao.queryForIdAsync(player.getName().toLowerCase()).get();
            if (user == null || !player.isOnline())
                return null;

            return new StaffPlayer(player, user);
        } catch (ExecutionException | InterruptedException ignored) {
        }
        return null;
    }

    public boolean isOnline(Player player) {
        return staffPlayers.stream()
                .anyMatch(p -> player.equals(p.getPlayer()));
    }

    public boolean isWork(Player player) {
        StaffPlayer staffPlayer = getStaffPlayerByPlayer(player);
        return staffPlayer != null && staffPlayer.isWork();
    }

    public boolean isOnline(StaffPlayer player) {
        return isOnline(player.getPlayer());
    }

    public Set<StaffPlayer> getStaffPlayers() {
        return staffPlayers;
    }
}
