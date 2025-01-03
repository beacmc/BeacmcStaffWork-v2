package com.beacmc.beacmcstaffwork.discord;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.discord.command.creator.LinkCommand;
import com.beacmc.beacmcstaffwork.discord.command.creator.StaffChatCommand;
import com.beacmc.beacmcstaffwork.discord.command.creator.StatsCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;


public class DiscordBot {

    private JDA jda;
    private Guild guild;

    public DiscordBot() {}

    public void connect() {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection discordSettings = config.getDiscordSettings();

        try {
            JDALogger.setFallbackLoggerEnabled(false);
            jda = JDABuilder.createDefault(discordSettings.getString("token"))
                    .enableIntents(Arrays.asList(GatewayIntent.values()))
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        guild = jda.getGuildById(discordSettings.getLong("guild-id"));

        if(discordSettings.getBoolean("activity.enable")) {
            jda.getPresence().setActivity(
                    Activity.of(
                            Activity.ActivityType.valueOf(discordSettings.getString("activity.type", "PLAYING")),
                            discordSettings.getString("activity.text", "beacmc"),
                            discordSettings.getString("activity.url")
                    ));
        }
        new LinkCommand(jda);
        new StaffChatCommand(jda);
        new StatsCommand(jda);
    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }
}
