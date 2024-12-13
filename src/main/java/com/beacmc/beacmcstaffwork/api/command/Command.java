package com.beacmc.beacmcstaffwork.api.command;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Command implements CommandExecutor, TabCompleter {

    public Command(String command) {
        PluginCommand cmd = BeacmcStaffWork.getInstance().getCommand(command);
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }

    public abstract void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, command, label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
