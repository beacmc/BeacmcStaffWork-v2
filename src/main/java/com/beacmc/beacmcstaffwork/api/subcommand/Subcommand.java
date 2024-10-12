package com.beacmc.beacmcstaffwork.api.subcommand;

import org.bukkit.command.CommandSender;

public interface Subcommand {

    String getName();

    void execute(CommandSender sender, String[] args);
}
