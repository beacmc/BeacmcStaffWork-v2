package com.beacmc.beacmcstaffwork.util;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.data.Placeholder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.scheduler.BukkitRunnable;

public class Runner {

    public static void run() {
        new BukkitRunnable() {

            @Override
            public void run() {
                boolean bool = PlaceholderAPI.isRegistered("beacmcstaffwork");
                if(!bool) {
                    (new Placeholder()).load();
                }
            }
        }.runTaskTimer(BeacmcStaffWork.getInstance(), 100L, 100L);
    }
}
