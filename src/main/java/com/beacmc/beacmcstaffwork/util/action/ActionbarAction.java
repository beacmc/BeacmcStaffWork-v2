package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.entity.Player;

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
    public void execute(StaffPlayer staffPlayer, String params) {
        Player player = staffPlayer.getPlayer();
        player.sendActionBar(Color.compile(params));
    }
}
