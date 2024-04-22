package com.beacmc.beacmcstaffwork.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class ConsoleAction extends Action {

    @Override
    public String getName() {
        return "[console]";
    }

    @Override
    public String getDescription() {
        return "dispatch console command";
    }

    @Override
    public void execute(StaffPlayer staffPlayer, String params) {
        System.out.println(params);
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                PlaceholderAPI.setPlaceholders(staffPlayer.getPlayer(), params));
    }
}
