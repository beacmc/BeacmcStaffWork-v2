package com.beacmc.beacmcstaffwork.hook.placeholderapi;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.work.TimeUtil;
import com.beacmc.beacmcstaffwork.config.Config;
import com.j256.ormlite.stmt.QueryBuilder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class PlaceholderHook extends PlaceholderExpansion {

    private static final BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();
    private final UserDao userDao;


    public PlaceholderHook() {
        userDao = BeacmcStaffWork.getDatabase().getUserDao();
    }

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
            return Color.compile(TimeUtil.getFormattedTime(user.getTime()));

        else if(params.startsWith("bans")) {
            if (params.equals("bans_all")) {
                return String.valueOf(getStatisticSum("bans"));
            }
            return String.valueOf(user.getBans());
        }

        else if(params.startsWith("mutes")) {
            if (params.equals("mutes_all")) {
                return String.valueOf(getStatisticSum("mutes"));
            }
            return String.valueOf(user.getMutes());
        }

        else if(params.startsWith("kicks")) {
            if (params.equals("kicks_all")) {
                return String.valueOf(getStatisticSum("kicks"));
            }
            return String.valueOf(user.getKicks());
        }

        else if(params.startsWith("unbans")) {
            if (params.equals("unbans_all")) {
                return String.valueOf(getStatisticSum("unbans"));
            }
            return String.valueOf(user.getUnbans());
        }

        else if(params.startsWith("unmutes")) {
            if (params.equals("unmutes_all")) {
                return String.valueOf(getStatisticSum("unmutes"));
            }
            return String.valueOf(user.getUnmutes());
        }

        else
            return "";

    }

    private String getStatisticSum(String column) {
        try {
            QueryBuilder<User, String> queryBuilder = userDao.queryBuilder();
            return queryBuilder.selectRaw("SUM(" + column + ") AS total")
                    .queryRaw()
                    .getResults()
                    .get(0)[0];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
