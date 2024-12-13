package com.beacmc.beacmcstaffwork.discord.command;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public abstract class DiscordCommand extends ListenerAdapter {

    private ConfigurationCommand configCommand;

    public DiscordCommand(JDA jda, String configPath) {
        jda.addEventListener(this);
        configCommand = new ConfigurationCommand(Config.getSection(configPath));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(configCommand.getCommandPrefix()))
            return;

        final Member member = event.getMember();
        final String content = event.getMessage().getContentRaw().replace(configCommand.getCommandPrefix(), "").trim();
        final MessageChannelUnion channel = event.getChannel();
        final String[] args = content.split("\\s+");

        if (member == null)
            return;

        if (!checkPermissions(member)) {
            channel.sendMessage(configCommand.getMessage("no-permission")).queue();
            return;
        }

        if (!checkRoles(member)) {
            channel.sendMessage(configCommand.getMessage("no-roles")).queue();
            return;
        }

        execute(member, channel, args);
    }

    public ConfigurationCommand getConfigurationCommand() {
        return configCommand;
    }

    private boolean checkPermissions(Member member) {
        if (configCommand.getRequiredPermissions().size() == 0)
            return true;

        return member.hasPermission(configCommand.getRequiredPermissions());
    }

    private boolean checkRoles(Member member) {
        if (configCommand.getRequiredRoles().size() == 0)
            return true;

        return new HashSet<>(member.getRoles()).containsAll(configCommand.getRequiredRoles());
    }

    public abstract void execute(Member member, MessageChannelUnion channel, String[] args);
}
