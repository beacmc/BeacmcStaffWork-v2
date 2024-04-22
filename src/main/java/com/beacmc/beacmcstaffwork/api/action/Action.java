package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;
import org.bukkit.entity.Player;

public abstract class Action {

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute(StaffPlayer player, String params);
}
