package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class ResetSubcommand implements Subcommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 3) {
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
                    case "unmutes": {
                        userDao.update(user.setUnmutes(0));
                        break;
                    }
                    case "unbans": {
                        userDao.update(user.setUnbans(0));
                        break;
                    }
                    case "all": {
                        userDao.update(user.setBans(0).setKicks(0).setTime(0).setMutes(0).setUnbans(0).setUnmutes(0));
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            final String message = Color.compile(Config.getString("settings.messages.statistic-reset")
                            .replace("{PREFIX}", Config.getString("settings.prefix")));
            sender.sendMessage(message);
            return;
        }
        final String message = Color.compile(Config.getString("settings.messages.swa-reset-error-use")
                .replace("{PREFIX}", Config.getString("settings.prefix")));
        sender.sendMessage(message);
    }
}
