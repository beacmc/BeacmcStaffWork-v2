package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.util.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class StatsSubcommand implements Subcommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final ConfigurationSection messages = BeacmcStaffWork.getMainConfig().getMessages();

        if (args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);

            if(player == null) {
                sender.sendMessage(Message.getMessageFromConfig("player-offline"));
                return;
            }
            List<String> lines = messages.getStringList("stats").stream()
                    .map(line -> Message.of(PlaceholderAPI.setPlaceholders(player, line)))
                    .toList();
            lines.forEach(player::sendMessage);
            return;
        }
        sender.sendMessage(Message.getMessageFromConfig("swa-stats-error-use"));
    }
}

