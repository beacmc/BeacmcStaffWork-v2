package com.beacmc.beacmcstaffwork.api.action.model;

import com.beacmc.beacmcstaffwork.api.action.AbstractAction;

public class Action {

    private final String name;
    private final String description;
    private final AbstractAction action;

    public Action(String name, String description, AbstractAction action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public AbstractAction getAction() {
        return action;
    }
}
