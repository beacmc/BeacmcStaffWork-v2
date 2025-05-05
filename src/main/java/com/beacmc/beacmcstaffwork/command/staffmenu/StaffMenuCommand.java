package com.beacmc.beacmcstaffwork.command.staffmenu;

import com.beacmc.beacmcstaffwork.api.command.Command;
import com.beacmc.beacmcstaffwork.menu.stafflist.StaffListPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffMenuCommand extends Command {

    public StaffMenuCommand() {
        super("staffplayers");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only player");
            return;
        }
        new StaffListPanel().open(player);
    }
}
