package com.beacmc.beacmcstaffwork.warn;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.config.NeedConfiguration;
import com.beacmc.beacmcstaffwork.database.dao.WarnDao;
import com.beacmc.beacmcstaffwork.database.model.Warn;
import com.beacmc.beacmcstaffwork.util.TimeUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarnManager {

    private final List<Warn> warns;
    private final WarnDao warnDao;

    public WarnManager() {
        this.warns = new LinkedList<>();
        this.warnDao = BeacmcStaffWork.getDatabase().getWarnDao();
        loadWarns();
    }

    public List<Warn> getPlayerWarns(String name) {
        return warns.stream()
                .filter(warn -> warn.getPlayer().equalsIgnoreCase(name))
                .toList();
    }

    public List<Warn> getPlayerWarns(String name, WarnType type) {
        return getPlayerWarns(name).stream()
                .filter(warn -> warn.getType() == type)
                .toList();
    }

    public void addWarn(String player, String author, WarnType type, String reason) {
        Warn warn = new Warn().setAuthor(author)
                .setPlayer(player)
                .setType(type)
                .setReason(reason)
                .setDate(TimeUtil.getCurrentTime().toString());

        warnDao.createAsync(warn);
    }

    public void removeLastWarn(String name, WarnType type) {
        List<Warn> warns = getPlayerWarns(name, type);

        if (!warns.isEmpty()) {
            Warn lastWarn = warns.get(warns.size() - 1);
            warnDao.deleteAsync(lastWarn);
            getPlayerWarns(name).remove(lastWarn);
        }
    }

    public void resetWarns(String name, WarnType type) {
        List<Warn> playerWarns = getPlayerWarns(name, type);

        if (playerWarns.size() == 0)
            return;

        playerWarns.forEach(warn -> {
            warns.remove(warn);
            warnDao.deleteAsync(warn);
        });
    }

    public String getWarnAuthorByNumber(String player, int i) {
        Warn warn = getPlayerWarnByNumber(player, i);
        return warn != null ? warn.getAuthor() : null;
    }

    public WarnType getWarnTypeByNumber(String player, int i) {
        Warn warn = getPlayerWarnByNumber(player, i);
        return warn != null ? warn.getType() : null;
    }

    public Integer getWarnIdByNumber(String player, int i) {
        Warn warn = getPlayerWarnByNumber(player, i);
        return warn != null ? warn.getId() : null;
    }

    public String getWarnReasonByNumber(String player, int i) {
        Warn warn = getPlayerWarnByNumber(player, i);
        return warn != null ? warn.getReason() : null;
    }

    public String getWarnDateByNumber(String player, int i) {
        Warn warn = getPlayerWarnByNumber(player, i);
        return warn != null ? warn.getDate() : null;
    }

    public Warn getPlayerWarnByNumber(String name, int i) {
        List<Warn> playerWarns = getPlayerWarns(name);
        if (playerWarns.size() < i) {
            return null;
        }

        return playerWarns.get(i - 1);
    }

    protected void loadWarns() {
        warnDao.queryForAllAsync().thenAccept(warns::addAll);
    }

    public List<Warn> getWarns() {
        return warns;
    }
}
