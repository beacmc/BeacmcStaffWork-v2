package com.beacmc.beacmcstaffwork.hook.advancedban;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class BanListener implements Listener {

    private final UserDao userDao;
    private final StaffWorkManager manager;

    public BanListener() {
        this.userDao = BeacmcStaffWork.getDatabase().getUserDao();
        this.manager = BeacmcStaffWork.getStaffWorkManager();
    }

    @EventHandler
    public void onPunish(PunishmentEvent event) {
        final AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();
        final Punishment punish = event.getPunishment();
        final StaffPlayer staffPlayer = manager.getStaffPlayerByPlayer(Bukkit.getPlayer(punish.getOperator()));

        if (staffPlayer == null) return;

        final User user = staffPlayer.getUser();

        if (user == null)
            return;

        if (!staffPlayer.isWork() && settings.getBoolean("required-work-on-add-statistic"))
            return;

        handler.start();

        switch (handler.getType()) {
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

    @EventHandler
    public void onRevoke(RevokePunishmentEvent event) {
        final AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();
        final Punishment punish = event.getPunishment();
        final StaffPlayer staffPlayer = manager.getStaffPlayerByPlayer(Bukkit.getPlayer(punish.getOperator()));

        if (staffPlayer == null) return;

        final User user = staffPlayer.getUser();
        final Database database = BeacmcStaffWork.getDatabase();
        final UserDao userDao = database.getUserDao();

        if (!staffPlayer.isWork() && settings.getBoolean("required-work-on-add-statistic")) {
            return;
        }

        handler.start();

        switch (handler.getType()) {
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
}