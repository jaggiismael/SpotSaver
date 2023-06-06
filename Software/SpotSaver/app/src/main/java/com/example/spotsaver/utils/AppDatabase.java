package com.example.spotsaver.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotDao;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;


@Database(entities = {SpotList.class, Spot.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SpotListDao spotListDao();
    public abstract SpotDao spotDao();
}
