package com.example.spotsaver;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotDao;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.utils.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {

    private AppDatabase database;
    private SpotDao spotDao;
    private SpotListDao spotListDao;

    @Before
    public void createDatabase() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        spotDao = database.spotDao();
        spotListDao = database.spotListDao();
    }

    @After
    public void closeDatabase() {
        database.close();
    }

    @Test
    public void insertAndRead() {
        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();
        assertNotNull(spotLists);
        assertEquals(1, spotLists.size());

        SpotList retrievedSpotList = spotLists.get(0);
        assertEquals("List 1", retrievedSpotList.name);

        Spot spot = new Spot("Spot 1", "Description 1", 52.526853, 13.558792, retrievedSpotList.lid);
        spotDao.insertAll(spot);

        List<Spot> spots = spotDao.getAll();
        assertNotNull(spots);
        assertEquals(1, spots.size());

        Spot retrievedSpot = spots.get(0);
        assertEquals("Spot 1", retrievedSpot.name);
        assertEquals("Description 1", retrievedSpot.description);
        assertEquals(52.526853, retrievedSpot.latitude, 0.0001);
        assertEquals(13.558792, retrievedSpot.longitude, 0.0001);
        assertEquals(1, retrievedSpot.lid);
    }

    @Test
    public void insertAndDelete() {
        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();
        assertNotNull(spotLists);
        assertEquals(1, spotLists.size());

        SpotList retrievedSpotList = spotLists.get(0);
        assertEquals("List 1", retrievedSpotList.name);

        Spot spot = new Spot("Spot 1", "Description 1", 52.526853, 13.558792, retrievedSpotList.lid);
        spotDao.insertAll(spot);

        List<Spot> spots = spotDao.getAll();
        assertNotNull(spots);
        assertEquals(1, spots.size());

        Spot retrievedSpot = spots.get(0);
        assertEquals("Spot 1", retrievedSpot.name);

        spotDao.delete(retrievedSpot);

        spots = spotDao.getAll();
        assertNotNull(spots);
        assertEquals(0, spots.size());

        spotListDao.delete(retrievedSpotList);

        spotLists = spotListDao.getAll();
        assertNotNull(spotLists);
        assertEquals(0, spotLists.size());
    }

    @Test
    public void insertAndUpdate() {
        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();
        assertNotNull(spotLists);
        assertEquals(1, spotLists.size());

        SpotList retrievedSpotList = spotLists.get(0);
        assertEquals("List 1", retrievedSpotList.name);

        retrievedSpotList.name = "Updated List";
        spotListDao.update(retrievedSpotList);

        SpotList updatedSpotList = spotListDao.getListById(retrievedSpotList.lid);
        assertNotNull(updatedSpotList);
        assertEquals("Updated List", updatedSpotList.name);

        Spot spot = new Spot("Spot 1", "Description 1", 52.526853, 13.558792, retrievedSpotList.lid);
        spotDao.insertAll(spot);

        List<Spot> spots = spotDao.getAll();
        assertNotNull(spots);
        assertEquals(1, spots.size());

        Spot retrievedSpot = spots.get(0);
        assertEquals("Spot 1", retrievedSpot.name);

        retrievedSpot.name = "Updated Spot";
        spotDao.update(retrievedSpot);

        Spot updatedSpot = spotDao.getById(retrievedSpot.id);
        assertNotNull(updatedSpot);
        assertEquals("Updated Spot", updatedSpot.name);
    }
}
