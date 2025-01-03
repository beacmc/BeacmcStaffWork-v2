package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Message;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

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
            sender.sendMessage(Message.getMessageFromConfig("swa-delete-user-error-use"));
            return;
        }

        CompletableFuture<User> future = userDao.queryForIdAsync(args[1].toLowerCase());
        future.thenAccept(user -> {
            if (user == null) {
                sender.sendMessage(Message.getMessageFromConfig("user-not-found"));
                return;
            }

            userDao.deleteAsync(user);
            sender.sendMessage(Message.getMessageFromConfig("user-deleted"));
        });
    }
}
