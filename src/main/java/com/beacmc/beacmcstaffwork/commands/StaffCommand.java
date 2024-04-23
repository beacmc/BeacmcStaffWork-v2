package com.beacmc.beacmcstaffwork.commands;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.manager.core.ActionExecute;
import com.beacmc.beacmcstaffwork.manager.command.CommandManager;
import com.beacmc.beacmcstaffwork.util.CooldownManager;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;


public class StaffCommand extends CommandManager {

    private static LuckPerms luckPerms;
    private static CooldownManager manager;

    public StaffCommand(LuckPerms luckPerms) {
        super("staffwork");
        this.luckPerms = luckPerms;

    }

    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        StaffPlayer moderator = new StaffPlayer((Player) sender);

        if(!moderator.hasPermission("beacmcstaffwork.use")) {
            moderator.sendMessage("settings.messages.no-permission");
            return;
        }
        if (!(sender instanceof Player)) return;
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

        Set<String> groups = moderator.getUserGroups();
        boolean hasConfiguredGroup = groups.stream()
                .anyMatch(group -> Config.contains("settings.actions." + group));

        if (!hasConfiguredGroup) {
            moderator.sendMessage("settings.messages.no-group");
            return;
        }
        if(moderator.isWork()) {
            moderator.sendTitle("settings.titles.on-disable-work.title", "settings.titles.on-disable-work.subtitle");
            moderator.stopWork();
            moderator.sendMessage("settings.messages.on-disable-work");
            Bukkit.getPluginManager().callEvent(new PlayerDisableWorkEvent(moderator.getPlayer()));
            //Actions.start(Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".disable-work"), (Player) sender);
            ActionExecute.execute(moderator, Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".disable-work"));
            return;
        }
        moderator.sendTitle("settings.titles.on-enable-work.title", "settings.titles.on-enable-work.subtitle");
        moderator.startWork();
        moderator.sendMessage("settings.messages.on-enable-work");

        Bukkit.getPluginManager().callEvent(new PlayerEnableWorkEvent(moderator.getPlayer()));
        ActionExecute.execute(moderator, Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".enable-work"));

        //Actions.start(Config.getStringList("settings.actions." + moderator.getPrimaryGroup() + ".enable-work"), (Player) sender);
    }

}

