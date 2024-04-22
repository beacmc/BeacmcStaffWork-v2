package com.beacmc.beacmcstaffwork.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.Bukkit;

public class BroadcastAction extends Action {

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
