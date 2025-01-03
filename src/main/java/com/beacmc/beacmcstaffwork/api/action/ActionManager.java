package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public interface ActionManager {

    void registerAction(Action action);

    void registerActions(Action... actions);

    void unregisterAction(Action action);

    void unregisterAllActions();

    boolean isRegisterAction(Action action);

    void execute(OfflinePlayer player, List<String> actions, Pair<String, Object>... pairs);

    HashSet<Action> getRegisterActions();
}
