package com.beacmc.beacmcstaffwork.manager;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Work {

    public static void messageToModerator(Player player, String message){

        if(player == null || message == null) return;

        message = PlaceholderAPI.setPlaceholders(player, message);

        for (Player execute : Bukkit.getOnlinePlayers()) {
            if (execute.hasPermission("beacmcstaffwork.view")) {
                execute.sendMessage(
                        Color.compile(message)
                );
            }
        }
    }

    public static int moderatorsInWork(User user) {
        String sql = "SELECT COUNT(*) FROM staff WHERE worked = true";
        try (PreparedStatement preparedStatement = SQLBuilder.getConnection().prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeFormat(User user) {
        long totalSeconds = user.getTime();

        long days = totalSeconds / (24 * 60 * 60);
        long hours = (totalSeconds % (24 * 60 * 60)) / (60 * 60);
        long minutes = (totalSeconds % (60 * 60)) / 60;
        long seconds = totalSeconds % 60;

        String path = BeacmcStaffWork.getInstance().getConfig().getString("settings.placeholderapi.placeholders.time-in-work");
        String replace = path
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds));
        return replace;
    }
}
