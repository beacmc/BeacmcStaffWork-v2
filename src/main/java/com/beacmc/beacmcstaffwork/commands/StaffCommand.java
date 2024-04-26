package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.manager.StaffWorkManager;
import com.beacmc.beacmcstaffwork.manager.core.ActionExecute;
import com.beacmc.beacmcstaffwork.manager.command.CommandManager;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StaffCommand extends CommandManager {

    private static LuckPerms luckPerms;
    private static StaffWorkManager manager;

    public StaffCommand(LuckPerms luckPerms) {
        super("staffwork");
        this.luckPerms = luckPerms;
        manager = BeacmcStaffWork.getStaffWorkManager();
    }

    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if (!(sender instanceof Player)) return;

        StaffPlayer moderator = manager.getStaffPlayerByPlayer((Player) sender);

        if (!moderator.hasPermission("beacmcstaffwork.use")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }


        if (args.length == 0) {

            if (Config.getString("settings.actions." + moderator.getPrimaryGroup()) == null) {
                moderator.sendMessage("settings.messages.no-group");
                return;
            }
            if(moderator.isWork()) {
                moderator.sendTitle("settings.titles.on-disable-work.title", "settings.titles.on-disable-work.subtitle");
                moderator.stopWork();
                moderator.sendMessage("settings.messages.on-disable-work");
                Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(moderator.getPlayer()));
                ActionExecute.execute(moderator, Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".disable-work"));
                return;
            }
            moderator.sendTitle("settings.titles.on-enable-work.title", "settings.titles.on-enable-work.subtitle");
            moderator.startWork();
            moderator.sendMessage("settings.messages.on-enable-work");

            Bukkit.getPluginManager().callEvent(new PlayerEnableWorkEvent(moderator.getPlayer()));
            ActionExecute.execute(moderator, Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".enable-work"));
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("stats")) {
            moderator.sendMessageList("settings.messages.stats");
            return;
        }
        moderator.sendMessageList("settings.messages.help");
    }
}

