package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class StatsSubcommand implements Subcommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);

            if(player == null) {
                sender.sendMessage(Color.compile(Config.getString("settings.messages.player-offline")
                        .replace("{PREFIX}", Config.getString("settings.prefix"))
                ));
                return;
            }
            List<String> lines = Config.getStringList("settings.messages.stats").stream()
                    .map(x -> Color.compile(PlaceholderAPI.setPlaceholders(player, x.replace("{PREFIX}", Config.getString("settings.prefix")))))
                    .collect(Collectors.toList());

            lines.forEach(player::sendMessage);
            return;
        }
        final String message = Color.compile(Config.getString("settings.messages.swa-stats-error-use")
                .replace("{PREFIX}", Config.getString("settings.prefix")));
        sender.sendMessage(message);
    }
}

