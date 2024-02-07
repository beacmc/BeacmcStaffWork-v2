package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.CommandManager;
import com.beacmc.beacmcstaffwork.manager.CooldownManager;
import com.beacmc.beacmcstaffwork.manager.Data;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import org.bukkit.command.CommandSender;

public class StaffAdminCommand extends CommandManager {

    private static CooldownManager manager;

    public StaffAdminCommand() {
        super("staffworkadmin");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        manager = BeacmcStaffWork.cooldowns.get(sender.getName());

        if(manager == null) {
            BeacmcStaffWork.cooldowns.put(sender.getName(), new CooldownManager(sender));
            manager = new CooldownManager(sender);
        }
        if (manager.isCooldown()){
            sender.sendMessage(
                    Color.compile(Config.getString("settings.messages.cooldown")
                            .replace("{PREFIX}", Config.getString("settings.prefix")))
            );
            return;
        }

        manager.execute(10000);

        if(args.length == 1) {
            if(!args[0].equalsIgnoreCase("reload"))
                return;
            if (!sender.hasPermission("beacmcstaffwork.admin")){
                sendHelp(sender);
                return;
            }
            BeacmcStaffWork.getInstance().reloadConfig();
            sender.sendMessage(Color.compile(Config.getString("settings.messages.reload")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))
            ));
        } else if(args.length == 3) {
            if(!args[0].equalsIgnoreCase("reset"))
                return;

            if (!sender.hasPermission("beacmcstaffwork.admin")){
                sendHelp(sender);
                return;
            }
            switch (args[2].toLowerCase()) {
                case "time": {
                    Data.resetTime(args[1]);
                    break;
                }
                case "bans": {
                    Data.resetBans(args[1]);
                    break;
                }
                case "kicks": {
                    Data.resetKicks(args[1]);
                    break;
                }
                case "mutes": {
                    Data.resetMutes(args[1]);
                    break;
                }
                case "all": {
                    Data.resetMutes(args[1]);
                    Data.resetBans(args[1]);
                    Data.resetTime(args[1]);
                    Data.resetKicks(args[1]);
                    break;
                }
            }
            sender.sendMessage(
                    Color.compile(
                            Config.getString("settings.messages.statistic-reset")
                                    .replace("{PREFIX}", Config.getString("settings.prefix")))
            );
        } else {
            if (!sender.hasPermission("beacmcstaffwork.admin")){
                sendHelp(sender);
                return;
            }
            sender.sendMessage("неверное использование команды");
        }
    }

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage(Color.compile(Config.getString("settings.messages.no-permission")
                .replace("{PREFIX}", Config.getString("settings.prefix"))
        ));
    }
}
