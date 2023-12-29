package com.beacmc.beacmcstaffwork.api.manager;

public abstract class CustomAddonHook extends Command {

    private final String command;

    public CustomAddonHook(String command) {
        assert command != null;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
