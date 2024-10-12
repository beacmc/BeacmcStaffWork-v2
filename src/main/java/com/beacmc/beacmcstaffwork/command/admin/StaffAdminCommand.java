package com.beacmc.beacmcstaffwork.command.admin;

import com.beacmc.beacmcstaffwork.api.subcommand.SubcommandManager;
import com.beacmc.beacmcstaffwork.command.admin.subcommand.DeleteUserSubcommand;
import com.beacmc.beacmcstaffwork.command.admin.subcommand.ReloadSubcommand;
import com.beacmc.beacmcstaffwork.command.admin.subcommand.ResetSubcommand;
import com.beacmc.beacmcstaffwork.command.admin.subcommand.StatsSubcommand;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.api.command.CommandManager;
import com.beacmc.beacmcstaffwork.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StaffAdminCommand extends CommandManager {

    private final SubcommandManager subcommandManager;

    public StaffAdminCommand() {
        super("staffworkadmin");
        subcommandManager = new SubcommandManager();
        subcommandManager.registerSubcommands(new ReloadSubcommand(), new ResetSubcommand(), new StatsSubcommand(), new DeleteUserSubcommand());
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("beacmcstaffwork.admin")){
            sender.sendMessage(Color.compile(Config.getString("settings.messages.no-permission")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))));
            return;
        }

        if (subcommandManager.executeSubcommands(sender, args))
            return;

        sender.sendMessage("Неверное использование команды");
    }

    public SubcommandManager getSubcommandManager() {
        return subcommandManager;
    }
}
