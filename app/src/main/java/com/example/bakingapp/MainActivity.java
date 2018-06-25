package com.example.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.data.BakingApiBuilder;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnClickListener {
  public static final String TAG = Options.XTAG + MainActivity.class.getSimpleName();
  
  public static final String SAVED_KEY_POSITION = "pos";
  public static final String SAVED_KEY_RECIPES  = "recipes";


  private Toast mToast = null;
  private RecyclerView mRecyclerView;
  private RecipesAdapter mAdapter;
  
  private BakingApiBuilder.BakingApi mBakingAPI;
  private Call<ArrayList<Recipe>> mRecipesDownloadCall;
  private boolean mRequestStopped = false;
  
  private int mSavedPosition = NO_POSITION;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mRecyclerView = findViewById(R.id.main_rv);
    mAdapter = new RecipesAdapter(this, this);
    mRecyclerView.setAdapter(mAdapter);

    ArrayList<Recipe> savedRecipes = null;
    if (savedInstanceState != null) {
      // Restore activity state.
      mSavedPosition = savedInstanceState.getInt(SAVED_KEY_POSITION, NO_POSITION); // Remember until load completes.
      savedRecipes = savedInstanceState.getParcelableArrayList(SAVED_KEY_RECIPES);
    }
    
    // Finally request recipes.
    if (savedRecipes == null) {
      Log.d(TAG, "onCreate() Requesting recipes...");
      requestRecipes();
    } else {
      Log.d(TAG, "Restored recipes from saved state!");
      mAdapter.setRecipes(savedRecipes);
    }
  }


  /**
   * Request recipes from network using Retrofit and handle results/errors asynchronously.
   */
  private void requestRecipes() {
    if (mBakingAPI == null) mBakingAPI = BakingApiBuilder.getBakingApi();
    if (mRecipesDownloadCall != null) mRecipesDownloadCall.cancel();
    mRecipesDownloadCall = BakingApiBuilder.getBakingApi().getRecipes();
    Log.d(TAG, "requestRecipes() enqueuing req " + mRecipesDownloadCall.hashCode());

    mRecipesDownloadCall.enqueue(new Callback<ArrayList<Recipe>>() {
      @Override
      public void onResponse (Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
        Log.d(TAG, "onResponse() " + call.hashCode());
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
      public void onFailure (Call<ArrayList<Recipe>> call, Throwable error) {
        Log.d(TAG, "onFailure() " + call.hashCode());
        if (!call.isCanceled()) {
          showToast(getString(R.string.network_error));
          Log.w(TAG, "onFailure(): " + error);
        }
      }
    });
  }


  @Override
  protected void onStart() {
    super.onStart();
    if (mRequestStopped) {
      Log.d(TAG, "onStart() Requesting recipes... (after was stopped)");
      requestRecipes(); // Restart network request after stopped.
    }
  }
  

  @Override
  protected void onStop() {
    super.onStop();
    if (mRecipesDownloadCall != null) {
      Log.d(TAG, "onStop() Stopping recipes request: " + mRecipesDownloadCall.hashCode());
      mRequestStopped = true;
      mRecipesDownloadCall.cancel();
      mRecipesDownloadCall = null;
    }
  }
  
  
  @Override
  protected void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState (outState);
    
    if (mAdapter != null) {
      ArrayList<Recipe> recipes = mAdapter.getRecipes();
      if (recipes != null) {
        
        // Save RV position only if more then 1 item and if position is not zero (and exists).
        if (recipes.size() >= 2) {
          int pos = Utils.getRecyclerViewPosition(mRecyclerView);
          if (pos > 0) outState.putInt(SAVED_KEY_POSITION, pos);
        }

        // Save recipes list if not empty.
        if (recipes.size() > 0) outState.putParcelableArrayList(SAVED_KEY_RECIPES, recipes);
        
      }
    }
  }


  /**
   * Show toast message. Reuse existing toast to prevent multiple messages queued.
   * @param text Message text.
   */
  private void showToast(@NonNull String text) {
    Log.w(TAG, text);
    if (mToast != null) mToast.cancel();
    mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    mToast.show();
  }


  /**
   * This called when user clicks on a recipe. Recipe details should be opened next.
   * @param itemId Unused view ID.
   */
  @Override
  public void onRecipeClick (int itemId) {
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
