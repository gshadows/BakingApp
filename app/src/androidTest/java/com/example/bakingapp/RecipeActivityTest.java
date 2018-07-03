package com.example.bakingapp;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.bakingapp.adapters.StepsAdapter;
import com.example.bakingapp.data.Ingredient;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
  
  private static final String MOCK_RECIPE_NAME = "Mock Recipe";
  private static final String MOCK_STEP_NAME   = "Mock Step";
  private static final String MOCK_STEP_DESC   = "Mock Step Description";
  private static final String MOCK_INGREDIENT  = "Mock Ingredient";
  
  private class MockIngredient extends Ingredient { public MockIngredient() {
    quantity = 1.5f;
    measure = "FAKE";
    ingredient = MOCK_INGREDIENT;
  }}
  
  private class MockStep extends Step { public MockStep() {
    id = -1;
    shortDescription = MOCK_STEP_NAME;
    description = MOCK_STEP_DESC;
    videoURL = null;
    thumbnailURL = null;
  }}
  
  private class MockRecipe extends Recipe { public MockRecipe() {
    id = -1;
    name = MOCK_RECIPE_NAME;
    ingredients = new ArrayList<>();
    ingredients.add(new MockIngredient());
    steps = new ArrayList<>();
    steps.add(new MockStep());
    servings = -1;
    image = null;
  }}
  
  
  @Rule public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
    @Override protected Intent getActivityIntent() {
      Intent intent = new Intent();
      intent.putExtra(RecipeActivity.EXTRA_RECIPE, new MockRecipe());
      return intent;
    }
  };
  
  
  @Test public void clickStep_CheckCorrectStepOpened() {
  
    // Prepare: obtain recipes count and select random recipe for test open.
    StepsAdapter adapter = (StepsAdapter)((RecyclerView)mActivityTestRule.getActivity().findViewById(R.id.steps_rv)).getAdapter();
    int count = adapter.getItemCount();
    int idx = new Random().nextInt(count);
    Step step= adapter.getStep(idx);
    if (step == null) throw new IndexOutOfBoundsException("Step not exists in adapter: " + idx);
    
    // Click on the step. StepActivity should be opened or step selected in fragment.
    onView(withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    
    // Check if we're not using landscape on the phone.
    Configuration config = mActivityTestRule.getActivity().getResources().getConfiguration();
    boolean isLandscape = (config.orientation == Configuration.ORIENTATION_LANDSCAPE);
    int smallestSideDP = Math.min(config.screenWidthDp, config.screenHeightDp);
    boolean isTablet = (smallestSideDP >= 600);
    //if (true) throw new RuntimeException("Width = " + config.screenWidthDp + ", Height = " + config.screenHeightDp);
    if (!isTablet && isLandscape) {
      throw new RuntimeException("Could not test on phone in landscape! No views except Exoplayer! Please rotate and try again! ");
    }
  
    // Check if step activity opened or already visible - ExoPlayer should exist.
    onView(withId(R.id.exoplayer)).check(matches(anything()));
  
    // Check if correct step opened using description (because some layouts have no title).
    onView(withId(R.id.step_description)).check(matches(withText(step.getDescription())));
  }
  
  
}
