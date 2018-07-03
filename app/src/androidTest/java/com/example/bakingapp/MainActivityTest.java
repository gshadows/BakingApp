package com.example.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.data.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
  
  @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
  
  private IdlingResource mIdlingResource;
  
  
  @Before public void before() {
    mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(mIdlingResource);
    //if (mIdlingResource.isIdleNow()) throw new RuntimeException("Should not be idle...");
  }
  
  
  @After public void unregisterIdlingResource() {
    if (mIdlingResource != null) IdlingRegistry.getInstance().unregister(mIdlingResource);
  }
  
  
  /**
   * HELPER METHOD.
   * Check if current activity's title (toolbar text) is what we expected to see.
   * @param expectedTitle Expected activity title.
   */
  private void testActivityTitle (String expectedTitle) {
    onView(allOf(
        isAssignableFrom(TextView.class),
        withParent(isAssignableFrom(Toolbar.class))
    )).check(matches(withText(expectedTitle)));
  }
  
  
  /**
   * HELPER METHOD.
   * Wait for activity becomes idle (finish loading resources).
   */
  private void waitForIdle() {
    onView(isRoot()).check(matches(anything()));
    if (!mIdlingResource.isIdleNow()) throw new RuntimeException("Should be idle...");
  }
  
  
  /**
   * 1. Wait for recipes loaded and added to RecyclerView.
   * 2. Click on a random recipe.
   * 3. Check that RecipeActivity opened with correct recipe displayed.
   */
  @Test public void clickRecipe_CheckCorrectRecipeOpened() {
    
    // Wait for finish recipes loading and becomes idle.
    waitForIdle();
    
    // Prepare: obtain recipes count and select random recipe for test open.
    RecipesAdapter adapter = (RecipesAdapter)((RecyclerView)mActivityTestRule.getActivity().findViewById(R.id.main_rv)).getAdapter();
    int count = adapter.getItemCount();
    int idx = new Random().nextInt(count);
    Recipe recipe = adapter.getRecipe(idx);
    if (recipe == null) throw new IndexOutOfBoundsException("Recipe not exists in adapter: " + idx);
    
    // Ensure start-up activity title is default one.
    // This is generally a test of testActivityTitle() itself to ensure it works before real use.
    testActivityTitle(mActivityTestRule.getActivity().getString(R.string.app_name));
    
    // Click on recipe. RecipeActivity should be opened.
    onView(withId(R.id.main_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(idx, click()));
    
    // Check if activity title changed to recipe name (because recipe activity opened).
    testActivityTitle(recipe.getName());
  }
  
  
}
