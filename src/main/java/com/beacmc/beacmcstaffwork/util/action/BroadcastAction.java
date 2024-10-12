package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.Bukkit;

public class BroadcastAction implements Action {

    @Override
    public String getName() {
        return "[broadcast]";
    }

    @Override
    public String getDescription() {
        return "send message to all players";
    }

    @Override
    public void execute(StaffPlayer player, String params) {
        Bukkit.broadcastMessage(Color.compile(params));
    }
}
