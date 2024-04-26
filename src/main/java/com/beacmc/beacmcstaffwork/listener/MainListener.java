package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.discord.Embed;
import com.beacmc.beacmcstaffwork.manager.StaffWorkManager;
import com.beacmc.beacmcstaffwork.manager.core.ActionExecute;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.beacmc.beacmcstaffwork.util.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public class MainListener implements Listener {

    private HashSet<StaffPlayer> users;
    private StaffWorkManager manager;

    public MainListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        users = manager.getStaffPlayers();
    }



    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!Config.getBoolean("settings.work.disable-entity-damage"))
            return;

        if(event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            if(manager.contains(damager)) {
                event.getDamager().sendMessage(Message.fromConfig("settings.messages.entity-damage-on-work"));
                event.setCancelled(true);
            }
        }
        if(event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();

            if(manager.contains(damaged)) {
                if((event.getDamager() instanceof Player))
                    event.getDamager().sendMessage(Message.fromConfig("settings.messages.damager-damage-on-work"));

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemPickUp(PlayerPickupItemEvent event) {
        if(!Config.getBoolean("settings.work.disable-pick-up-item"))
            return;

        Player player = event.getPlayer();

        if(manager.contains(player)) {
            player.sendMessage(Message.fromConfig("settings.messages.pick-up-item-on-work"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!Config.getBoolean("settings.work.disable-place-block"))
            return;
        Player player = event.getPlayer();

        if(manager.contains(player)) {
            event.setCancelled(true);
            player.sendMessage(Message.fromConfig("settings.messages.block-place-on-work"));
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!Config.getBoolean("settings.work.disable-break-block"))
            return;

        Player player = event.getPlayer();

        if(manager.contains(player)) {
            event.setCancelled(true);
            player.sendMessage(Message.fromConfig("settings.messages.block-break-on-work"));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(!Config.getBoolean("settings.work.disable-work-on-quit"))
            return;

        Player player = event.getPlayer();
        if (manager.contains(player)) {
            StaffPlayer staffPlayer = new StaffPlayer(player);
            ActionExecute.execute(staffPlayer, Config.getStringList("settings.actions." + staffPlayer.getPrimaryGroup() + ".disable-work"));
            staffPlayer.stopWork();
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(staffPlayer.getPlayer()));
            users.remove(staffPlayer);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        StaffPlayer staffPlayer = new StaffPlayer(event.getPlayer());
        if(staffPlayer.isWork())
            users.add(staffPlayer);
    }

    @EventHandler
    public void onEnableWork(PlayerEnableWorkEvent event) {
        if (Config.getBoolean("settings.discord.enable")) {
            Player player = event.getPlayer();
            TextChannel channel = BeacmcStaffWork.getDiscordBot().getJDA().getGuildById(Long.valueOf(Config.getString("settings.discord.guild-id"))).getTextChannelById(Long.valueOf(Config.getString("settings.discord.on-enable-work.channel-id")));
            if (channel == null) {
                Bukkit.getLogger().info("Channel is null");
                return;
            }

            String title = Config.getString("settings.discord.on-enable-work.title");
            Bukkit.getLogger().info("Title: " + title);
            if (title != null) {
                title = PlaceholderAPI.setPlaceholders(player, title);
            }
            String titleUrl = Config.getString("settings.discord.on-enable-work.title-url");
            Bukkit.getLogger().info("Title URL: " + titleUrl);
            String author = Config.getString("settings.discord.on-enable-work.author-name");
            Bukkit.getLogger().info("Author: " + author);
            if (author != null) {
                author = PlaceholderAPI.setPlaceholders(player, PlaceholderAPI.setPlaceholders(player, author));
            }
            String authorIcon = Config.getString("settings.discord.on-enable-work.author-icon-url");
            Bukkit.getLogger().info("Author Icon URL: " + authorIcon);
            String image = Config.getString("settings.discord.on-enable-work.image-url");
            Bukkit.getLogger().info("Image URL: " + image);
            String description = Config.getString("settings.discord.on-enable-work.description");
            Bukkit.getLogger().info("Description: " + description);
            if (description != null) {
                description = PlaceholderAPI.setPlaceholders(player, description);
            }
            String color = Config.getString("settings.discord.on-enable-work.color");
            Bukkit.getLogger().info("Color: " + color);

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
