package com.beacmc.beacmcstaffwork.messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class MessagingListener implements PluginMessageListener {


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        System.out.println(message);
        if(!channel.equalsIgnoreCase("staffchat"))
            return;

        if(player.hasPermission("beacmcstaffwork.chat")) {
            String[] data = new String(message, StandardCharsets.UTF_8).split("\\|");
            player.sendMessage(data[2]);
        }
    }
}
