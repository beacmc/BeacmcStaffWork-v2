package com.beacmc.beacmcstaffwork.manager;


import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LiteBansHandler {

    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                Player player = Bukkit.getPlayer(entry.getExecutorName());
                if (player == null)
                    return;

                User user = new User(player);
                switch (entry.getType()) {
                    case "mute": {
                        if(user.isOnline() && user.isWork()) {
                            Data.addMute(user);
                        }
                        break;
                    }
                    case "ban": {
                        if(user.isOnline() && user.isWork()) {
                            Data.addBan(user);
                        }
                        break;
                    }
                    case "kick": {
                        if(user.isOnline() && user.isWork()) {
                            Data.addKick(user);
                        }
                        break;
                    }
                }
            }
        });
    }
}
