package com.example.android.bakingbuddy;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BakingBuddyInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class,true,
                    true);

    @Test
    public void testNavigationUntilDetail(){
//         Click Main Activity item and check next screen
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.rv_ingredients))
                .check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps))
                .check(matches(isDisplayed()));

//        Click step detail item and check next screen
        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.player_step_video))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.tv_step_description))
                .check(matches(isCompletelyDisplayed()));

//        Click on Next to go to next step and check if previous button is shown
        onView(withId(R.id.bt_next_step))
                .perform(click());
        onView(withId(R.id.bt_previous_step))
                .check(matches(isClickable()));

    }
}
