package com.beacmc.beacmcstaffwork.hook.litebans;


import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class LiteBansHandler {

    private final UserDao userDao;
    private final StaffWorkManager manager;

    public LiteBansHandler() {
        userDao = BeacmcStaffWork.getDatabase().getUserDao();
        manager = BeacmcStaffWork.getStaffWorkManager();
    }

    public void register() {
        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {
                final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();
                final Player player = Bukkit.getPlayer(entry.getExecutorName());

                if (player == null)
                    return;

                StaffPlayer staffPlayer = manager.getStaffPlayerByPlayer(player);
                if (staffPlayer == null)
                    return;

                User user = staffPlayer.getUser();

                if (user == null)
                    return;

                if (!staffPlayer.isOnline() || (!staffPlayer.isWork() && settings.getBoolean("required-work-on-add-statistic")))
                    return;

                switch (entry.getType()) {
                    case "mute": {
                        userDao.updateAsync(user.setMutes(user.getMutes() + 1));
                        manager.updatePlayer(staffPlayer);
                        break;
                    }
                    case "ban": {
                        userDao.updateAsync(user.setBans(user.getBans() + 1));
                        manager.updatePlayer(staffPlayer);
                        break;
                    }
                    case "kick": {
                        userDao.updateAsync(user.setKicks(user.getKicks() + 1));
                        manager.updatePlayer(staffPlayer);
                        break;
                    }
                }
            }

            @Override
            public void entryRemoved(Entry entry) {
                final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();
                final Player player = Bukkit.getPlayer(entry.getExecutorName());

                if (player == null)
                    return;

                StaffPlayer staffPlayer = manager.getStaffPlayerByPlayer(player);
                if (staffPlayer == null)
                    return;

                User user = staffPlayer.getUser();

                if (user == null)
                    return;

                if (staffPlayer.isOnline() || (!staffPlayer.isWork() && settings.getBoolean("required-work-on-add-statistic")))
                    return;

                switch (entry.getType()) {
                    case "mute": {
                        userDao.updateAsync(user.setUnmutes(user.getUnmutes() + 1));
                        manager.updatePlayer(staffPlayer);
                        break;
                    }
                    case "ban": {
                        userDao.updateAsync(user.setUnbans(user.getUnbans() + 1));
                        manager.updatePlayer(staffPlayer);
                        break;
                    }
                }
            }
        });
    }
}
