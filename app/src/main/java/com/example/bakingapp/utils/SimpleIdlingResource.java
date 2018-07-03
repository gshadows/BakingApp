package com.example.bakingapp.utils;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;


@VisibleForTesting
public class SimpleIdlingResource implements IdlingResource {
  
  @Nullable private volatile ResourceCallback mCallback;
  private AtomicBoolean mIsIdling = new AtomicBoolean(true);
  
  
  @Override
  public String getName () {
    return getClass().getSimpleName();
  }
  
  
  @Override
  public boolean isIdleNow () {
    return mIsIdling.get();
  }
  
  
  @Override
  public void registerIdleTransitionCallback (ResourceCallback callback) {
    mCallback = callback;
  }
  
  
  public void setIdleState (boolean idle) {
    mIsIdling.set(idle);
    if (idle && (mCallback != null)) mCallback.onTransitionToIdle();
  }
}
