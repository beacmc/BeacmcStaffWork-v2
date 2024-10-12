package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.entity.Player;

public class MessageAction implements Action {

    @Override
    public String getName() {
        return "[message]";
    }

    @Override
    public String getDescription() {
        return "message to player";
    }

    @Override
    public void execute(StaffPlayer staffPlayer, String params) {
        if(params.isEmpty())
            return;

        Player player = staffPlayer.getPlayer();
        player.sendMessage(Color.compile(params
                .replace("{PREFIX}", Config.getString("settings.prefix"))
        ));
    }
}
