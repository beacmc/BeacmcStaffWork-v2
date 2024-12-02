package com.beacmc.beacmcstaffwork.command.staffmenu;

import com.beacmc.beacmcstaffwork.api.command.Command;
import com.beacmc.beacmcstaffwork.menu.StaffPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffMenuCommand extends Command {

    public StaffMenuCommand() {
        super("staffplayers");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player");
            return;
        }
        new StaffPanel().open(((Player) sender));
    }
}
