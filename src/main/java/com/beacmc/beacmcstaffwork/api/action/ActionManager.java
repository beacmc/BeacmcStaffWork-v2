package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.List;

public class ActionManager {

    private final HashSet<Action> registerActions;
    private final BeacmcStaffWork plugin;

    public ActionManager() {
        plugin = BeacmcStaffWork.getInstance();
        registerActions = new HashSet<>();
    }

    public void registerAction(Action action) {
        if(isRegisterAction(action)) {
            plugin.getLogger().severe("action " + action.getName() + " already registered");
            return;
        }
        plugin.getLogger().info("register action " + action.getName() + " - " + action.getDescription());
        registerActions.add(action);
    }

    public void registerActions(Action... actions) {
        for(Action action : actions) {
            registerAction(action);
        }
    }

    public void unregisterAction(Action action) {
        if(!isRegisterAction(action)) {
            plugin.getLogger().severe("action " + action.getName() + " not registered");
            return;
        }
        registerActions.remove(action);
    }

    public void unregisterAllActions() {
        registerActions.forEach(this::unregisterAction);
    }

    public boolean isRegisterAction(Action action) {
        String name = action.getName();
        for (Action execute : registerActions) {
            if(name.equals(execute.getName()))
                return true;
        }
        return false;
    }

    public void execute(StaffPlayer player, List<String> actions) {
        for (String execute : actions) {
            for (Action action : registerActions) {
                String name = action.getName();
                if (!execute.startsWith(name))
                    continue;

                action.execute(player, execute.replace(name, "").trim());
            }
        }
    }

    public HashSet<Action> getRegisterActions() {
        return registerActions;
    }
}
