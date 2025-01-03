package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player == null) return;

        if(!player.isOnline() || params == null)
            return;

        params = PlaceholderAPI.setPlaceholders(player, params);

        for (Player execute : Bukkit.getOnlinePlayers()) {
            if (execute.hasPermission("beacmcstaffwork.view")) {
                execute.sendMessage(Message.of(params));
            }
        }
    }
}
