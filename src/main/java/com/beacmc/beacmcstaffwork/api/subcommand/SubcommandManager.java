package com.beacmc.beacmcstaffwork.api.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.command.CommandSender;

import java.util.HashSet;

public class SubcommandManager {

    private final HashSet<Subcommand> registerSubcommands;
    private final BeacmcStaffWork plugin;

    public SubcommandManager() {
        plugin = BeacmcStaffWork.getInstance();
        registerSubcommands = new HashSet<>();
    }

    public void registerSubcommand(Subcommand subcommand) {
        if(isRegisterSubcommand(subcommand)) {
            plugin.getLogger().severe("subcommand " + subcommand.getName() + " already registered");
            return;
        }
        registerSubcommands.add(subcommand);
    }

    public void registerSubcommands(Subcommand... subcommands) {
        for(Subcommand subcommand : subcommands) {
            registerSubcommand(subcommand);
        }
    }

    public void unregisterSubcommand(Subcommand subcommand) {
        if(!isRegisterSubcommand(subcommand)) {
            plugin.getLogger().severe("subcommand " + subcommand.getName() + " not registered");
            return;
        }
        registerSubcommands.remove(subcommand);
    }

    public boolean isRegisterSubcommand(Subcommand subcommand) {
        String name = subcommand.getName();
        for (Subcommand execute : registerSubcommands) {
            if(name.equals(execute.getName()))
                return true;
        }
        return false;
    }

    public boolean executeSubcommands(CommandSender sender, String[] args) {
        for (String execute : args) {
            for (Subcommand sub : registerSubcommands) {
                String name = sub.getName();
                if (!execute.startsWith(name))
                    continue;

                sub.execute(sender, args);
                return true;
            }
        }
        return false;
    }

    public HashSet<Subcommand> getRegisterSubcommands() {
        return registerSubcommands;
    }
}
