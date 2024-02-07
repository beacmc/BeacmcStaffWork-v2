package com.beacmc.beacmcstaffwork.data;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.User;
import com.beacmc.beacmcstaffwork.manager.Work;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {

    private static final BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();


    public void load() {
        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "beacmcstaffwork";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    public String onPlaceholderRequest(Player player, String params) {
        User user = new User(player);
        assert player != null;
        if (params.equals("moderators_on_work")) {
            return String.valueOf(Work.moderatorsInWork(user));
        }

        else if (params.equals("player_work")) {
            return user.isWork() ? Color.compile(Config.getString("settings.placeholderapi.placeholders.on-work")) : Color.compile(Config.getString("settings.placeholderapi.placeholders.not-work"));
        }

        else if (params.equals("time_in_work")) {
            return Color.compile(Work.getTimeFormat(user));
        }

        else if(params.equals("bans")) {
            return String.valueOf(user.getBans());
        }

        else if(params.equals("mutes")) {
            return String.valueOf(user.getMutes());
        }

        else if(params.equals("kicks")) {
            return String.valueOf(user.getKicks());
        }

        else {
            return "";
        }
    }
}
