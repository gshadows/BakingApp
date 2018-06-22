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

import com.example.bakingapp.adapters.IngredientsAdapter;
import com.example.bakingapp.adapters.StepsAdapter;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;


public class RecipeFragment extends Fragment implements StepsAdapter.OnClickListener {
  public static final String TAG = Options.XTAG + RecipeFragment.class.getSimpleName();

  public interface OnStepClickListener { void onStepClick (Step step); }
  private OnStepClickListener mStepClickListener;
  
  
  private RecyclerView mIngredientsRV, mStepsRV;
  private TextView mRecipeNameTV;

  IngredientsAdapter mIngredientsAdapter;
  StepsAdapter mStepsAdapter;
  
  private Recipe mRecipe;
  

  public RecipeFragment() {} // Unused constructor.
  

  @Override
  public void onAttach (Context context) {
    super.onAttach(context);

    // Attempt to get OnStepClickListener interface for parent activity.
    try {
      mStepClickListener = (OnStepClickListener)context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
    }
  }
  
  
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
    final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
    
    // Get all views.
    mIngredientsRV = rootView.findViewById(R.id.ingredients_rv);
    mStepsRV = rootView.findViewById(R.id.steps_rv);
    mRecipeNameTV = rootView.findViewById(R.id.recipe_name_tv);
    
    // Create and set adapters.
    mIngredientsAdapter = new IngredientsAdapter(getContext());
    mIngredientsRV.setAdapter(mIngredientsAdapter);
    mStepsAdapter = new StepsAdapter(getContext(), this);
    mStepsRV.setAdapter(mStepsAdapter);

    return rootView;
  }
  
  
  public void setRecipe (@NonNull Recipe recipe) {
    Log.d(TAG, String.format("setRecipe() id = %d, name = %s", recipe.getId(), recipe.getName()));
    mRecipe = recipe;
    
    // Set title.
    mRecipeNameTV.setText(recipe.getName());
    
    // Put data to the adapters.
    mIngredientsAdapter.setIngredients(recipe.getIngredients());
    mStepsAdapter.setSteps(recipe.getSteps());
  }


  @Override
  public void onClick (int itemId) {
    Step step = mStepsAdapter.getStep(itemId);
    if (step != null) {
      if (mStepClickListener != null) mStepClickListener.onStepClick(step);
    } else {
      Log.w(TAG, "onClick() no such step: " + itemId);
    }
  }

}
