package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class DeleteUserSubcommand implements Subcommand {

    private final Database database;
    private final UserDao userDao;

    public DeleteUserSubcommand() {
        database = BeacmcStaffWork.getDatabase();
        userDao = database.getUserDao();
    }

    @Override
    public String getName() {
        return "deleteuser";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            final String message = Color.compile(Config.getString("settings.messages.swa-delete-user-error-use")
                    .replace("{PREFIX}", Config.getString("settings.prefix")));
            sender.sendMessage(message);
            return;
        }

        try {
            User user = userDao.queryForId(args[1].toLowerCase());

            if (user == null) {
                final String message = Color.compile(Config.getString("settings.messages.user-not-found")
                        .replace("{PREFIX}", Config.getString("settings.prefix")));
                sender.sendMessage(message);
                return;
            }

            userDao.delete(user);
            final String message = Color.compile(Config.getString("settings.messages.user-deleted")
                    .replace("{PREFIX}", Config.getString("settings.prefix")));
            sender.sendMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
