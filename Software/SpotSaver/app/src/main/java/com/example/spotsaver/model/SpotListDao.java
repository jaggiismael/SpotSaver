package com.example.spotsaver.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface SpotListDao {

    @Query("SELECT * FROM spotList")
    List<SpotList> getAll();

    @Query("SELECT * FROM spotList WHERE iid = :listid")
    SpotList getListById(int listid);

    @Insert
    void insertAll(SpotList... spotList);

    @Update
    void update(SpotList spotList);

    @Delete
    void delete(SpotList spotList);

    @Query("DELETE FROM spotList WHERE iid = :listid")
    void deleteListById(int listid);
}
