package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private final StaffWorkManager manager;

    public CommandListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection workLimitsSettings = config.getWorkLimitsSettings();

        if(!workLimitsSettings.getBoolean("commands.enable"))
            return;

        final Player player = event.getPlayer();

        if(workLimitsSettings.getBoolean("enable-bypass-permission") && player.hasPermission("beacmcstaffwork.work-limits.bypass"))
            return;

        if(!manager.isWork(player)) {
            List<String> disableCommands = workLimitsSettings.getStringList("commands.disable-commands");
            String command = event.getMessage();
            boolean cancel = disableCommands.stream().anyMatch(command::startsWith);
            if (cancel) {
                event.setCancelled(true);
                player.sendMessage(Message.getMessageFromConfig("disable-command"));
            }
        }
    }
}
