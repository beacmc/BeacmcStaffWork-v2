package com.beacmc.beacmcstaffwork.manager;


import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;

public class LiteBansHandler {

    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                User user = new User(Bukkit.getPlayer(entry.getExecutorName()));
                Data data = new Data(user);
                switch (entry.getType()) {
                    case "mute": {
                        if(user.isOnline() && user.isWork()) {
                            data.addMute();
                            System.out.println(user.getMutes());
                        }
                        break;
                    }
                    case "ban": {
                        if(user.isOnline() && user.isWork()) {
                            data.addBan();
                        }
                        break;
                    }
                    case "kick": {
                        if(user.isOnline() && user.isWork()) {
                            data.addKick();
                        }
                        break;
                    }
                }
            }
        });
    }
}
