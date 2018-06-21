package com.example.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class StepActivity extends AppCompatActivity implements StepFragment.OnStepNavigationListener  {
  public static final String TAG = Options.XTAG + StepActivity.class.getSimpleName();

  public static final String EXTRA_STEPS        = "steps";
  public static final String EXTRA_CURRENT_STEP = "cur_step";
  
  private StepFragment mStepFragment;
  
  private ArrayList<Step> mSteps;
  private int mCurrentStep;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_step);

    // Get steps list that we will work with.
    mSteps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
    if (mSteps == null) {
      Log.e(TAG, "Started without steps list extra");
      finish(); return;
    }
    
    // Get current step index.
    mCurrentStep = getIntent().getIntExtra(EXTRA_CURRENT_STEP, -1);
    if ((mCurrentStep < 0) || (mCurrentStep >= mSteps.size())) {
      Log.e(TAG, "Started without or with bad step index extra");
      finish(); return;
    }
    
    // Setup fragment.
    mStepFragment = (StepFragment)getSupportFragmentManager().findFragmentById(R.id.step_fragment);
    setFragmentStep();
  }


  /**
   * Set current step to the StepFragment.
   */
  private void setFragmentStep() {
    mStepFragment.setStep(mSteps.get(mCurrentStep), Utils.getListPositionFlags(mSteps, mCurrentStep));
  }


  /**
   * This called when user clicks "Previous Step" button in StepFragment.
   */
  @Override
  public void onClickPrev() {
    if (mCurrentStep > 0) mCurrentStep--;
    setFragmentStep();
  }


  /**
   * This called when user clicks "Next Step" button in StepFragment.
   */
  @Override
  public void onClickNext() {
    if (mCurrentStep < (mSteps.size() - 1)) mCurrentStep++;
    setFragmentStep();
  }
}
