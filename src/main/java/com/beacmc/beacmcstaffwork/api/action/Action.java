package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.OfflinePlayer;

import java.util.Map;

public interface Action {

    String getName();

    String getDescription();

    void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs);

    default void execute(StaffPlayer player, String param, Pair<String, Object>... pairs) {
        execute(player.getPlayer(), param, pairs);
    }
}
