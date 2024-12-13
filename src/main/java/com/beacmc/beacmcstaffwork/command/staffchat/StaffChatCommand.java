package com.beacmc.beacmcstaffwork.command.staffchat;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.api.command.Command;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.config.Config;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChatCommand extends Command {

    public StaffChatCommand() {
        super("staffchat");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("only player");
            return;
        }

        StaffPlayer user = new StaffPlayer((Player) sender);
        if (!user.hasPermission("beacmcstaffwork.chat")) {
            user.sendMessage("settings.messages.no-permission");
            return;
        }

        if (args.length == 0) {
            user.sendMessage("settings.messages.error-use");
            return;
        }

        final boolean isBroadcast = Arrays.asList(args).contains("-bc");

        final String messageKey = isBroadcast && user.hasPermission("beacmcstaffwork.chat.broadcast") ? "settings.chat-broadcast-format" : "settings.chat-format";
        final String messageTemplate = Config.getString(messageKey);

        args = Arrays.stream(args)
                .filter(arg -> !"-bc".equals(arg))
                .toArray(String[]::new);

        String message = String.join(" ", args);

        final String format = Color.compile(PlaceholderAPI.setPlaceholders(user.getPlayer(), messageTemplate))
                .replace("{MESSAGE}", message);

        sendPluginMessage(user.getPlayer(), format);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("beacmcstaffwork.chat"))
                .forEach(player -> player.sendMessage(format));

        if (Config.getBoolean("settings.discord.commands.staff-chat-command.staff-chat-sync") && BeacmcStaffWork.getDiscordBot() != null)
            sendMessageToDiscord(user.getPlayer(), message, isBroadcast);
    }

    private void sendMessageToDiscord(Player player, String message, boolean isBroadcast) {
        final String messageKey = isBroadcast ? "settings.chat-broadcast-game-to-discord" : "settings.chat-game-to-discord";
        final String channelKey = isBroadcast ? "broadcast-channel-id" : "chat-channel-id";
        final long channelId = Config.getLong("settings.discord.commands.staff-chat-command." + channelKey);
        final String messageTemplate = Config.getString(messageKey);

        final String format = PlaceholderAPI.setPlaceholders(player, messageTemplate)
                .replace("{MESSAGE}", message);

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
