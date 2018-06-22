package com.example.bakingapp;

import android.content.Context;
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
import com.example.bakingapp.utils.Utils;
import com.google.android.exoplayer2.ui.PlayerView;


public class StepFragment extends Fragment implements View.OnClickListener {
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
  private int mListPosition;
  

  public StepFragment() {} // Unused constructor.


  @Override
  public void onAttach (Context context) {
    super.onAttach(context);
    
    // Attempt to get OnStepClickListener interface for parent activity.
    try {
      mStepNavigationListener = (OnStepNavigationListener)context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement OnStepNavigationListener");
    }
  }


  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
    
    // Get all views.
    mPlayerView = rootView.findViewById(R.id.exoplayer);
    mStepTitleTV = rootView.findViewById(R.id.step_title);
    mStepDescTV = rootView.findViewById(R.id.step_description);
    mPrevButton = rootView.findViewById(R.id.prev_step_button);
    mNextButton = rootView.findViewById(R.id.next_step_button);
    
    // Set inter-step navigation buttons onClick listeners and disable.
    if (mPrevButton != null) {
      mPrevButton.setOnClickListener(this);
      mPrevButton.setEnabled(false);
    }
    if (mNextButton != null) {
      mNextButton.setOnClickListener(this);
      mNextButton.setEnabled(false);
    }

    return rootView;
  }


  /**
   * Called by the activity to set current step data and flags.
   * @param step Current step data to display in fragment.
   * @param listPositionFlags Current step position flags in step list: is this FIRST and/or LAST step?
   */
  public void setStep (Step step, int listPositionFlags) {
    Log.d(TAG, String.format("setStep() id = %d, desc = %s, flags = ", step.getId(), step.getShortDescription(), listPositionFlags));
    mStep = step;
    mListPosition = listPositionFlags;
    
    // Set text fields.
    if (mStepTitleTV != null) mStepTitleTV.setText(step.getShortDescription());
    if (mStepDescTV  != null) mStepDescTV.setText (step.getDescription());
    
    // Ensure only applicable buttons active.
    if (mPrevButton != null) mPrevButton.setEnabled((listPositionFlags & Utils.LIST_POSITION_FIRST) != 0);
    if (mNextButton != null) mNextButton.setEnabled((listPositionFlags & Utils.LIST_POSITION_LAST)  != 0);
  }


  /**
   * Navigatio buttons (Next Step, Previous Step) click.
   * @param v clicked view.
   */
  @Override
  public void onClick (View v) {
    if (mStepNavigationListener == null) return;
    switch (v.getId()) {
      case R.id.prev_step_button:
        if ((mListPosition & Utils.LIST_POSITION_FIRST) != 0) mStepNavigationListener.onClickPrev();
        break;
      case R.id.next_step_button:
        if ((mListPosition & Utils.LIST_POSITION_LAST) != 0) mStepNavigationListener.onClickNext();
        break;
    }
  }


}
