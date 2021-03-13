package com.dongmyungahn.android.bakingapp;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.allOf;

import com.dongmyungahn.android.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void check_main_activity_title() {
        // Check if action bar title in Main Activity is "BakingApp" (Default)
        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar")))).check(matches(withText("BakingApp")));
    }

    @Test
    public void click_recipe_list() {
        // Click the second recipe in MainActivity's RecyclerView
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // After clicking, check if action bar title in RecipeDetailActivity is changed to the second recipe name (Here, "Brownies"))
        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar")))).check(matches(withText("Brownies")));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }

    }
}
