package com.beacmc.beacmcstaffwork.work;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class StaffWorkManager {

    private HashSet<StaffPlayer> staffPlayers;

    public StaffWorkManager() {
        staffPlayers = BeacmcStaffWork.getUsers();
    }

    public @NotNull StaffPlayer getStaffPlayerByPlayer(Player player) {
        if(player == null)
            return null;

        for (StaffPlayer staffPlayer : BeacmcStaffWork.getUsers()) {
            if(staffPlayer.getName() == player.getName())
                return staffPlayer;
        }
        return new StaffPlayer(player);
    }

    public boolean contains(Player player) {
        for (StaffPlayer staffPlayer : staffPlayers) {
            if(staffPlayer.getPlayer() == player)
                return true;
        }
        return false;
    }

    public HashSet<StaffPlayer> getStaffPlayers() {
        return staffPlayers;
    }
}
