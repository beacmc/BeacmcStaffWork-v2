package com.beacmc.beacmcstaffwork.discord.command.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.database.dao.UserDao;
import com.beacmc.beacmcstaffwork.database.model.User;
import com.beacmc.beacmcstaffwork.discord.command.DiscordCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.concurrent.CompletableFuture;

public class LinkCommand extends DiscordCommand {

    private final UserDao userDao;

    public LinkCommand(JDA jda) {
        super(jda, "settings.discord.commands.link-command");
        userDao = BeacmcStaffWork.getDatabase().getUserDao();
    }

    @Override
    public void execute(Member member, MessageChannelUnion channel, String[] args) {
        if (args.length != 2) {
            channel.sendMessage(getConfigurationCommand().getMessage("no-args")).queue();
            return;
        }

        CompletableFuture<User> future = userDao.queryForIdAsync(args[0]);
        future.thenAccept(user -> {
            long id;
            if (user == null) {
                channel.sendMessage(getConfigurationCommand().getMessage("no-player")).queue();
                return;
            }
            try {
                id = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage(getConfigurationCommand().getMessage("number-format-exception")).queue();
                return;
            }
            userDao.updateAsync(user.setDiscordID(id));
            channel.sendMessage(getConfigurationCommand().getMessage("link-success")).queue();
        });
    }
}
