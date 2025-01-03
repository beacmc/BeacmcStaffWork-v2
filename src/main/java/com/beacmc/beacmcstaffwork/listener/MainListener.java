package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.Set;

public class MainListener implements Listener {

    private final Set<StaffPlayer> users;
    private final StaffWorkManager manager;
    private final ActionManager action;

    public MainListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        users = manager.getStaffPlayers();
        action = BeacmcStaffWork.getActionManager();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        final ConfigurationSection workLimitsSettings = BeacmcStaffWork.getMainConfig().getWorkLimitsSettings();

        if (!workLimitsSettings.getBoolean("disable-entity-damage"))
            return;

        if(event.getDamager() instanceof Player damager) {
            if(workLimitsSettings.getBoolean("enable-bypass-permission") && damager.hasPermission("beacmcstaffwork.work-limits.bypass"))
                return;

            if(manager.isWork(damager)) {
                sendMessage(damager, "entity-damage-on-work");
                event.setCancelled(true);
            }
        }
        if(event.getEntity() instanceof Player damaged) {
            if(workLimitsSettings.getBoolean("enable-bypass-permission") && damaged.hasPermission("beacmcstaffwork.work-limits.bypass"))
                return;

            if(manager.isWork(damaged)) {
                if((event.getDamager() instanceof Player))
                    sendMessage((Player) event.getDamager(), "damager-damage-on-work");

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (manager.isWork(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemPickUp(PlayerPickupItemEvent event) {
        final ConfigurationSection workLimitsSettings = BeacmcStaffWork.getMainConfig().getWorkLimitsSettings();

        if(!workLimitsSettings.getBoolean("disable-pick-up-item"))
            return;

        Player player = event.getPlayer();

        if(workLimitsSettings.getBoolean("enable-bypass-permission") && player.hasPermission("beacmcstaffwork.work-limits.bypass"))
            return;

        if(manager.isWork(player)) {
            sendMessage(player, "pick-up-item-on-work");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final ConfigurationSection workLimitsSettings = BeacmcStaffWork.getMainConfig().getWorkLimitsSettings();

        if(!workLimitsSettings.getBoolean("disable-place-block"))
            return;

        Player player = event.getPlayer();

        if(workLimitsSettings.getBoolean("enable-bypass-permission") && player.hasPermission("beacmcstaffwork.work-limits.bypass"))
            return;

        if(manager.isWork(player)) {
            event.setCancelled(true);
            sendMessage(player, "block-place-on-work");
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final ConfigurationSection workLimitsSettings = BeacmcStaffWork.getMainConfig().getWorkLimitsSettings();

        if(!workLimitsSettings.getBoolean("disable-break-block"))
            return;

        Player player = event.getPlayer();

        if(workLimitsSettings.getBoolean("enable-bypass-permission") && player.hasPermission("beacmcstaffwork.work-limits.bypass"))
            return;

        if(manager.isWork(player)) {
            event.setCancelled(true);
            sendMessage(player, "block-break-on-work");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        manager.onLeave(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        manager.onLeave(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.onJoin(event.getPlayer());
    }

    private void sendMessage(Player player, String key) {
        final ConfigurationSection messages = BeacmcStaffWork.getMainConfig().getMessages();
        final String message = messages.getString(key);

        if(message != null && message.isEmpty())
            player.sendMessage(Message.of(message));
    }
}
