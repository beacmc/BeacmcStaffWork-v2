package com.beacmc.beacmcstaffwork.discord.command;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class LinkCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        String[] args = msg.split(" ");
        Member member = event.getMember();
        long id;

        if(!member.hasPermission(Permission.ADMINISTRATOR))
            return;

        if(msg.startsWith(Config.getString("settings.discord.commands.link-command.command"))) {
            try {
                TextChannel channel = event.getChannel().asTextChannel();
                if (args.length != 3) {
                    channel.sendMessage(Config.getString("settings.discord.commands.link-command.messages.no-args")).queue();
                    return;
                }
                UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();
                User user = userDao.queryForId(args[1]);

                if(user == null) {
                    channel.sendMessage(Config.getString("settings.discord.commands.link-command.messages.no-player")).queue();
                    return;
                }
                try {
                    id = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    channel.sendMessage(Config.getString("settings.discord.commands.link-command.messages.number-format-exception")).queue();
                    return;
                }
                userDao.update(user.setDiscordID(id));
                channel.sendMessage(Config.getString("settings.discord.commands.link-command.messages.link-success")).queue();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
