package com.beacmc.beacmcstaffwork.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class MessagingListener implements PluginMessageListener {


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        ByteArrayDataInput arrayInput = ByteStreams.newDataInput(message);

        String sub = arrayInput.readUTF();
        if(!sub.equalsIgnoreCase("beacmcstaffwork"))
            return;

        final String msg = arrayInput.readUTF();
        Bukkit.getOnlinePlayers().forEach(execute -> {
            if (execute.hasPermission("beacmcstaffwork.chat")) {
                execute.sendMessage(msg);
            }
        });
    }
}
