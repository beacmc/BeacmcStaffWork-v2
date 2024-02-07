package com.beacmc.beacmcstaffwork.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Webhook {

    private static final WebhookClient webhook = WebhookClient.withUrl(Config.getString("settings.discord.webhook-url"));

    public static void sendWebhook(
            Player player, String title, String titleUrl, String description, String authorName, String authorIcon, String image, Integer color
    ) {
        if(!Config.getBoolean("settings.discord.enable")) {
            return;
        }

        title = PlaceholderAPI.setPlaceholders(player, title);
        description = PlaceholderAPI.setPlaceholders(player, description);
        authorName = PlaceholderAPI.setPlaceholders(player, authorName);

        WebhookEmbedBuilder embed = (new WebhookEmbedBuilder())
                .setTitle(new WebhookEmbed.EmbedTitle(title, titleUrl))
                .setDescription(description)
                .setImageUrl(image)
                .setAuthor(new WebhookEmbed.EmbedAuthor(authorName, authorIcon, ""))
                .setColor(color);
        webhook.send(embed.build());
    }
}
