package com.example.spotsaver;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotDao;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.utils.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private AppDatabase database;
    private SpotListDao spotListDao;
    private SpotDao spotDao;
    private SpotList retrievedSpotList;
    private Spot retrievedSpot;

    @Rule
    public ActivityTestRule<DetailSpotActivity> activityRule
            = new ActivityTestRule<>(DetailSpotActivity.class, false, false);

    @Before
    public void setup() {
        // Create a database for testing
        database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        spotListDao = database.spotListDao();
        spotDao = database.spotDao();

        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();

        retrievedSpotList = spotLists.get(0);

        Spot spot = new Spot("Eine Bar", "Eine richtig gute Bar", 52.536769, 13.555526, retrievedSpotList.lid);
        spotDao.insertAll(spot);

        List<Spot> spots = spotDao.getAll();

        retrievedSpot = spots.get(0);

        // Launch the activity with the retrieved SpotList object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DetailSpotActivity.class);
        intent.putExtra("key", retrievedSpot.id);
        activityRule.launchActivity(intent);
    }

    @After
    public void cleanup() {
        // Clear and close the database after each test
        database.clearAllTables();
        database.close();
    }

    @Test
    public void deleteSpotTest() {

        // Click on the delete ImageView
        Espresso.onView(ViewMatchers.withId(R.id.delete)).perform(ViewActions.click());

        // Verify that the delete dialog is shown
        Espresso.onView(ViewMatchers.withText(R.string.deleteSpot))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on delete button in dialog field
        onView(withText(R.string.delete)).perform(ViewActions.click());

        onView(withText(retrievedSpotList.name)).check(matches(isDisplayed()));
    }
}
