package com.beacmc.beacmcstaffwork.api.action;

import com.beacmc.beacmcstaffwork.player.StaffPlayer;

public interface Action {

    String getName();

    String getDescription();

    void execute(StaffPlayer player, String params);
}
