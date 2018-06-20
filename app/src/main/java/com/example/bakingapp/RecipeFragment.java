package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;


public class RecipeFragment extends Fragment {
  public static final String TAG = Options.XTAG + RecipeFragment.class.getSimpleName();

  public interface OnStepClickListener { void onClick (Step step); }
  private OnStepClickListener mStepClickListener;
  
  
  private RecyclerView mIngredientsRV, mStepsRV;
  private TextView mRecipeNameTV;
  
  private Recipe mRecipe;
  

  public RecipeFragment() {
  }


  @Override
  public void onAttach (Context context) {
    super.onAttach(context);
    Log.d(TAG, "onAttach()");

    // Attempt to get OnStepClickListener interface for parent activity.
    try {
      mStepClickListener = (OnStepClickListener)context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
    }
  }
  
  
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView()");
    
    final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

    mIngredientsRV = rootView.findViewById(R.id.ingredients_rv);
    mStepsRV = rootView.findViewById(R.id.steps_rv);
    mRecipeNameTV = rootView.findViewById(R.id.recipe_name_tv);
    
    return rootView;
  }
  
  
  public void setRecipe (@NonNull Recipe recipe) {
    Log.d(TAG, String.format("setRecipe() id = %d, name = %s", recipe.getId(), recipe.getName()));
    mRecipe = recipe;
    mRecipeNameTV.setText(recipe.getName());
  }


}
