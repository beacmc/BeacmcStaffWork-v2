package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.OfflinePlayer;

public class ActionbarAction implements Action {


    @Override
    public String getName() {
        return "[actionbar]";
    }

    @Override
    public String getDescription() {
        return "send action bar message to player";
    }

    @Override
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player != null && player.isOnline())
            player.getPlayer().sendActionBar(Color.compile(params));
    }
}
