package com.beacmc.beacmcstaffwork.work;

import com.beacmc.beacmcstaffwork.config.Config;

public class TimeUtil {

    public static String getFormattedTime(long totalSeconds) {

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
