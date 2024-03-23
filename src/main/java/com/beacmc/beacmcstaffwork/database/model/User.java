package com.beacmc.beacmcstaffwork.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(columnName = "nickname", canBeNull = false, id = true)
    public String nickname;

    @DatabaseField(columnName = "password")
    public String password;

    @DatabaseField(columnName = "is_work", defaultValue = "false")
    public boolean work;

    @DatabaseField(columnName = "work_start", defaultValue = "0")
    public long workStart;

    @DatabaseField(columnName = "time", defaultValue = "0")
    public long time;

    @DatabaseField(columnName = "bans", defaultValue = "0")
    public int bans;

    @DatabaseField(columnName = "mutes", defaultValue = "0")
    public int mutes;

    @DatabaseField(columnName = "kicks", defaultValue = "0")
    public int kicks;

    @DatabaseField(columnName = "discord_id", defaultValue = "0", unique = true)
    public long discordID;



    public User() {}

    public User(String nickname, String password, boolean work, long workStart, long time, int bans, int mutes, int kicks, long discordID) {
        setNickname(nickname);
        setPassword(password);
        setWork(work);
        setWorkStart(workStart);
        setTime(time);
        setBans(bans);
        setMutes(mutes);
        setKicks(kicks);
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
