package com.beacmc.beacmcstaffwork.discord.command;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.work.Work;
import com.beacmc.beacmcstaffwork.config.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;

public class StatsCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        String[] args = msg.split(" ");
        Member member = event.getMember();
        if(msg.startsWith(Config.getString("settings.discord.commands.stats-command.command"))) {
            try {
                TextChannel channel = event.getChannel().asTextChannel();
                UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();

                User user = null;
                if(args.length >= 2) {
                    user = userDao.queryForId(args[1].toLowerCase());
                } else {
                    List<User> query = userDao.queryForEq("discord_id", member.getIdLong());
                    if (query.size() > 0) {
                        user = query.get(0);
                    }
                }

                if(user == null) {
                    channel.sendMessage(Config.getString("settings.discord.commands.stats-command.messages.user-not-found")).queue();
                    return;
                }

                channel.sendMessage(
                        Config.getString("settings.discord.commands.stats-command.messages.user-stats")
                                .replace("{user}", user.getNickname())
                                .replace("{is-work}", user.isWork() ? Config.getString("settings.placeholderapi.placeholders.on-work") : Config.getString("settings.placeholderapi.placeholders.not-work"))
                                .replace("{time-work}", Work.getTimeFormat(user))
                                .replace("{bans}", String.valueOf(user.getBans()))
                                .replace("{mutes}", String.valueOf(user.getMutes()))
                                .replace("{kicks}", String.valueOf(user.getKicks()))
                                .replace("{unbans}", String.valueOf(user.getUnbans()))
                                .replace("{unmutes}", String.valueOf(user.getUnmutes()))

                ).queue();
            } catch (SQLException | IllegalArgumentException e) { }
        }
    }
}
