package com.beacmc.beacmcstaffwork.api.event;

import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDisableWorkEvent extends Event implements Cancellable {

    private boolean cancel;
    private final StaffPlayer player;

    public PlayerDisableWorkEvent(StaffPlayer player) {
        this.player = player;
        this.cancel = false;
    }

    @Override
    public @NotNull HandlerList getHandlers(){
        return handlers;
    }

    public StaffPlayer getStaffPlayer() {
        return player;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
