package com.beacmc.beacmcstaffwork.discord;

import org.bukkit.OfflinePlayer;

import java.util.Random;

public class StaffLink {

    private OfflinePlayer player;
    private long code;
    private boolean linked;

    public StaffLink(OfflinePlayer player) {
        this.player = player;
        code = 0;
        linked = false;
    }

    public void generateCode() {
        Random random = new Random();
        setCode(random.nextLong());
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    public boolean isLinked() {
        return linked;
    }

    public long getCode() {
        return code;
    }
}
