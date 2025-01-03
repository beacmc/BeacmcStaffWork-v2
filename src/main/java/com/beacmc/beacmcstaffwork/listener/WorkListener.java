package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.discord.Embed;

import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WorkListener implements Listener {

    private TextChannel enableChannel;
    private TextChannel disableChannel;

    public WorkListener() {
        final ConfigurationSection discordSettings = BeacmcStaffWork.getMainConfig().getDiscordSettings();
        if (discordSettings.getBoolean("enable")) {
            loadChannels();
        }
    }

    @EventHandler
    public void onEnableWork(PlayerEnableWorkEvent event) {
        if(enableChannel == null) return;
        sendMessage(event.getStaffPlayer().getPlayer(), enableChannel, "on-enable-work");
    }

    @EventHandler
    public void onDisableWork(PlayerDisableWorkEvent event) {
        if(disableChannel == null) return;
        sendMessage(event.getStaffPlayer().getPlayer(), disableChannel, "on-disable-work");
    }


    private void loadChannels() {
        enableChannel = getChannel("on-enable-work.channel-id");
        disableChannel = getChannel("on-disable-work.channel-id");
    }

    private TextChannel getChannel(String channelIdConfig) {
        final ConfigurationSection discordSettings = BeacmcStaffWork.getMainConfig().getDiscordSettings();
        return BeacmcStaffWork.getDiscordBot().getGuild()
                .getTextChannelById(discordSettings.getLong(channelIdConfig));
    }
    private void sendMessage(Player player, TextChannel channel, String configPrefix) {
        final ConfigurationSection discordSettings = BeacmcStaffWork.getMainConfig().getDiscordSettings();

        String title = PlaceholderAPI.setPlaceholders(player, discordSettings.getString(configPrefix + ".title"));
        String titleUrl = discordSettings.getString(configPrefix + ".title-url");
        String author = PlaceholderAPI.setPlaceholders(player, discordSettings.getString(configPrefix + ".author-name"));
        String authorIcon = discordSettings.getString(configPrefix + ".author-icon-url");
        String image = discordSettings.getString(configPrefix + ".image-url");
        String description = PlaceholderAPI.setPlaceholders(player, discordSettings.getString(configPrefix + ".description"));
        String color = discordSettings.getString(configPrefix + ".color");

        channel.sendMessageEmbeds(Embed.of(title, titleUrl, author, authorIcon, image, description, color).build()).queue();
    }
}
