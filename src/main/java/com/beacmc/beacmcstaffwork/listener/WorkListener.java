package com.beacmc.beacmcstaffwork.listener;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.event.PlayerDisableWorkEvent;
import com.beacmc.beacmcstaffwork.api.event.PlayerEnableWorkEvent;
import com.beacmc.beacmcstaffwork.discord.Embed;
import com.beacmc.beacmcstaffwork.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WorkListener implements Listener {
    private TextChannel enableChannel;
    private TextChannel disableChannel;

    public WorkListener() {
        if (Config.getBoolean("settings.discord.enable")) {
            loadChannels();
        }
    }

    @EventHandler
    public void onEnableWork(PlayerEnableWorkEvent event) {
        if(enableChannel == null) return;
        sendMessage(event.getPlayer(), enableChannel, "on-enable-work");
    }

    @EventHandler
    public void onDisableWork(PlayerDisableWorkEvent event) {
        if(disableChannel == null) return;
        sendMessage(event.getPlayer(), disableChannel, "on-disable-work");
    }


    private void loadChannels() {
        Long guildId = Long.valueOf(Config.getString("settings.discord.guild-id"));
        enableChannel = getChannel(guildId, "settings.discord.on-enable-work.channel-id");
        disableChannel = getChannel(guildId, "settings.discord.on-disable-work.channel-id");
    }

    private TextChannel getChannel(Long guildId, String channelIdConfig) {
        return BeacmcStaffWork.getDiscordBot().getJDA().getGuildById(guildId)
                .getTextChannelById(Long.valueOf(Config.getString(channelIdConfig)));
    }
    private void sendMessage(Player player, TextChannel channel, String eventSuffix) {
        String configPrefix = "settings.discord." + eventSuffix;
        String title = PlaceholderAPI.setPlaceholders(player, Config.getString(configPrefix + ".title"));
        String titleUrl = Config.getString(configPrefix + ".title-url");
        String author = PlaceholderAPI.setPlaceholders(player, Config.getString(configPrefix + ".author-name"));
        String authorIcon = Config.getString(configPrefix + ".author-icon-url");
        String image = Config.getString(configPrefix + ".image-url");
        String description = PlaceholderAPI.setPlaceholders(player, Config.getString(configPrefix + ".description"));
        String color = Config.getString(configPrefix + ".color");

        channel.sendMessageEmbeds(Embed.of(title, titleUrl, author, authorIcon, image, description, color).build()).queue();
    }
}
