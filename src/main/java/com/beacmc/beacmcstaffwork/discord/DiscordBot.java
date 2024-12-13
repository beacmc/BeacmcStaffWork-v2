package com.beacmc.beacmcstaffwork.discord;

import com.beacmc.beacmcstaffwork.discord.command.creator.LinkCommand;
import com.beacmc.beacmcstaffwork.discord.command.creator.StaffChatCommand;
import com.beacmc.beacmcstaffwork.discord.command.creator.StatsCommand;
import com.beacmc.beacmcstaffwork.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EventListener;


public class DiscordBot {

    private JDA jda;
    private Guild guild;

    public DiscordBot() {}

    public void connect() {
        try {
            JDALogger.setFallbackLoggerEnabled(false);
            jda = JDABuilder.createDefault(Config.getString("settings.discord.token"))
                    .enableIntents(Arrays.asList(GatewayIntent.values()))
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        guild = jda.getGuildById(Config.getLong("settings.discord.guild-id"));

        if(Config.getBoolean("settings.discord.activity.enable")) {
            jda.getPresence().setActivity(
                    Activity.of(
                            Activity.ActivityType.valueOf(Config.getString("settings.discord.activity.type")),
                            Config.getString("settings.discord.activity.text"),
                            Config.getString("settings.discord.activity.url")
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
