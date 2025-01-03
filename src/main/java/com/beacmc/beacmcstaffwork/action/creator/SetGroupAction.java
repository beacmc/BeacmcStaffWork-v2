package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.util.Pair;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SetGroupAction implements Action {

    private final LuckPerms luckPerms;

    public SetGroupAction() {
        luckPerms = BeacmcStaffWork.getLuckPerms();
    }

    @Override
    public String getName() {
        return "[SET_GROUP]";
    }

    @Override
    public String getDescription() {
        return "set primary group";
    }

    @Override
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        if (player == null) return;

        User user = luckPerms.getPlayerAdapter(OfflinePlayer.class).getUser(player);
        if (luckPerms.getGroupManager().getGroup(params) != null) {
            user.setPrimaryGroup(params);
        }
    }
}
