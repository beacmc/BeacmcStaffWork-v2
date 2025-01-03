package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player != null && player.isOnline())
            Bukkit.dispatchCommand(player.getPlayer(), PlaceholderAPI.setPlaceholders(player, params));
    }
}
