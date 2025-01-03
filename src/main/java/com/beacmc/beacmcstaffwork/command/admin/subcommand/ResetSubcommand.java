package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Message;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class ResetSubcommand implements Subcommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 3) {
            UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();
            CompletableFuture<User> future = userDao.queryForIdAsync(args[1].toLowerCase());
            future.thenAccept(user -> {
                if (user == null)
                    return;

                switch (args[2].toLowerCase()) {
                    case "time": {
                        userDao.updateAsync(user.setTime(0));
                        break;
                    }
                    case "bans": {
                        userDao.updateAsync(user.setBans(0));
                        break;
                    }
                    case "kicks": {
                        userDao.updateAsync(user.setKicks(0));
                        break;
                    }
                    case "mutes": {
                        userDao.updateAsync(user.setMutes(0));
                        break;
                    }
                    case "unmutes": {
                        userDao.updateAsync(user.setUnmutes(0));
                        break;
                    }
                    case "unbans": {
                        userDao.updateAsync(user.setUnbans(0));
                        break;
                    }
                    case "all": {
                        userDao.updateAsync(user.setBans(0).setKicks(0).setTime(0).setMutes(0).setUnbans(0).setUnmutes(0));
                        break;
                    }
                }
                sender.sendMessage(Message.getMessageFromConfig("statistic-reset"));
            });
            return;
        }
        sender.sendMessage(Message.getMessageFromConfig("swa-reset-error-use"));
    }
}
