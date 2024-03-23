package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.discord.Embed;
import com.beacmc.beacmcstaffwork.manager.Actions;
import com.beacmc.beacmcstaffwork.manager.StaffPlayer;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    @EventHandler
    public void onDasmage(EntityDamageByEntityEvent event) {
        if (!Config.getBoolean("settings.work.disable-entity-damage"))
            return;

        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            StaffPlayer damager = new StaffPlayer((Player) event.getDamager());

            StaffPlayer entity = new StaffPlayer((Player) event.getEntity());
            if(!BeacmcStaffWork.getUsers().contains(damager) || !BeacmcStaffWork.getUsers().contains(entity))
                return;


            if (damager.isWork()) {
                event.setCancelled(true);
                damager.sendMessage("settings.messages.entity-damage-on-work");
            }
            else if(entity.isWork()) {
                event.setCancelled(true);
                entity.sendMessage("settings.messages.damager-damage-on-work");
            }
        }
        else if(event.getEntity() instanceof Player) {
            StaffPlayer user = new StaffPlayer((Player) event.getEntity());

            if(user.isWork())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemPickUp(PlayerPickupItemEvent event) {
        if(!BeacmcStaffWork.getUsers().contains(event.getPlayer()))
            return;

        if(!Config.getBoolean("settings.work.disable-pick-up-item"))
            return;

        StaffPlayer user = new StaffPlayer(event.getPlayer());

        if(user.isWork()) {
            user.sendMessage("settings.messages.pick-up-item-on-work");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!BeacmcStaffWork.getUsers().contains(event.getPlayer()))
            return;
        StaffPlayer user = new StaffPlayer(event.getPlayer());

        if(Config.getBoolean("settings.work.disable-place-block")) {
            if(user.isWork()) {
                event.setCancelled(true);
                user.sendMessage("settings.messages.block-place-on-work");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!BeacmcStaffWork.getUsers().contains(event.getPlayer()))
                return;
        StaffPlayer user = new StaffPlayer(event.getPlayer());

        if(Config.getBoolean("settings.work.disable-break-block")) {
            if(user.isWork()) {
                event.setCancelled(true);
                user.sendMessage("settings.messages.block-break-on-work");
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(!BeacmcStaffWork.getUsers().contains(event.getPlayer()))
            return;
        if(!Config.getBoolean("settings.work.disable-work-on-quit"))
            return;

        StaffPlayer staffPlayer = new StaffPlayer(event.getPlayer());
        User user = staffPlayer.getUser();
        if (user != null && user.isWork()) {
            Actions.start(Config.getStringList("settings.actions." + staffPlayer.getPrimaryGroup() + ".disable-work"), staffPlayer.getPlayer());
            staffPlayer.stopWork();
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(staffPlayer.getPlayer()));
        }
    }

    @EventHandler
    public void onEnableWork(PlayerEnableWorkEvent event) {
        if(Config.getBoolean("settings.discord.enable")) {
            Player player = event.getPlayer();
            TextChannel channel = BeacmcStaffWork.getDiscordBot().getJDA().getGuildById(Long.valueOf(Config.getString("settings.discord.guild-id"))).getTextChannelById(Long.valueOf(Config.getString("settings.discord.on-enable-work.channel-id")));
            if(channel == null)
                return;
            String title = PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-enable-work.title"));
            String titleUrl = Config.getString("settings.discord.on-enable-work.title-url");
            String author = PlaceholderAPI.setPlaceholders(player, PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-enable-work.author-name")));
            String authorIcon = Config.getString("settings.discord.on-enable-work.author-icon-url");
            String image = Config.getString("settings.discord.on-enable-work.image-url");
            String description = PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-enable-work.description"));
            String color = Config.getString("settings.discord.on-enable-work.color");
            channel.sendMessageEmbeds(Embed.of(title, titleUrl, author, authorIcon, image, description, color).build()).queue();
        }
    }

    @EventHandler
    public void onDisableWork(PlayerDisableWorkEvent event) {
        if(Config.getBoolean("settings.discord.enable")) {
            Player player = event.getPlayer();
            TextChannel channel = BeacmcStaffWork.getDiscordBot().getJDA().getGuildById(Long.valueOf(Config.getString("settings.discord.guild-id"))).getTextChannelById(Long.valueOf(Config.getString("settings.discord.on-disable-work.channel-id")));
            if(channel == null)
                return;
            String title = PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-disable-work.title"));
            String titleUrl = Config.getString("settings.discord.on-disable-work.title-url");
            String author = PlaceholderAPI.setPlaceholders(player, PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-disable-work.author-name")));
            String authorIcon = Config.getString("settings.discord.on-disable-work.author-icon-url");
            String image = Config.getString("settings.discord.on-disable-work.image-url");
            String description = PlaceholderAPI.setPlaceholders(player, Config.getString("settings.discord.on-disable-work.description"));
            String color = Config.getString("settings.discord.on-disable-work.color");
            channel.sendMessageEmbeds(Embed.of(title, titleUrl, author, authorIcon, image, description, color).build()).queue();
        }
    }
}
