package com.beacmc.beacmcstaffwork.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(columnName = "nickname", canBeNull = false, id = true)
    private String nickname;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "is_work", defaultValue = "false")
    private boolean work;

    @DatabaseField(columnName = "work_start", defaultValue = "0")
    private long workStart;

    @DatabaseField(columnName = "time", defaultValue = "0")
    private long time;

    @DatabaseField(columnName = "bans", defaultValue = "0")
    private int bans;

    @DatabaseField(columnName = "mutes", defaultValue = "0")
    private int mutes;

    @DatabaseField(columnName = "kicks", defaultValue = "0")
    private int kicks;

    @DatabaseField(columnName = "unbans", defaultValue = "0")
    private int unbans;

    @DatabaseField(columnName = "unmutes", defaultValue = "0")
    private int unmutes;

    @DatabaseField(columnName = "discord_id", defaultValue = "0")
    private long discordID;

    public User() {}

    public User(String nickname, String password, boolean work, long workStart, long time, int bans, int mutes, int kicks, int unbans, int unmutes, long discordID) {
        setNickname(nickname);
        setPassword(password);
        setWork(work);
        setWorkStart(workStart);
        setTime(time);
        setBans(bans);
        setMutes(mutes);
        setKicks(kicks);
        setUnbans(unbans);
        setUnmutes(unmutes);
        setDiscordID(discordID);
    }

    public int getBans() {
        return bans;
    }

    public long getDiscordID() {
        return discordID;
    }

    public User setDiscordID(long discordID) {
        this.discordID = discordID;
        return this;
    }

    public int getUnbans() {
        return unbans;
    }

    public int getUnmutes() {
        return unmutes;
    }

    public User setUnbans(int unbans) {
        this.unbans = unbans;
        return this;
    }

    public User setUnmutes(int unmutes) {
        this.unmutes = unmutes;
        return this;
    }

    public User setBans(int bans) {
        this.bans = bans;
        return this;
    }

    public User setWorkStart(long workStart) {
        this.workStart = workStart;
        return this;
    }

    public User setKicks(int kicks) {
        this.kicks = kicks;
        return this;
    }

    public User setMutes(int mutes) {
        this.mutes = mutes;
        return this;
    }

    public User setTime(long time) {
        this.time = time;
        return this;
    }

    public int getKicks() {
        return kicks;
    }

    public int getMutes() {
        return mutes;
    }

    public long getTime() {
        return time;
    }

    public long getWorkStart() {
        return workStart;
    }

    public boolean isWork() {
        return work;
    }

    public User setWork(boolean work) {
        this.work = work;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getNickname() {
        return nickname;
    }
}
