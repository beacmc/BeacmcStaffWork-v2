package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        Bukkit.getOnlinePlayers().forEach(p ->
                p.sendMessage(Message.of(PlaceholderAPI.setPlaceholders(player, params))));
    }
}
