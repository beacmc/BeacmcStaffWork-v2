package com.beacmc.beacmcstaffwork.discord;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Embed {

    public static EmbedBuilder of(String title, String titleUrl, String author, String authorIcon, String image, String description, String color) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(color));
        builder.setTitle(title, titleUrl);
        builder.setDescription(description);
        if(image != null && !image.isEmpty()) {
            builder.setImage(image);
        }

        return builder;
    }
}
