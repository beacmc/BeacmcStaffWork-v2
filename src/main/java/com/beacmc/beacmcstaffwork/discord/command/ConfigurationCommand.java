package com.beacmc.beacmcstaffwork.discord.command;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.collections4.bag.CollectionBag;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.PseudoColumnUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConfigurationCommand {

    private String commandPrefix;
    private Collection<Permission> requiredPermissions;
    private List<Role> requiredRoles;
    private ConfigurationSection section;

    public ConfigurationCommand(ConfigurationSection section) {
        requiredPermissions = new LinkedList<>();
        requiredRoles = new LinkedList<>();
        reload(section);
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public Collection<Permission> getRequiredPermissions() {
        return requiredPermissions;
    }

    public List<Role> getRequiredRoles() {
        return requiredRoles;
    }

    public String getMessage(String key) {
        return section.getString("messages." + key);
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public void reload(ConfigurationSection section) {
        this.section = section;

        commandPrefix = section.getString("command");

        for (String permissionString : section.getStringList("required-permissions")) {
            requiredPermissions.clear();
            Permission permission = getPermission(permissionString);
            if (permission != null) requiredPermissions.add(permission);
        }

        for (long roleLong : section.getLongList("required-roles")) {
            requiredRoles.clear();
            Role role = getRole(roleLong);
            if (role != null) requiredRoles.add(role);
        }
    }

    private Permission getPermission(String perm) {
        try {
            return Permission.valueOf(perm.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Role getRole(long roleId) {
        Guild guild = BeacmcStaffWork.getDiscordBot().getGuild();
        if (guild == null)
            return null;

        return guild.getRoleById(roleId);
    }
}
