package com.beacmc.beacmcstaffwork.discord.command.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.discord.command.DiscordCommand;
import com.beacmc.beacmcstaffwork.util.Color;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public class StaffChatCommand extends DiscordCommand {

    public StaffChatCommand(JDA jda) {
        super(jda, "settings.discord.commands.staff-chat-command");
    }

    @Override
    public void execute(Member member, MessageChannelUnion channel, String[] args) {
        if (!getConfigurationCommand().getSection().getBoolean("staff-chat-sync") || args.length == 0)
            return;

        final boolean isBroadcast = Arrays.asList(args).contains("-bc");

        args = Arrays.stream(args)
                .filter(arg -> !"-bc".equals(arg))
                .toArray(String[]::new);

        String message = String.join(" ", args);

        sendMessageToGame(member, message, isBroadcast);
        sendMessageToDiscord(member, message, isBroadcast);
    }

    private void sendMessageToDiscord(Member member, String message, boolean isBroadcast) {
        final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();

        final String messageKey = isBroadcast ? "discord-chat-broadcast-format" : "discord-chat-format";
        final String channelKey = isBroadcast ? "broadcast-channel-id" : "chat-channel-id";
        final long channelId = getConfigurationCommand().getSection().getLong(channelKey);
        final String messageTemplate = settings.getString(messageKey);
        final String format = messageTemplate
                .replace("{DISCORD_USERNAME}", member.getUser().getEffectiveName())
                .replace("{MESSAGE}", String.join(" ", message));

        TextChannel channel = member.getGuild().getTextChannelById(channelId);
        if (channel == null)
            return;

        channel.sendMessage(format).queue();
    }

    private void sendMessageToGame(Member member, String message, boolean isBroadcast) {
        final ConfigurationSection settings = BeacmcStaffWork.getMainConfig().getSettings();

        final String messageKey = isBroadcast ? "chat-broadcast-discord-to-game" : "chat-discord-to-game";
        final String messageTemplate = settings.getString(messageKey);

        final String format = Color.compile(messageTemplate)
                .replace("{DISCORD_USERNAME}", member.getUser().getEffectiveName())
                .replace("{MESSAGE}", String.join(" ", message));

        sendPluginMessage(format);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("beacmcstaffwork.chat"))
                .forEach(player -> player.sendMessage(format));
    }

    private void sendPluginMessage(String message) {
        BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();
        if(!Bukkit.getServer().getMessenger().isIncomingChannelRegistered(plugin, "BungeeCord"))
            return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("beacmcstaffwork");
        out.writeUTF(message);
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
