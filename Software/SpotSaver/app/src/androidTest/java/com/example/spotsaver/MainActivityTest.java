package com.example.spotsaver;

import android.content.Intent;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.spotsaver.MainActivity;
import com.example.spotsaver.R;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.recyclerAdapter.ListAdapter;
import com.example.spotsaver.utils.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private AppDatabase database;
    private SpotListDao spotListDao;

    @Before
    public void setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        spotListDao = database.spotListDao();
    }

    @After
    public void cleanup() {
        // Close the database after each test
        database.close();
    }

    @Test
    public void addListTest() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity.class);

        // Perform actions and assertions on the UI
        onView(withId(R.id.addList)).perform(ViewActions.click());


        // Enter list name in the AlertDialog
        onView(withId(R.id.listName)).perform(ViewActions.typeText("List 1"));
        onView(withText(R.string.add)).perform(ViewActions.click());

        // Assert that the list is inserted in the database
        List<SpotList> lists = database.spotListDao().getAll();
        Log.d("List", String.valueOf(lists.size()));
        assertNotNull(lists);
        assertEquals(1, lists.size());
        assertEquals("List 1", lists.get(0).name);
    }

    @Test
    public void onBackPressedTest() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity.class);

        // Perform actions and assertions on the UI
        onView(withId(R.id.addList)).check(matches(isDisplayed()));

        // Simulate back button press
        Espresso.pressBackUnconditionally();

        // Assert that MainActivity is launched again
        onView(withId(R.id.addList)).check(matches(isDisplayed()));
    }
}
