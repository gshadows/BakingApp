package com.example.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;
import com.google.android.exoplayer2.ui.PlayerView;


public class StepFragment extends Fragment {
  public static final String TAG = Options.XTAG + StepFragment.class.getSimpleName();

  public interface OnStepNavigationListener {
    void onClickPrev();
    void onClickNext();
  }
  private OnStepNavigationListener mStepNavigationListener;

  private PlayerView mPlayerView;
  private TextView mStepTitleTV, mStepDescTV;
  private Button mPrevButton, mNextButton;

  private Step mStep;
  

  public StepFragment() {
  }


  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView()");

    final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

    mPlayerView = rootView.findViewById(R.id.exoplayer);
    mStepTitleTV = rootView.findViewById(R.id.step_title);
    mStepDescTV = rootView.findViewById(R.id.step_description);
    mPrevButton = rootView.findViewById(R.id.prev_step_button);
    mNextButton = rootView.findViewById(R.id.next_step_button);

    return rootView;
  }
  
  
  public void setStep (Step step) {
    Log.d(TAG, String.format("setStep() id = %d, desc = %s", step.getId(), step.getShortDescription()));
    mStep = step;
    
    if (mStepTitleTV != null) mStepTitleTV.setText(step.getShortDescription());
    if (mStepDescTV != null) mStepDescTV.setText(step.getDescription());
  }

}
