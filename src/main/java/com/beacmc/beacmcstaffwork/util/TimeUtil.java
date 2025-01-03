package com.beacmc.beacmcstaffwork.util;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

    public static String getFormattedTime(long totalSeconds) {
        final ConfigurationSection placeholderAPISettings = BeacmcStaffWork.getMainConfig().getPlaceholderAPISettings();

        long days = totalSeconds / (24 * 60 * 60);
        long hours = (totalSeconds % (24 * 60 * 60)) / (60 * 60);
        long minutes = (totalSeconds % (60 * 60)) / 60;
        long seconds = totalSeconds % 60;

        String path = placeholderAPISettings.getString("placeholders.time-in-work");
        String replace = path
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds));
        return replace;
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.ofInstant(
                Instant.now(),
                ZoneId.of(BeacmcStaffWork.getMainConfig().getSettings().getString("time-zone"))
        );
    }
}
