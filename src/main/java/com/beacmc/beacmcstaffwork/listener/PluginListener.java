package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class PluginListener implements Listener {

    private final StaffWorkManager manager;
    private final HashSet<StaffPlayer> staffPlayers;
    private final ActionManager action;

    public PluginListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        staffPlayers = manager.getStaffPlayers();
        action = BeacmcStaffWork.getActionManager();
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        if(!plugin.getName().equalsIgnoreCase("beacmcstaffwork"))
            return;

        for (StaffPlayer player : staffPlayers) {
            if (!player.isOnline())
                return;

            action.execute(player, Config.getStringList("settings.actions." + player.getPrimaryGroup() + ".disable-work"));
            player.stopWork();
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(player.getPlayer()));
        }
    }
}
