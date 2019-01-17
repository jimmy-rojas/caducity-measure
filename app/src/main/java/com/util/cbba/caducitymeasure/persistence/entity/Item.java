package com.util.cbba.caducitymeasure.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "expiration_date")
    private Date expirationDate;

    @NonNull
    @ColumnInfo(name = "resolved")
    private boolean resolved;

    public Item(@NonNull String name, String description, @NonNull Date expirationDate, boolean resolved) {
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.resolved = resolved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NonNull Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
