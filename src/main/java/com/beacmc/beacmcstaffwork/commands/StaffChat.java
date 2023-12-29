package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.CommandManager;
import com.beacmc.beacmcstaffwork.manager.Config;
import com.beacmc.beacmcstaffwork.manager.User;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChat extends CommandManager {

    public StaffChat() {
        super("staffchat");
    }
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        User moderator = new User((Player) sender);

        System.out.println(String.join(" ", Arrays.asList(args)));

        if (!moderator.hasPermission("beacmcstaffwork.chat")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }

        String format = PlaceholderAPI.setPlaceholders(moderator.getPlayer(), Config.getString("settings.chat-format"));
        format = Color.compile(format)
                .replace("{MESSAGE}", String.join(" ", Arrays.asList(args)));

        String finalFormat = format;
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.hasPermission("beacmcstaffwork.chat")) {
                player.sendMessage(finalFormat);
            }
        });
    }
}
