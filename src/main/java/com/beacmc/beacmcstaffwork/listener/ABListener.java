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

        Data data = new Data(new User(Bukkit.getPlayer(handler.getExecutor())));
        handler.start();

        switch (handler.getType()) {
            case "ban": {
                data.addBan();
                break;
            }
            case "kick": {
                data.addKick();
                break;
            }
            case "mute": {
                data.addMute();
                break;
            }
        }
    }
}
