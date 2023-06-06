package com.example.spotsaver.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "spotList")
public class SpotList {

    @PrimaryKey(autoGenerate = true)
    public int iid;

    @ColumnInfo(name = "name")
    public
    String name;

    public SpotList(String name) {
        this.name = name;
    }
}
