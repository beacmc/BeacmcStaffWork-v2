package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class PlayerAction implements Action {

    @Override
    public String getName() {
        return "[player]";
    }

    @Override
    public String getDescription() {
        return "dispatch player command";
    }

    @Override
    public void execute(StaffPlayer player, String params) {
        Bukkit.dispatchCommand(player.getPlayer(), PlaceholderAPI.setPlaceholders(player.getPlayer(), params));
    }
}
