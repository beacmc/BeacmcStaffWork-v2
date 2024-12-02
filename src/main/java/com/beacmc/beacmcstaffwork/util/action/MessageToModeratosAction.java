package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageToModeratosAction implements Action {


    @Override
    public String getName() {
        return "[message_to_moderators]";
    }

    @Override
    public String getDescription() {
        return "message to moderators";
    }

    @Override
    public void execute(StaffPlayer player, String params) {
        if(player.getPlayer() == null || params == null) return;

        params = PlaceholderAPI.setPlaceholders(player.getPlayer(), params);

        for (Player execute : Bukkit.getOnlinePlayers()) {
            if (execute.hasPermission("beacmcstaffwork.view")) {
                execute.sendMessage(Color.compile(params)
                        .replace("{PREFIX}", Config.getString("settings.prefix"))
                );
            }
        }
    }
}
