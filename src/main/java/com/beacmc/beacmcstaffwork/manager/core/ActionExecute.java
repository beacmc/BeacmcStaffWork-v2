package com.beacmc.beacmcstaffwork.manager.core;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionExecute {

    public static void execute(StaffPlayer player, List<String> actions) {
        ActionManager manager = BeacmcStaffWork.getActionManager();
        for (String execute : actions) {
            for (Action action : manager.getRegisterActions()) {
                String name = action.getName();
                if (!execute.startsWith(name))
                    continue;

                action.execute(player, execute.replace(name, "").trim());
            }
        }
    }
}
