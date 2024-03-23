package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.manager.Actions;
import com.beacmc.beacmcstaffwork.manager.CommandManager;
import com.beacmc.beacmcstaffwork.manager.CooldownManager;
import com.beacmc.beacmcstaffwork.manager.StaffPlayer;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StaffCommand extends CommandManager {

    LuckPerms luckPerms;

    private static CooldownManager manager;




    public StaffCommand(LuckPerms luckPerms) {
        super("staffwork");
        this.luckPerms = luckPerms;

    }

    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        StaffPlayer moderator = new StaffPlayer((Player) sender);

        if(!moderator.hasPermission("beacmcstaffwork.use")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }

        manager = BeacmcStaffWork.cooldowns.get(moderator.getName());

        if(manager == null) {
            BeacmcStaffWork.cooldowns.put(moderator.getName(), new CooldownManager(sender));
            manager = new CooldownManager(sender);
        }
        if (manager.isCooldown()){
            moderator.sendMessage("settings.messages.cooldown");
            return;
        }

        manager.execute(10000);


        if (args.length != 0) {
            moderator.sendMessageList("settings.messages.help");
            return;
        }

        if(Config.getString("settings.actions." + moderator.getPrimaryGroup()) == null) {
            moderator.sendMessage("settings.messages.no-group");
            return;
        }

        if(moderator.isWork()) {
            moderator.sendTitle("settings.titles.on-disable-work.title", "settings.titles.on-disable-work.subtitle");
            moderator.stopWork();
            moderator.sendMessage("settings.messages.on-disable-work");
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(moderator.getPlayer()));
            Actions.start(Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".disable-work"), (Player) sender);
            return;
        }
        moderator.sendTitle("settings.titles.on-enable-work.title", "settings.titles.on-enable-work.subtitle");
        moderator.startWork();
        moderator.sendMessage("settings.messages.on-enable-work");

        Bukkit.getPluginManager().callEvent(new PlayerEnableWorkEvent(moderator.getPlayer()));

        Actions.start(Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".enable-work"), (Player) sender);
    }

}

