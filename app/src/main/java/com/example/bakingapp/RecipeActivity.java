package com.example.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;
import com.example.bakingapp.widget.IngredientsWidget;

import java.util.List;


public class RecipeActivity extends AppCompatActivity
    implements RecipeFragment.OnStepClickListener, StepFragment.OnStepNavigationListener {
  public static final String TAG = Options.XTAG + RecipeActivity.class.getSimpleName();
  
  // Saved activity state keys.
  private static final String KEY_CURRENT_STEP = "cur_step";
  
  public static final String EXTRA_RECIPE = "recipe";
  
  private RecipeFragment mRecipeFragment;
  private StepFragment mStepFragment;
  
  private Recipe mRecipe;
  private boolean mIsTwoPane;
  private int mCurrentStep = 0;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    
    // Get fragments references.
    FragmentManager fm = getSupportFragmentManager();
    mRecipeFragment = (RecipeFragment)fm.findFragmentById(R.id.recipe_fragment);
    mStepFragment = (StepFragment)fm.findFragmentById(R.id.step_fragment);
    
    // Detect if we in two-pane (tablet master/detail) or single-pane (phone recipe-only) mode.
    mIsTwoPane = (mStepFragment != null);
    
    // Get recipe that we will work with.
    mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
    if (mRecipe == null) {
      Log.e(TAG, "Started without recipe extra");
      finish(); return;
    }
    Log.d(TAG, "Started with recipe: " + mRecipe.getName());
    setTitle(mRecipe.getName());
    
    // Restore previous state.
    if (savedInstanceState != null) {
      mCurrentStep = savedInstanceState.getInt(KEY_CURRENT_STEP, 0);
    }
    
    // Pass recipe to the corresponding fragment.
    mRecipeFragment.setRecipe(mRecipe);
    
    // Pass current step to the corresponding fragment.
    if (mIsTwoPane) setFragmentStep();
    
    // Update widget's recipe with current one.
    final AppWidgetManager manager = AppWidgetManager.getInstance(this);
    int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
    Log.d(TAG, "onCreate() updating widgets in manager " + manager + " and IDs count " + ((appWidgetIds == null) ? -1 : appWidgetIds.length));
    IngredientsWidget.updateAllWidgets(this, manager, appWidgetIds, mRecipe);
    manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
  }
  
  
  /**
   * This called when user clicks step in ths steps list.
   * *) In case of Master-Detail activity we should select new step in StepFragment.
   * *) In case of Recipe-only activity we should open Step in separate activity.
   * @param stepNum should be inside mRecipe.
   */
  @Override
  public void onStepClick (int stepNum) {
    if (mIsTwoPane) {
      // Step in fragment.
      mCurrentStep = stepNum;
      setFragmentStep();
    } else {
      // Separate activity.
      Log.d(TAG, String.format("onStepClick(%d) - put %d steps", stepNum, mRecipe.getSteps().size()));
      Intent intent = new Intent(this, StepActivity.class);
      intent.putParcelableArrayListExtra(StepActivity.EXTRA_STEPS, mRecipe.getSteps());
      intent.putExtra(StepActivity.EXTRA_CURRENT_STEP, stepNum);
      startActivity(intent);
    }
  }
  
  
  /**
   * Set current step to the StepFragment.
   */
  private void setFragmentStep() {
    if (mRecipe == null) return;
    List<Step> steps = mRecipe.getSteps();
    if (steps == null) return;
    mStepFragment.setStep(steps.get(mCurrentStep), Utils.getListPositionFlags(steps, mCurrentStep));
  }
  
  
  /**
   * This called when user clicks "Previous Step" button in StepFragment.
   * Normally those buttons are absent for master/detail tablet mode, but I leave this code here
   * just in case I change my mind and add buttons back to layout.
   */
  @Override
  public void onClickPrev() {
    Log.d(TAG, "onClickPrev");
    if (mCurrentStep > 0) mCurrentStep--;
    setFragmentStep();
  }
  
  
  /**
   * This called when user clicks "Next Step" button in StepFragment.
   * Normally those buttons are absent for master/detail tablet mode, but I leave this code here
   * just in case I change my mind and add buttons back to layout.
   */
  @Override
  public void onClickNext() {
    Log.d(TAG, "onClickNext");
    if (mCurrentStep < (mRecipe.getSteps().size() - 1)) mCurrentStep++;
    setFragmentStep();
  }
}
