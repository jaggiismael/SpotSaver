package com.example.spotsaver;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.utils.AppDatabase;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateSpotActivityTest {

    private AppDatabase database;
    private SpotListDao spotListDao;
    private SpotList retrievedSpotList;

    @Rule
    public ActivityTestRule<CreateSpotActivity> activityRule
            = new ActivityTestRule<>(CreateSpotActivity.class, false, false);

    @Before
    public void setup() {
        // Create an in-memory database for testing
        database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        spotListDao = database.spotListDao();

        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();

        retrievedSpotList = spotLists.get(0);

        // Launch the activity with the retrieved SpotList object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreateSpotActivity.class);
        intent.putExtra("key", retrievedSpotList.lid);
        activityRule.launchActivity(intent);
    }

    @After
    public void cleanup() {
        // Clear and close the database after each test
        database.clearAllTables();
        database.close();
    }

    @Test
    public void createNewSpot() {
        // Enter spot name
        onView(withId(R.id.name)).perform(ViewActions.typeText("New Spot"));

        // Enter spot description
        onView(withId(R.id.description)).perform(ViewActions.typeText("Spot description"));

        //Hide the keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());


        // Simulate map interaction (e.g., click at a specific location)
        onView(withId(R.id.mapView)).perform(ViewActions.click());

        // Click the "Add Spot" button
        onView(withId(R.id.addSpot)).perform(ViewActions.click());

        // Verify that the SpotListActivity is launched
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));

        // You can add additional assertions to verify the created spot
        // For example, you can check if the spot name is displayed in the list
        onView(withText("New Spot")).check(matches(isDisplayed()));
    }
}
