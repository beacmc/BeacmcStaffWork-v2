package com.beacmc.beacmcstaffwork.database.model;

import com.beacmc.beacmcstaffwork.warn.WarnType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "warns")
public class Warn {

    @DatabaseField(columnName = "id", id = true, generatedId = true)
    private Integer id;

    @DatabaseField(columnName = "author", canBeNull = false)
    private String author;

    @DatabaseField(columnName = "player", canBeNull = false)
    private String player;

    @DatabaseField(columnName = "type", dataType = DataType.ENUM_NAME, unknownEnumName = "VERBAL")
    private WarnType type;

    @DatabaseField(columnName = "reason")
    private String reason;

    @DatabaseField(columnName = "date")
    private String date;

    public Warn() {}

    public Warn(String author, String player, WarnType type, String reason, String date) {
        this.author = author;
        this.player = player;
        this.type = type;
        this.reason = reason;
        this.date = date;
    }

    public Warn setId(int id) {
        this.id = id;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public Warn setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Warn setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Warn setPlayer(String player) {
        this.player = player;
        return this;
    }

    public Warn setDate(String date) {
        this.date = date;
        return this;
    }

    public Warn setType(WarnType type) {
        this.type = type;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getPlayer() {
        return player;
    }

    public WarnType getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
