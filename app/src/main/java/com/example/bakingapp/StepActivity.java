package com.example.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;


public class StepActivity extends AppCompatActivity {
  public static final String TAG = Options.XTAG + StepActivity.class.getSimpleName();

  public static final String EXTRA_STEP = "step";
  
  private Fragment mStepFragment;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_step);
    
    Step step = getIntent().getParcelableExtra(EXTRA_STEP);
    if (step == null) {
      Log.e(TAG, "Started without bundle");
      finish(); return;
    }
    
    // TODO: Pass step to the fragment with id step_fragment.
  }
}
