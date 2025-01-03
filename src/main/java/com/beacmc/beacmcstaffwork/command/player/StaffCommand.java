package com.beacmc.beacmcstaffwork.command.player;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.api.command.Command;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;


public class StaffCommand extends Command {

    private final LuckPerms luckPerms;
    private final StaffWorkManager manager;
    private final ActionManager action;

    public StaffCommand() {
        super("staffwork");
        luckPerms = BeacmcStaffWork.getLuckPerms();
        manager = BeacmcStaffWork.getStaffWorkManager();
        action = BeacmcStaffWork.getActionManager();
    }

    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return;

        final StaffPlayer moderator = manager.getStaffPlayerByPlayer((Player) sender);
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection actions = config.getActions();
        final ConfigurationSection messages = config.getMessages();

        if (moderator == null) {
            player.sendMessage(Message.getMessageFromConfig("staff-player-not-found"));
            return;
        }

        if (!moderator.hasPermission("beacmcstaffwork.use")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }

        if (args.length == 0) {

            if (actions.getString(moderator.getPrimaryGroup()) == null) {
                moderator.sendMessage(Message.getMessageFromConfig("no-group"));
                return;
            }
            if(moderator.isWork()) {
                PlayerDisableWorkEvent event = new PlayerDisableWorkEvent(moderator);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    moderator.sendTitle(Message.fromConfig("settings.titles.on-disable-work.title"), Message.fromConfig("settings.titles.on-disable-work.subtitle"));
                    moderator.stopWork();
                    moderator.sendMessage(Message.getMessageFromConfig("on-disable-work"));
                    action.execute(moderator.getPlayer(), actions.getStringList(moderator.getPrimaryGroup() + ".disable-work"));
                }
                return;
            }

            PlayerEnableWorkEvent event = new PlayerEnableWorkEvent(moderator);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                moderator.sendTitle(Message.fromConfig("settings.titles.on-enable-work.title"), Message.fromConfig("settings.titles.on-enable-work.subtitle"));
                moderator.startWork();
                moderator.sendMessage(Message.getMessageFromConfig("on-enable-work"));
                action.execute(moderator.getPlayer(), actions.getStringList(moderator.getPrimaryGroup() + ".enable-work"));
            }
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("stats")) {
            List<String> lines = messages.getStringList("stats").stream()
                    .map(line -> Message.of(PlaceholderAPI.setPlaceholders(moderator.getPlayer(), line)))
                    .toList();
            moderator.sendMessage(lines);
            return;
        }
        List<String> lines = messages.getStringList("stats").stream()
                .map(Message::of)
                .toList();
        moderator.sendMessage(lines);
    }
}

