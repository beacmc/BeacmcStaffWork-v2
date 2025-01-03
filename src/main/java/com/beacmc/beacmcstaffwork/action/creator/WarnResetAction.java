package com.beacmc.beacmcstaffwork.action.creator;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.action.Action;
import com.beacmc.beacmcstaffwork.util.Pair;
import com.beacmc.beacmcstaffwork.warn.WarnManager;
import com.beacmc.beacmcstaffwork.warn.WarnType;
import org.bukkit.OfflinePlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarnResetAction implements Action {

    private final Pattern playerPattern = Pattern.compile("\\[player=(\\w+)]");
    private final Pattern warnTypePattern = Pattern.compile("\\[type=(\\w+)]");

    private final WarnManager warnManager;

    public WarnResetAction() {
        this.warnManager = BeacmcStaffWork.getWarnManager();
    }

    @Override
    public String getName() {
        return "[warn_remove]";
    }

    @Override
    public String getDescription() {
        return "reset warns by type";
    }

    @Override
    public void execute(OfflinePlayer player, String params, Pair<String, Object>... pairs) {
        final Matcher playerMatcher = playerPattern.matcher(params);
        final Matcher warnTypeMatcher = warnTypePattern.matcher(params);

        String playerName = playerMatcher.find() ? playerMatcher.group(1) : null;
        WarnType type = warnTypeMatcher.find() ? findWarnType(warnTypeMatcher.group(1)) : WarnType.VERBAL;

        if (playerName == null)
            return;

        warnManager.resetWarns(playerName, type);
    }

    WarnType findWarnType(String name) {
        try {
            return WarnType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WarnType.VERBAL;
        }
    }
}
