package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.Bukkit;
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
    public void execute(StaffPlayer staffPlayer, String params) {
        Player player = staffPlayer.getPlayer();
        Sound sound = Sound.valueOf(params);
        if(params == null)
            Bukkit.getLogger().info("sound is null");

        player.playSound(player.getLocation(), sound, 0.5f, 1.0f);
    }
}
