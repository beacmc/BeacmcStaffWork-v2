package com.beacmc.beacmcstaffwork.hook.placeholderapi;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.work.Work;
import com.beacmc.beacmcstaffwork.config.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class PlaceholderHook extends PlaceholderExpansion {

    private static final BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();


    public void load() {
        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "beacmcstaffwork";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of("moderators_on_work", "player_work", "time_in_work", "bans", "kicks", "mutes");
    }

    @Override
    public boolean persist() {
        return true;
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
        User user;
        UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();
        try {
            user = userDao.queryForId(player.getName().toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }


        if(player == null || user == null)
            return "";

        if (params.equals("moderators_on_work"))
            return String.valueOf(BeacmcStaffWork.getUsers().size());

        else if (params.equals("player_work"))
            return user.isWork() ? Color.compile(Config.getString("settings.placeholderapi.placeholders.on-work")) : Color.compile(Config.getString("settings.placeholderapi.placeholders.not-work"));

        else if (params.equals("time_in_work"))
            return Color.compile(Work.getTimeFormat(user));

        else if(params.equals("bans"))
            return String.valueOf(user.getBans());

        else if(params.equals("mutes"))
            return String.valueOf(user.getMutes());

        else if(params.equals("kicks"))
            return String.valueOf(user.getKicks());

        else if(params.equals("unbans"))
            return String.valueOf(user.getUnbans());

        else if(params.equals("unmutes"))
            return String.valueOf(user.getUnmutes());

        else
            return "";

    }
}
