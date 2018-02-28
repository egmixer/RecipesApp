package com.example.recipesapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.recipesapp.activities.StepsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class StepsActivityTest {
    @Rule
    public ActivityTestRule<StepsActivity> activityActivityTestRule =
            new ActivityTestRule<>(StepsActivity.class, true, false);

    @Test
    public void checkTextViews() {
        onView(allOf(withId(R.id.ingred_tv), withText(R.string.ingredients), isDisplayed()));
        onView(allOf(withId(R.id.stps_tv), withText(R.string.steps), isDisplayed()));
    }

    @Test
    public void checkRecyclerViews() {
        onView(allOf(withId(R.id.ingred_rv), isDisplayed()));
        onView(allOf(withId(R.id.stps_rv), isDisplayed()));
    }
}
