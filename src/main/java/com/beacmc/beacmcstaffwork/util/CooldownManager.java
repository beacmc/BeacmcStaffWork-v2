package com.beacmc.beacmcstaffwork.util;


import org.bukkit.command.CommandSender;

public class CooldownManager {

    private CommandSender user;
    private long lastActionTime;
    private long cooldownTime;

    public CooldownManager(CommandSender user){
        this.user = user;
        this.cooldownTime = 0;
        this.lastActionTime = 0;
    }

    public boolean isCooldown() {
        if (user.hasPermission("beacmcstaffwork.cooldown.bypass"))
            return false;

        long l = System.currentTimeMillis() - lastActionTime;
        return l < cooldownTime;
    }

    public void execute(long cooldownTime) {
        this.cooldownTime = cooldownTime;
        this.lastActionTime = System.currentTimeMillis();
    }

    public CommandSender getCommandSender() {
        return user;
    }
}
