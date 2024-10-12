package com.beacmc.beacmcstaffwork.command.admin.subcommand;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.subcommand.Subcommand;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.Color;
import org.bukkit.command.CommandSender;

public class ReloadSubcommand implements Subcommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            BeacmcStaffWork.getInstance().reloadConfig();
            sender.sendMessage(Color.compile(Config.getString("settings.messages.reload")
                    .replace("{PREFIX}", Config.getString("settings.prefix"))
            ));
        }
    }
}
