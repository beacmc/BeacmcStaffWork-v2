package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.StaffWorkManager;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.List;

public class CommandListener implements Listener {

    private HashSet<StaffPlayer> users;
    private StaffWorkManager manager;

    public CommandListener() {
        manager = BeacmcStaffWork.getStaffWorkManager();
        users = manager.getStaffPlayers();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(!Config.getBoolean("settings.work.commands.enable"))
            return;

        Player player = event.getPlayer();
        if(!manager.contains(player)) {
            List<String> disableCommands = Config.getStringList("settings.work.commands.disable-commands");
            String cmd = event.getMessage().split(" ")[0];
            if(disableCommands.contains(cmd.toLowerCase())) {
                player.sendMessage(Message.fromConfig("settings.messages.disable-command"));
                event.setCancelled(true);
            }
        }
    }
}
