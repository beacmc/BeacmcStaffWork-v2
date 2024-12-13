package com.beacmc.beacmcstaffwork.discord.command.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.discord.command.DiscordCommand;
import com.beacmc.beacmcstaffwork.work.TimeUtil;
import com.beacmc.beacmcstaffwork.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StatsCommand extends DiscordCommand {

    private final UserDao userDao;

    public StatsCommand(JDA jda) {
        super(jda, "settings.discord.commands.stats-command");
        this.userDao = BeacmcStaffWork.getDatabase().getUserDao();
    }

    @Override
    public void execute(Member member, MessageChannelUnion channel, String[] args) {
        try {
            User user = getUser(member, args.length > 0 ? args[0] : null);

            if (user == null) {
                channel.sendMessage(getConfigurationCommand().getMessage("user-not-found")).queue();
                return;
            }

            channel.sendMessage(
                    getConfigurationCommand().getMessage("user-stats")
                            .replace("{user}", user.getNickname())
                            .replace("{is-work}", user.isWork() ? Config.getString("settings.placeholderapi.placeholders.on-work") : Config.getString("settings.placeholderapi.placeholders.not-work"))
                            .replace("{time-work}", TimeUtil.getFormattedTime(user.getTime()))
                            .replace("{bans}", String.valueOf(user.getBans()))
                            .replace("{mutes}", String.valueOf(user.getMutes()))
                            .replace("{kicks}", String.valueOf(user.getKicks()))
                            .replace("{unbans}", String.valueOf(user.getUnbans()))
                            .replace("{unmutes}", String.valueOf(user.getUnmutes()))

            ).queue();
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private User getUser(Member member, String name) throws SQLException {
        if (name != null && !name.isEmpty()) {
            return userDao.queryForId(name.toLowerCase());
        }
        return userDao.queryForEq("discord_id", member.getId()).stream()
                .findFirst()
                .orElse(null);
    }
}
