package com.example.bakingapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class StepFragment extends Fragment implements View.OnClickListener, Player.EventListener {
  public static final String TAG = Options.XTAG + StepFragment.class.getSimpleName();
  
  private static final String KEY_START_POSITION = "start_pos";
  
  public interface OnStepNavigationListener {
    void onClickPrev();
    void onClickNext();
  }
  private OnStepNavigationListener mStepNavigationListener;
  
  private PlayerView mPlayerView;
  private TextView mStepTitleTV, mStepDescTV;
  private ImageButton mPrevButton, mNextButton;

  private SimpleExoPlayer mPlayer;
  
  private Step mStep;
  private int mListPositionFlags;
  private long mStartPosition = C.TIME_UNSET;
  
  
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
    final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
    
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
    
    // Restore.
    if (savedInstanceState != null) {
      mStartPosition = savedInstanceState.getLong (KEY_START_POSITION, C.TIME_UNSET);
    }
    
    // Prepare ExoPlayer.
    mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.fallback_image));
    initializePlayer();
    
    return rootView;
  }
  
  
  @Override
  public void onSaveInstanceState (@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mPlayer != null) {
      long pos = mPlayer.getContentPosition();
      if (pos > 0) outState.putLong(KEY_START_POSITION, pos);
    }
  }
  
  
  private void initializePlayer() {
    if (mPlayer != null) return;
    
    RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
    LoadControl loadControl = new DefaultLoadControl();
    TrackSelector trackSelector = new DefaultTrackSelector();
    mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    mPlayerView.setPlayer(mPlayer);
    
    mPlayer.addListener(this);
    mPlayer.setPlayWhenReady(false);
  }
  
  
  private void startPlaying (String mediaURL) {
    if ((mPlayer.getPlaybackState() == Player.STATE_READY) && mPlayer.getPlayWhenReady()) {
      mPlayer.stop();
    }
    
    Context context = getContext();
    String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
    
    MediaSource mediaSource = new ExtractorMediaSource.Factory(
        new DefaultDataSourceFactory(context, userAgent)
    ).createMediaSource(Uri.parse(mediaURL));
  
    //initMediaSession();
    
    mPlayer.prepare(mediaSource);
    if (mStartPosition > 0) mPlayer.seekTo(mStartPosition);
    mPlayer.setPlayWhenReady(true);
    mPlayerView.hideController(); // Do not show controls on playback start,
  }
  
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    
    // Stop and release ExoPlayer.
    if (mPlayer != null) {
      mPlayer.stop();
      mPlayer.release();
      mPlayer = null;
    }
  }
  
  
  /**
   * Called by the activity to set current step data and flags.
   * @param step Current step data to display in fragment.
   * @param listPositionFlags Current step position flags in step list: is this FIRST and/or LAST step?
   */
  public void setStep (Step step, int listPositionFlags) {
    Log.d(TAG, String.format("setStep() id = %d, desc = %s, flags = ", step.getId(), step.getShortDescription(), listPositionFlags));
    mStep = step;
    mListPositionFlags = listPositionFlags;
    
    // Set text fields.
    if (mStepTitleTV != null) mStepTitleTV.setText(step.getShortDescription());
    if (mStepDescTV  != null) mStepDescTV.setText (step.getDescription());
    
    // Ensure only applicable buttons active.
    if (mPrevButton != null) mPrevButton.setEnabled((listPositionFlags & Utils.LIST_POSITION_FIRST) != 0);
    if (mNextButton != null) mNextButton.setEnabled((listPositionFlags & Utils.LIST_POSITION_LAST)  != 0);
    
    // Get media (video or audio) URL and check it.
    String media = step.getVideoURL();
    String other = null; // Fallback (probably image). I assume ExoPlayer is able to load images.
    if (media != null) {
      if (media.isEmpty()) {
        // No video URL.
        media = null;
      } else {
        // We have some video URL. Save it as fallback.
        other = media;
        if (Utils.probablyImageFile(media)) media = null;
      }
    }
    if (media == null) {
      // Second chance - if it was swapped with thumbnail URL.
      media = step.getThumbnailURL();
      if (media != null) {
        if (media.isEmpty()) {
          // No thumbnail URL.
          media = other;
        } else {
          // We have some thumbnail URL.
          if (Utils.probablyImageFile(media)) media = (other == null) ? media : other; // Between two images prefer "video" image.
        }  
      } else {
        // Fallback to a probably image (or to a null if no any URLs exists).
        media = other;
      }
    }
    
    // Start playing media.
    if (media != null) startPlaying(media);
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
        if ((mListPositionFlags & Utils.LIST_POSITION_FIRST) != 0) mStepNavigationListener.onClickPrev();
        break;
      case R.id.next_step_button:
        if ((mListPositionFlags & Utils.LIST_POSITION_LAST) != 0) mStepNavigationListener.onClickNext();
        break;
    }
  }
  
  
  // Unused ExoPlayer callbacks.
  @Override public void onTimelineChanged (Timeline timeline, Object manifest, int reason) {}
  @Override public void onTracksChanged (TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
  @Override public void onLoadingChanged (boolean isLoading) {}
  @Override public void onPlayerStateChanged (boolean playWhenReady, int playbackState) {}
  @Override public void onRepeatModeChanged (int repeatMode) {}
  @Override public void onShuffleModeEnabledChanged (boolean shuffleModeEnabled) {}
  @Override public void onPlayerError (ExoPlaybackException error) {}
  @Override public void onPositionDiscontinuity (int reason) {}
  @Override public void onPlaybackParametersChanged (PlaybackParameters playbackParameters) {}
  @Override public void onSeekProcessed() {}
  
}
