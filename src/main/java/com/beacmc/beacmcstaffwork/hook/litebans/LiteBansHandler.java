package com.beacmc.beacmcstaffwork.hook.litebans;


import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class LiteBansHandler {

    public void register() {
        Events.get().register(new Events.Listener() {
            Database database = BeacmcStaffWork.getDatabase();
            UserDao userDao = database.getUserDao();
            @Override
            public void entryAdded(Entry entry) {
                try {
                    Player player = Bukkit.getPlayer(entry.getExecutorName());

                    if (player == null)
                        return;

                    StaffPlayer staffPlayer = new StaffPlayer(player);
                    User user = staffPlayer.getUser();

                    if (staffPlayer.isOnline() || !staffPlayer.isWork())
                        return;

                    switch (entry.getType()) {
                        case "mute": {
                            userDao.update(user.setMutes(user.getMutes() + 1));
                            break;
                        }
                        case "ban": {
                            userDao.update(user.setBans(user.getBans() + 1));
                            break;
                        }
                        case "kick": {
                            userDao.update(user.setKicks(user.getKicks() + 1));
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void entryRemoved(Entry entry) {
                try {
                    Player player = Bukkit.getPlayer(entry.getExecutorName());

                    if (player == null)
                        return;

                    StaffPlayer staffPlayer = new StaffPlayer(player);
                    User user = staffPlayer.getUser();

                    if (staffPlayer.isOnline() || (!staffPlayer.isWork() && Config.getBoolean("settings.required-work-on-add-statistic")))
                        return;

                    switch (entry.getType()) {
                        case "mute": {
                            userDao.update(user.setMutes(user.getMutes() - 1).setUnmutes(user.getUnmutes() + 1));
                            break;
                        }
                        case "ban": {
                            userDao.update(user.setBans(user.getBans() - 1).setUnbans(user.getUnbans() + 1));
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
