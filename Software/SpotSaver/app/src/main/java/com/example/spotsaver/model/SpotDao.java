package com.example.spotsaver.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SpotDao {
    @Query("SELECT * FROM spot")
    List<Spot> getAll();

    @Query("SELECT * FROM spot WHERE id = :spotId")
    Spot getById(int spotId);

    @Query("SELECT * FROM spot WHERE listId = :listId")
    List<Spot> getSpotsByListId(int listId);

    @Insert
    void insert(Spot spot);

    @Insert
    void insertAll(Spot... spots);

    @Update
    void update(Spot spot);

    @Delete
    void delete(Spot spot);

    @Query("Delete FROM spot WHERE id = :spotId")
    void delete(int spotId);

    @Query("DELETE FROM spot")
    void deleteAll();
}
