package com.example.spotsaver.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface SpotListDao {

    @Query("SELECT * FROM spotList")
    List<SpotList> getAll();

    @Query("SELECT * FROM spotList WHERE iid IN (:userIds)")
    List<SpotList> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(SpotList... spotList);

    @Delete
    void delete(SpotList spotList);
}
