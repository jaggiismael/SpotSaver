package com.example.spotsaver.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = SpotList.class,
        parentColumns = "iid",
        childColumns = "listId",
        onDelete = ForeignKey.CASCADE)
})
public class Spot {

    public Spot(String name, String description, int lid) {
        this.name = name;
        this.description = description;
        this.lid = lid;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public
    String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "listId")
    public int lid;
}
