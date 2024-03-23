package com.beacmc.beacmcstaffwork.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffAdminCompleter implements TabCompleter {



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 0 || !sender.hasPermission("beacmcstaffwork.admin"))
            return null;

        if(args.length == 2) {
            if(!args[0].equalsIgnoreCase("reset"))
                return null;

            ArrayList<String> list = new ArrayList<>();
            String input = args[1].toLowerCase();

            Bukkit.getOnlinePlayers().forEach(player -> {

                if(player.getName().startsWith(input))
                    list.add(player.getName());
            });

            return list;
        }

        if(args.length == 3) {
            if(!args[0].equalsIgnoreCase("reset"))
                return null;

            return args.length == 3 ? getCompletionsTwoArgs().stream().filter((category) -> {
                return category.toLowerCase().startsWith(args[2].toLowerCase());
            }).collect(Collectors.toList()) : null;
        }

        return args.length == 1 ? getCompletionsOneArgs().stream().filter((category) -> {
            return category.toLowerCase().startsWith(args[0].toLowerCase());
        }).collect(Collectors.toList()) : null;
    }


    private static List<String> getCompletionsOneArgs() {
        return List.of("reload", "reset");
    }

    private static List<String> getCompletionsTwoArgs() {
        return List.of("all", "bans", "time", "kicks", "mutes");
    }
}
