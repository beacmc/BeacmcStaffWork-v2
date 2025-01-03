package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                PlaceholderAPI.setPlaceholders(player, params));
    }
}
