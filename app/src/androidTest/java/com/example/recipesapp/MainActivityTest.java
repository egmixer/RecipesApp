package com.example.recipesapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.recipesapp.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void idlingResourceTest() {
        onView(ViewMatchers.withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

}
