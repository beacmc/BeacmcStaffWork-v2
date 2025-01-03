package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.OfflinePlayer;
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
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player == null) return;

        if(params.isEmpty() || !player.isOnline())
            return;

        player.getPlayer().sendMessage(Message.of(params));
    }
}
