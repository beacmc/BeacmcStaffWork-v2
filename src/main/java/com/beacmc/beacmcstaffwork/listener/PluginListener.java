package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class PluginListener implements Listener {

    private final StaffWorkManager manager;
    private final Set<StaffPlayer> staffPlayers;
    private final ActionManager action;

    public PluginListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        staffPlayers = manager.getStaffPlayers();
        action = BeacmcStaffWork.getActionManager();
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        final Plugin plugin = event.getPlugin();
        final ConfigurationSection actions = BeacmcStaffWork.getMainConfig().getActions();

        if(!plugin.getName().equalsIgnoreCase("beacmcstaffwork"))
            return;

        for (StaffPlayer player : staffPlayers) {
            if (player.isOnline() || !player.isWork())
                return;

            PlayerDisableWorkEvent playerDisableWorkEvent = new PlayerDisableWorkEvent(player);
            Bukkit.getPluginManager().callEvent(playerDisableWorkEvent);
            if (!playerDisableWorkEvent.isCancelled()) {
                action.execute(player.getPlayer(), actions.getStringList(player.getPrimaryGroup() + ".disable-work"));
                player.stopWork();
            }
        }
    }
}
