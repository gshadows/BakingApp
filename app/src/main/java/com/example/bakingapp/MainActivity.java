package com.example.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.data.BakingApiBuilder;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.utils.Options;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnClickListener {
  public static final String TAG = Options.XTAG + MainActivity.class.getSimpleName();
  
  public static final String SAVED_KEY_POSITION = "pos";


  private Toast mToast = null;
  private RecyclerView mRecyclerView;
  private RecipesAdapter mAdapter;
  
  private BakingApiBuilder.BakingApi mBakingAPI;
  private Call<List<Recipe>> mRecipesDownloadCall;
  
  private int mSavedPosition = NO_POSITION;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mRecyclerView = findViewById(R.id.main_rv);
    mAdapter = new RecipesAdapter(this, this);
    mRecyclerView.setAdapter(mAdapter);
    
    if (savedInstanceState != null) {
      // Restore activity state.
      mSavedPosition = savedInstanceState.getInt(SAVED_KEY_POSITION, NO_POSITION); // Remember until load completes.
    }
  }
  
  
  private void requestRecipes() {
    if (mBakingAPI == null) mBakingAPI = BakingApiBuilder.getBakingApi();
    if (mRecipesDownloadCall != null) mRecipesDownloadCall.cancel();
    mRecipesDownloadCall = BakingApiBuilder.getBakingApi().getRecipes();

    mRecipesDownloadCall.enqueue(new Callback<List<Recipe>>() {
      @Override
      public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
          Log.d(TAG, String.format("onResponse(): received %d recipes", response.body().size()));
          if (mAdapter != null) mAdapter.setRecipes(response.body());
          if (mSavedPosition != NO_POSITION) mRecyclerView.scrollToPosition(mSavedPosition);
        } else {
          Log.w(TAG,String.format("onResponse() failed: %d %s", response.code(), response.message()));
          showToast(getString(R.string.bad_server_response, response.code()));
        }
      }

      @Override
      public void onFailure(Call<List<Recipe>> call, Throwable error) {
        showToast(getString(R.string.network_error));
        Log.w(TAG, "onFailure(): " + error);
      }
    });
  }


  @Override
  protected void onStart() {
    super.onStart();
    requestRecipes();
  }
  
  
  @Override
  protected void onStop() {
    super.onStop();
    if (mRecipesDownloadCall != null) mRecipesDownloadCall.cancel();
  }
  

  private int getRecyclerViewPosition() {
    GridLayoutManager lm = (GridLayoutManager)mRecyclerView.getLayoutManager();
    int pos = lm.findFirstCompletelyVisibleItemPosition();
    if (pos == NO_POSITION) pos = lm.findFirstVisibleItemPosition(); // If all items partially invisible.
    return pos;
  }


  @Override
  protected void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState (outState);
    
    if ((mAdapter != null) && (mAdapter.getItemCount() >= 2)) {
      // Save RV position only if more then 1 item and if position is not zero (and exists).
      int pos = getRecyclerViewPosition();
      if (pos > 0) outState.putInt(SAVED_KEY_POSITION, pos);
    }
  }
  
  
  private void showToast(@NonNull String text) {
    Log.w(TAG, text);
    if (mToast != null) mToast.cancel();
    mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    mToast.show();
  }
  
  
  @Override
  public void onClick (int itemId) {
    Recipe recipe = mAdapter.getRecipe(itemId);
    if (recipe == null) {
      Log.w(TAG, "No recipe with id " + itemId);
      return;
    }
    
    Intent intent = new Intent(this, RecipeActivity.class);
    intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
    startActivity(intent);
  }
}
