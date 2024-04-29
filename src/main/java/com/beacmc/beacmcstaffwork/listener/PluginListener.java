package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.manager.StaffWorkManager;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.beacmc.beacmcstaffwork.manager.core.ActionExecute;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class PluginListener implements Listener {

    private StaffWorkManager manager;
    private HashSet<StaffPlayer> staffPlayers;

    public PluginListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        staffPlayers = manager.getStaffPlayers();
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        if(!plugin.getName().equalsIgnoreCase("beacmcstaffwork"))
            return;

        for (StaffPlayer player : staffPlayers) {
            if(!player.isOnline())
                continue;

            ActionExecute.execute(player, Config.getStringList("settings.actions." + player.getPrimaryGroup() + ".disable-work"));
            player.stopWork();
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(player.getPlayer()));
        }
    }
}
