package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.manager.command.CommandManager;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StaffAdminCommand extends CommandManager {


    public StaffAdminCommand() {
        super("staffworkadmin");
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("beacmcstaffwork.admin")){
            sendHelp(sender);
            return;
        }

        if(args.length == 1) {
            if(!args[0].equalsIgnoreCase("reload"))
                return;

            BeacmcStaffWork.getInstance().reloadConfig();
            sender.sendMessage(Color.compile(Config.getString("settings.messages.reload")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))
            ));
        } else if (args.length == 2) {
            if(!args[0].equalsIgnoreCase("stats"))
                return;

            Player player = Bukkit.getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage(Color.compile(Config.getString("settings.messages.player-offline")
                        .replace("{PREFIX}", Config.getString("settings.prefix"))
                ));
                return;
            }
            List<String> lines = Config.getStringList("settings.messages.stats").stream()
                    .map(x -> Color.compile(x.replace("{PREFIX}", Config.getString("settings.prefix"))))
                    .collect(Collectors.toList());

            for (String line : lines) {
                player.sendMessage(PlaceholderAPI.setPlaceholders(player, line));
            }
        } else if(args.length == 3) {
            if(!args[0].equalsIgnoreCase("reset"))
                return;

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
