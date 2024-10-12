package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class ConsoleAction implements Action {

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
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                PlaceholderAPI.setPlaceholders(staffPlayer.getPlayer(), params));
    }
}
