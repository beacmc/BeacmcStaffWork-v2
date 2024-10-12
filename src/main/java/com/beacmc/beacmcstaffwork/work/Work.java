package com.beacmc.beacmcstaffwork.work;

import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Work {

    public static void messageToModerator(Player player, String message){

        if(player == null || message == null) return;

        message = PlaceholderAPI.setPlaceholders(player, message);

        for (Player execute : Bukkit.getOnlinePlayers()) {
            if (execute.hasPermission("beacmcstaffwork.view")) {
                execute.sendMessage(Color.compile(message)
                                .replace("{PREFIX}", Config.getString("settings.prefix"))
                );
            }
        }
    }

    public static String getTimeFormat(User user) {
        long totalSeconds = user.getTime();

        long days = totalSeconds / (24 * 60 * 60);
        long hours = (totalSeconds % (24 * 60 * 60)) / (60 * 60);
        long minutes = (totalSeconds % (60 * 60)) / 60;
        long seconds = totalSeconds % 60;

        String path = Config.getString("settings.placeholderapi.placeholders.time-in-work");
        String replace = path
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds));
        return replace;
    }
}
