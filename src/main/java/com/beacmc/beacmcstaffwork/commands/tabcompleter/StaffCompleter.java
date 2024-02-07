package com.beacmc.beacmcstaffwork.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffCompleter implements TabCompleter {



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 0 || !sender.hasPermission("beacmcstaffwork.use"))
            return null;

        return args.length == 1 ? getCompletions().stream().filter((category) -> {
            return category.toLowerCase().startsWith(args[0].toLowerCase());
        }).collect(Collectors.toList()) : null;
    }


    private static List<String> getCompletions() {
        ArrayList<String> list = new ArrayList<>();

        list.add("on");
        list.add("off");
        return list;
    }
}
