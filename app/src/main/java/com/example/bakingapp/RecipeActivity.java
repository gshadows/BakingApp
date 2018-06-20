package com.example.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;


public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnStepClickListener {
  public static final String TAG = Options.XTAG + RecipeActivity.class.getSimpleName();
  
  // Saved activity state keys.
  private static final String KEY_CURRENT_STEP = "cur_step";

  public static final String EXTRA_RECIPE = "recipe";

  private RecipeFragment mRecipeFragment;
  private StepFragment mStepFragment;

  private boolean mIsTwoPane;
  private int mCurrentStep = 0;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    Log.d(TAG, "onCreate()");
    
    FragmentManager fm = getSupportFragmentManager();
    mRecipeFragment = (RecipeFragment)fm.findFragmentById(R.id.recipe_fragment);
    mStepFragment = (StepFragment)fm.findFragmentById(R.id.step_fragment);
    
    mIsTwoPane = (mStepFragment != null);
    
    // Get recipe that we will work with.
    Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
    if (recipe == null) {
      Log.e(TAG, "Started without bundle");
      finish(); return;
    }
    
    // Restore previous state.
    if (savedInstanceState != null) {
      mCurrentStep = savedInstanceState.getInt(KEY_CURRENT_STEP, 0);
    }
    
    // Pass recipe to the corresponding fragment.
    mRecipeFragment.setRecipe(recipe);
    
    // Pass current step to the corresponding fragment.
    if (mIsTwoPane) {
      mStepFragment.setStep(recipe.getSteps().get(mCurrentStep));
    }
    
    // TODO: Update widget's recipe with current one.
  }
  
  
  @Override
  public void onClick (Step step) {
    Log.d(TAG, String.format("onClick() step id %d, desc %s", step.getId(), step.getShortDescription()));
    // TODO: Either run new step activity or update step fragment.
  }
}
