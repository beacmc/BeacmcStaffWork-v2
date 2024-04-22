package com.beacmc.beacmcstaffwork.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class MessageAction extends Action {

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
