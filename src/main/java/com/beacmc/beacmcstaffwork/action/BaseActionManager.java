package com.beacmc.beacmcstaffwork.action;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BaseActionManager implements ActionManager {

    private final HashSet<Action> registerActions;
    private final BeacmcStaffWork plugin;

    public BaseActionManager() {
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
        Arrays.stream(actions).forEach(this::registerAction);
    }

    public void unregisterAction(Action action) {
        if(!isRegisterAction(action)) {
            plugin.getLogger().severe("action " + action.getName() + " not registered");
            return;
        }
        registerActions.remove(action);
    }

    public void unregisterAllActions() {
        for (Action action : registerActions) unregisterAction(action);
    }

    public boolean isRegisterAction(Action action) {
        String name = action.getName();
        for (Action execute : registerActions) {
            if(name.equals(execute.getName()))
                return true;
        }
        return false;
    }

    public void execute(OfflinePlayer player, List<String> actions, Pair<String, Object>... pairs) {
        actions.forEach(execute -> registerActions.stream()
                .filter(action -> execute.startsWith(action.getName()))
                .findFirst()
                .ifPresent(action -> action.execute(player, execute.replace(action.getName(), "").trim(), pairs)));
    }

    public HashSet<Action> getRegisterActions() {
        return registerActions;
    }
}
