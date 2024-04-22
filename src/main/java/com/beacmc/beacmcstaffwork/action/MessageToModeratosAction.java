package com.beacmc.beacmcstaffwork.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.manager.core.Work;
import com.beacmc.beacmcstaffwork.manager.player.StaffPlayer;

public class MessageToModeratosAction extends Action {


    @Override
    public String getName() {
        return "[message_to_moderators]";
    }

    @Override
    public String getDescription() {
        return "message to moderators";
    }

    @Override
    public void execute(StaffPlayer player, String params) {
        Work.messageToModerator(player.getPlayer(), params);
    }
}
