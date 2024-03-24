package com.beacmc.beacmcstaffwork.discord;

import com.beacmc.beacmcstaffwork.discord.command.LinkCommand;
import com.beacmc.beacmcstaffwork.discord.command.StatsCommand;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {

    private JDA jda;
    private Guild guild;

    public DiscordBot() {}

    public void connect() {
        jda = JDABuilder.createDefault(Config.getString("settings.discord.token"))
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new LinkCommand(), new StatsCommand())
                .build();

        guild = jda.getGuildById(Long.valueOf(Config.getString("settings.discord.guild-id")));

        if(Config.getBoolean("settings.discord.activity.enable")) {
            jda.getPresence().setActivity(
                    Activity.of(
                            Activity.ActivityType.valueOf(Config.getString("settings.discord.activity.type")),
                            Config.getString("settings.discord.activity.text"),
                            Config.getString("settings.discord.activity.url")
                    ));
        }

    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }
}
