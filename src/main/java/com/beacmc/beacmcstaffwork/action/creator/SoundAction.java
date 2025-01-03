package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundAction implements Action {


    @Override
    public String getName() {
        return "[sound]";
    }

    @Override
    public String getDescription() {
        return "play sound for player";
    }

    @Override
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player == null) return;

        if (!player.isOnline())
            return;

        Sound sound = Sound.valueOf(params);
        if(params == null)
            Bukkit.getLogger().info("sound is null");

        player.getPlayer().playSound(player.getPlayer().getLocation(), sound, 0.5f, 1.0f);
    }
}
