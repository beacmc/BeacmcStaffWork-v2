package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.manager.command.CommandManager;
import com.beacmc.beacmcstaffwork.util.CooldownManager;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class StaffAdminCommand extends CommandManager {

    private static CooldownManager manager;

    public StaffAdminCommand() {
        super("staffworkadmin");
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

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
            try {
                UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();
                User user = userDao.queryForId(args[1].toLowerCase());
                if(user == null)
                    return;

                switch (args[2].toLowerCase()) {
                    case "time": {
                        userDao.update(user.setTime(0));
                        break;
                    }
                    case "bans": {
                        userDao.update(user.setBans(0));
                        break;
                    }
                    case "kicks": {
                        userDao.update(user.setKicks(0));
                        break;
                    }
                    case "mutes": {
                        userDao.update(user.setMutes(0));
                        break;
                    }
                    case "all": {
                        userDao.update(user.setBans(0).setKicks(0).setTime(0).setMutes(0));
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
