package com.beacmc.beacmcstaffwork.discord.command.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.discord.command.DiscordCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class LinkCommand extends DiscordCommand {

    public LinkCommand(JDA jda) {
        super(jda, "settings.discord.commands.link-command");
    }

    @Override
    public void execute(Member member, MessageChannelUnion channel, String[] args) {
        try {
            long id;
            if (args.length != 2) {
                channel.sendMessage(getConfigurationCommand().getMessage("no-args")).queue();
                return;
            }
            UserDao userDao = BeacmcStaffWork.getDatabase().getUserDao();
            User user = userDao.queryForId(args[0]);

            if(user == null) {
                channel.sendMessage(getConfigurationCommand().getMessage("no-player")).queue();
                return;
            }
            try {
                id = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage(getConfigurationCommand().getMessage("number-format-exception")).queue();
                return;
            }
            userDao.update(user.setDiscordID(id));
            channel.sendMessage(getConfigurationCommand().getMessage("link-success")).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
