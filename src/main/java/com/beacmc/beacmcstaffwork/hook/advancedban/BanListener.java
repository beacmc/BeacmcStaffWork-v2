package com.beacmc.beacmcstaffwork.hook.advancedban;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class BanListener implements Listener {

    private final UserDao userDao;

    public BanListener() {
        this.userDao = BeacmcStaffWork.getDatabase().getUserDao();
    }

    @EventHandler
    public void onPunish(PunishmentEvent event) {
        final AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        final Punishment punish = event.getPunishment();
        final StaffPlayer staffPlayer = new StaffPlayer(Bukkit.getPlayer(punish.getOperator()));
        final User user = staffPlayer.getUser();

        if (user == null)
            return;

        if(!staffPlayer.isWork() && Config.getBoolean("settings.required-work-on-add-statistic"))
            return;

        try {
            handler.start();

            switch (handler.getType()) {
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

    @EventHandler
    public void onRevoke(RevokePunishmentEvent event) {
        final AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        final Punishment punish = event.getPunishment();
        final StaffPlayer staffPlayer = new StaffPlayer(Bukkit.getPlayer(punish.getOperator()));
        final User user = staffPlayer.getUser();
        final Database database = BeacmcStaffWork.getDatabase();
        final UserDao userDao = database.getUserDao();

        try {
            handler.start();

            switch (handler.getType()) {
                case "mute": {
                    userDao.update(user.setUnmutes(user.getUnmutes() + 1));
                    break;
                }
                case "ban": {
                    userDao.update(user.setUnbans(user.getUnbans() + 1));
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}