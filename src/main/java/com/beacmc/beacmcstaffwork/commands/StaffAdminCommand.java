package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.CommandManager;
import com.beacmc.beacmcstaffwork.manager.Config;
import org.bukkit.command.CommandSender;

public class StaffAdminCommand extends CommandManager {
    public StaffAdminCommand() {
        super("staffworkadmin");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if(!sender.hasPermission("beacmcstaffwork.admin")) {
            sender.sendMessage(Color.compile(Config.getString("settings.messages.no-permission")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))
            ));
            return;
        }

        if (args.length < 1 || args.length > 1) {
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            BeacmcStaffWork.getInstance().reloadConfig();
            sender.sendMessage(Color.compile(Config.getString("settings.messages.reload")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))
            ));
        }

    }
}
