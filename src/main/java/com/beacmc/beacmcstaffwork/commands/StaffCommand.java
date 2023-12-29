package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.api.AddonAPI;
import com.beacmc.beacmcstaffwork.api.events.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.events.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.api.manager.CustomAddonHook;
import com.beacmc.beacmcstaffwork.manager.Actions;
import com.beacmc.beacmcstaffwork.manager.CommandManager;
import com.beacmc.beacmcstaffwork.manager.Config;
import com.beacmc.beacmcstaffwork.manager.User;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class StaffCommand extends CommandManager {

    LuckPerms luckPerms;




    public StaffCommand(LuckPerms luckPerms) {
        super("staffwork");
        this.luckPerms = luckPerms;
    }

    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        User moderator = new User((Player) sender);

        if(!moderator.hasPermission("beacmcstaffwork.use")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }

        if (args.length < 1 || args.length > 2) {
            moderator.sendMessageList("settings.messages.help");
            return;
        }

        for(CustomAddonHook hook : AddonAPI.getAddons().values()) {
            hook.onCommand(sender, command, label, args);
        }

        if(args[0].equalsIgnoreCase("on")) {
            if (moderator.isWork()) {
                moderator.sendMessage("settings.messages.already-worked");

            } else {
                if(Config.getString("settings.actions.groups-on-work." + moderator.getPrimaryGroup()) == null) {
                    moderator.sendMessage("settings.messages.no-group");

                }
                else {
                    moderator.startWork();
                    moderator.sendMessage("settings.messages.on-enable-work");

                     Bukkit.getPluginManager().callEvent(new PlayerEnableWorkEvent(moderator.getPlayer()));


                    Actions.start(Config.getStringList("settings.actions.groups-on-work." + moderator.getPrimaryGroup()), (Player) sender);
                }
            }
        }
        else if (args[0].equalsIgnoreCase("off")) {
            if (moderator.isWork()) {
                if(Config.getString("settings.actions.groups-off-work." + moderator.getPrimaryGroup()) == null) {
                    moderator.sendMessage("settings.messages.no-group");
                }
                else {
                    moderator.stopWork();
                    moderator.sendMessage("settings.messages.on-disable-work");


                    Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(moderator.getPlayer()));

                    Actions.start(Config.getStringList("settings.actions.groups-off-work." + moderator.getPrimaryGroup()), (Player) sender);

                }
            } else {
                moderator.sendMessage("settings.messages.not-worked");

            }
        }
    }
}

