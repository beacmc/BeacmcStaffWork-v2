package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.database.model.Warn;
import com.beacmc.beacmcstaffwork.util.Pair;
import com.beacmc.beacmcstaffwork.warn.WarnManager;
import com.beacmc.beacmcstaffwork.warn.WarnType;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarnAddAction implements Action {


    private final Pattern authorPattern = Pattern.compile("\\[author=(\\w+)]");
    private final Pattern playerPattern = Pattern.compile("\\[player=(\\w+)]");
    private final Pattern warnTypePattern = Pattern.compile("\\[type=(\\w+)]");
    private final Pattern reasonPattern = Pattern.compile("\\[reason=(\\w+)]");

    private final WarnManager warnManager;
    private final ActionManager actionManager;

    public WarnAddAction() {
        this.warnManager = BeacmcStaffWork.getWarnManager();
        this.actionManager = BeacmcStaffWork.getActionManager();
    }

    @Override
    public String getName() {
        return "[warn_add]";
    }

    @Override
    public String getDescription() {
        return "add warn";
    }

    @Override
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        final Matcher authorMatcher = authorPattern.matcher(params);
        final Matcher playerMatcher = playerPattern.matcher(params);
        final Matcher warnTypeMatcher = warnTypePattern.matcher(params);
        final Matcher reasonMatcher = reasonPattern.matcher(params);

        String author = authorMatcher.find() ? authorMatcher.group(1) : "CONSOLE";
        String playerName = playerMatcher.find() ? playerMatcher.group(1) : null;
        WarnType type = warnTypeMatcher.find() ? findWarnType(warnTypeMatcher.group(1)) : WarnType.VERBAL;
        String reason = reasonMatcher.find() ? reasonMatcher.group(1) : "Without reason";

        if (playerName == null)
            return;

        warnManager.addWarn(playerName, author, type, reason);

        ConfigurationSection warnSection = BeacmcStaffWork.getMainConfig()
                .getWarnsSettings()
                .getConfigurationSection(type == WarnType.VERBAL ? "verbal" : "severe");

        if (warnManager.getPlayerWarns(playerName, type).size() >= warnSection.getInt("max", 2)) {
            List<String> actions = BeacmcStaffWork.getNeedConfiguration()
                    .getActions()
                    .getStringList(type == WarnType.VERBAL ? "on-verbal-max-warns" : "on-severe-max-warns")
                    .stream()
                    .map(action -> action
                            .replace("{AUTHOR}", author)
                            .replace("{PLAYER}", playerName)
                    )
                    .toList();

            actionManager.execute(player, actions);
        }
    }

    WarnType findWarnType(String name) {
        try {
            return WarnType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WarnType.VERBAL;
        }
    }
}
