package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.manager.handler.AdvancedBanHandler;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class ABListener implements Listener {

    @EventHandler
    public void onPunish(PunishmentEvent event) {
        AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        StaffPlayer staffPlayer = new StaffPlayer(Bukkit.getPlayer(handler.getExecutor()));
        User user = staffPlayer.getUser();
        Database database = BeacmcStaffWork.getDatabase();
        UserDao userDao = database.getUserDao();

        if(!staffPlayer.isWork())
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
}