package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.manager.AdvancedBanHandler;
import com.beacmc.beacmcstaffwork.manager.Data;
import com.beacmc.beacmcstaffwork.manager.User;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ABListener implements Listener {

    @EventHandler
    public void onPunish(PunishmentEvent event) {
        AdvancedBanHandler handler = new AdvancedBanHandler(event.getPunishment());
        User user = new User(Bukkit.getPlayer(handler.getExecutor()));

        handler.start();

        switch (handler.getType()) {
            case "ban": {
                Data.addBan(user);
                break;
            }
            case "kick": {
                Data.addKick(user);
                break;
            }
            case "mute": {
                Data.addMute(user);
                break;
            }
        }
    }
}
