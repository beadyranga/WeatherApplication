package com.rawtechworld.weatherapplication.activities;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.rawtechworld.wheatherapplication.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);


        @Rule
        public ActivityScenarioRule<MainActivity> activityRule
                = new ActivityScenarioRule<>(MainActivity.class);

        @Before
        public void initValidString() {
        }

        @Test
        public void whenButtonIsClickedTheUseCaseTextIsShown() {
            // Type text and then press the button.
            onView(withId(R.id.fetch_whether_update)).perform(click());

            onView(withId(R.id.fetch_whether_update)).perform((click()));

            onView(withId(R.id.fetch_whether_update)).perform((click()));
            onView(withId(R.id.timeZone)).check(ViewAssertions.matches(withText(containsString("Time"))));
        }

}