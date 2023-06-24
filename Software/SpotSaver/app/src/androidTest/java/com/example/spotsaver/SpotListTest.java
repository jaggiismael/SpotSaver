package com.example.spotsaver;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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
public class SpotListTest {

    private AppDatabase database;
    private SpotDao spotDao;
    private SpotListDao spotListDao;
    private SpotList retrievedSpotList;

    @Rule
    public ActivityTestRule<SpotListActivity> activityRule
            = new ActivityTestRule<>(SpotListActivity.class, false, false);


    @Before
    public void createDatabase() {
        // Create a database for testings
        database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        spotDao = database.spotDao();
        spotListDao = database.spotListDao();
    }

    @After
    public void cleanup() {
        // Clear and close the database after each test
        database.clearAllTables();
        database.close();
    }

    @Test
    public void renameListTest() {
        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();

        retrievedSpotList = spotLists.get(0);

        String newName = "List 3";

        // Launch the activity with the retrieved SpotList object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SpotListActivity.class);
        intent.putExtra("key", retrievedSpotList.lid);
        activityRule.launchActivity(intent);

        Espresso.onView(ViewMatchers.withId(R.id.edit)).perform(ViewActions.click());

        // Verify that the edit dialog is shown
        Espresso.onView(ViewMatchers.withText(R.string.renameList))
                .check(matches(ViewMatchers.isDisplayed()));

        // Enter list name in the AlertDialog
        onView(withId(R.id.listName)).perform(ViewActions.typeText(newName));
        onView(withText(R.string.rename)).perform(ViewActions.click());

        // Verify that the list name is updated in the activity
        Espresso.onView(ViewMatchers.withId(R.id.tTextview)).check(matches(withText(newName)));
    }

    @Test
    public void deleteListTest() {

        SpotList spotList = new SpotList("List 1");
        spotListDao.insertAll(spotList);

        List<SpotList> spotLists = spotListDao.getAll();

        retrievedSpotList = spotLists.get(0);

        // Launch the activity with the retrieved SpotList object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SpotListActivity.class);
        intent.putExtra("key", retrievedSpotList.lid);
        activityRule.launchActivity(intent);

        // Click on the delete ImageView
        Espresso.onView(ViewMatchers.withId(R.id.delete)).perform(ViewActions.click());

        // Verify that the delete dialog is shown
        Espresso.onView(ViewMatchers.withText(R.string.deleteList))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on delete button in dialog field
        onView(withText(R.string.delete)).perform(ViewActions.click());

        onView(withId(R.id.addList)).check(matches(isDisplayed()));
    }
}
