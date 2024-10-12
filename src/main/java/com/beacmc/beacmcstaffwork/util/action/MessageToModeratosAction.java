package com.beacmc.beacmcstaffwork.util.action;

import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.work.Work;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;

public class MessageToModeratosAction implements Action {


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
