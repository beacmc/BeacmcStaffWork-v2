package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.events.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.events.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.discord.Webhook;
import com.beacmc.beacmcstaffwork.manager.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class MainListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            User damager = new User((Player) event.getDamager());

            User entity = new User((Player) event.getEntity());

            if (Config.getBoolean("settings.work.disable-entity-damage")) {
                if (damager.isWork()) {
                    event.setCancelled(true);
                    damager.sendMessage("settings.messages.entity-damage-on-work");
                }
                else if(entity.isWork()) {
                    event.setCancelled(true);
                    entity.sendMessage("settings.messages.damager-damage-on-work");
                }
            }
        }
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User player = new User(event.getPlayer());
        if(Config.getBoolean("settings.update-check")) {
            if (player.hasPermission("beacmcstaffwork.update")) {
                String latestVersion = UpdateChecker.start();

                if(latestVersion != BeacmcStaffWork.getInstance().getDescription().getVersion()) {
                    List<String> list = Config.getStringList("settings.messages.update-check-player");
                    for (String execute : list) {
                        event.getPlayer().sendMessage(
                                Color.compile(execute
                                        .replace("{current_version}", BeacmcStaffWork.getInstance().getDescription().getVersion())
                                        .replace("{latest_version}", latestVersion)
                                ));
                    }
                }
            }
        }
    }




    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        User user = new User(event.getPlayer());

        Data data = new Data(user);
        if(Config.getBoolean("settings.work.disable-place-block")) {
            if(user.isWork()) {
                event.setCancelled(true);
                user.sendMessage("settings.messages.block-place-on-work");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        User user = new User(event.getPlayer());

        Data data = new Data(user);
        if(Config.getBoolean("settings.work.disable-break-block")) {
            if(user.isWork()) {
                event.setCancelled(true);
                user.sendMessage("settings.messages.block-break-on-work");
            }
        }
    }

    @EventHandler
    public void onEnableWork(PlayerEnableWorkEvent event) {
        if(Config.getBoolean("settings.discord.enable")) {
            Webhook.sendWebhook(
                    event.getPlayer(),
                    Config.getString("settings.discord.on-enable-work.title"),
                    Config.getString("settings.discord.on-enable-work.title-url"),
                    Config.getString("settings.discord.on-enable-work.description"),
                    Config.getString("settings.discord.on-enable-work.author-name"),
                    Config.getString("settings.discord.on-enable-work.author-icon-url"),
                    Config.getString("settings.discord.on-enable-work.image-url"),
                    Config.getInt("settings.discord.on-enable-work.color")
            );
        }
    }

    @EventHandler
    public void onEnableWork(PlayerDisableWorkEvent event) {
        if(Config.getBoolean("settings.discord.enable")) {
            Webhook.sendWebhook(
                    event.getPlayer(),
                    Config.getString("settings.discord.on-disable-work.title"),
                    Config.getString("settings.discord.on-disable-work.title-url"),
                    Config.getString("settings.discord.on-disable-work.description"),
                    Config.getString("settings.discord.on-disable-work.author-name"),
                    Config.getString("settings.discord.on-disable-work.author-icon-url"),
                    Config.getString("settings.discord.on-disable-work.image-url"),
                    Config.getInt("settings.discord.on-disable-work.color")
            );
        }
    }
}
