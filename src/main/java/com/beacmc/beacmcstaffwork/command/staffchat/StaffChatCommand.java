package com.beacmc.beacmcstaffwork.command.staffchat;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.api.command.Command;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Message;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChatCommand extends Command {

    private final StaffWorkManager manager;

    public StaffChatCommand() {
        super("staffchat");
        manager = BeacmcStaffWork.getStaffWorkManager();
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("only player");
            return;
        }

        final StaffPlayer user = manager.getStaffPlayerByPlayer(player);
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection discordSettings = config.getDiscordSettings();
        final ConfigurationSection settings = config.getSettings();

        if (user == null) {
            player.sendMessage(Message.getMessageFromConfig("staff-player-not-found"));
            return;
        }

        if (!user.hasPermission("beacmcstaffwork.chat")) {
            user.sendMessage(Message.getMessageFromConfig("no-permission"));
            return;
        }

        if (args.length == 0) {
            user.sendMessage(Message.getMessageFromConfig("error-use"));
            return;
        }

        final boolean isBroadcast = Arrays.asList(args).contains("-bc");
        final String messageKey = isBroadcast && user.hasPermission("beacmcstaffwork.chat.broadcast") ? "chat-broadcast-format" : "chat-format";
        final String messageTemplate = settings.getString(messageKey, "ERROR");

        args = Arrays.stream(args)
                .filter(arg -> !"-bc".equals(arg))
                .toArray(String[]::new);

        final String message = String.join(" ", args);
        final String format = Message.of(PlaceholderAPI.setPlaceholders(user.getPlayer(), messageTemplate.replace("{MESSAGE}", message)));

        sendPluginMessage(user.getPlayer(), format);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("beacmcstaffwork.chat"))
                .forEach(p -> p.sendMessage(format));

        if (discordSettings.getBoolean("commands.staff-chat-command.staff-chat-sync") && BeacmcStaffWork.getDiscordBot() != null)
            sendMessageToDiscord(user.getPlayer(), message, isBroadcast);
    }

    private void sendMessageToDiscord(Player player, String message, boolean isBroadcast) {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection discordSettings = config.getDiscordSettings();
        final ConfigurationSection settings = config.getSettings();

        final String messageKey = isBroadcast ? "chat-broadcast-game-to-discord" : "chat-game-to-discord";
        final String channelKey = isBroadcast ? "broadcast-channel-id" : "chat-channel-id";
        final long channelId = discordSettings.getLong("commands.staff-chat-command." + channelKey);
        final String messageTemplate = settings.getString(messageKey, "MESSAGE NOT FOUND");

        final String format = PlaceholderAPI.setPlaceholders(player, messageTemplate.replace("{MESSAGE}", message));

        TextChannel channel = BeacmcStaffWork.getDiscordBot().getGuild().getTextChannelById(channelId);
        if (channel == null)
            return;

        channel.sendMessage(format).queue();
    }

    private void sendPluginMessage(Player player, String message) {
        BeacmcStaffWork plugin = BeacmcStaffWork.getInstance();
        if(!player.getServer().getMessenger().isIncomingChannelRegistered(plugin, "BungeeCord"))
            return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("beacmcstaffwork");
        out.writeUTF(message);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
