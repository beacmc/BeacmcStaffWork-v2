package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.bukkit.Bukkit;

import java.util.HashSet;

public class ActionManager {

    private HashSet<Action> registerActions;
    private BeacmcStaffWork plugin;

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
            if (isRegisterAction(action)) {
                plugin.getLogger().severe("action " + action.getName() + " already registered");
                continue;
            }
            plugin.getLogger().info("register action " + action.getName() + " - " + action.getDescription());
            registerActions.add(action);
        }
    }

    public void unregisterAction(Action action) {
        if(!isRegisterAction(action)) {
            plugin.getLogger().severe("action " + action.getName() + " not registered");
            return;
        }
        plugin.getLogger().info("unregister action " + action.getName() + " - " + action.getDescription());
        registerActions.remove(action);
    }

    public boolean isRegisterAction(Action action) {
        String name = action.getName();
        for (Action execute : registerActions) {
            if(name.equals(execute.getName()))
                return true;
        }
        return false;
    }

    public HashSet<Action> getRegisterActions() {
        return registerActions;
    }
}
