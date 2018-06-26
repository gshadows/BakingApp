package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.data.Ingredient;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;

import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecipeHolder> {
  public static final String TAG = Options.XTAG + IngredientsAdapter.class.getSimpleName();
  
  private Context mContext;
  private List<Ingredient> mIngredients;
  
  
  public IngredientsAdapter (Context context) {
    mContext = context;
  }
  
  
  @NonNull
  @Override
  public RecipeHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
    return new RecipeHolder(view);
  }
  
  
  @Override
  public void onBindViewHolder (@NonNull RecipeHolder holder, int position) {
    
    if ((mIngredients == null) || (mIngredients.size() < position)) return;
    final Ingredient ingredient = mIngredients.get(position);
    
    // Prepare measurement string.
    String measure = Utils.makeHumanReadableMeasureString(mContext, ingredient.getMeasure());
    Log.d(TAG, "Measure " + ingredient.getMeasure() + " -> " + measure);
    
    // Ensure 1st character in upper case.
    String ingName = ingredient.getIngredient();
    if (Character.isLowerCase(ingName.charAt(0))) {
      char[] chars = ingName.toCharArray();
      chars[0] = Character.toUpperCase(chars[0]);
      ingName = String.valueOf(chars);
    }
    
    String text = String.format("\u2022 %s: %.2f %s", ingName, ingredient.getQuantity(), measure);
    holder.mTextTV.setText(text);
  }


  @Override
  public int getItemCount() {
    return (mIngredients != null) ? mIngredients.size() : 0;
  }
  
  
  public Ingredient getIngredient (int id) { return ((mIngredients != null) && (id < mIngredients.size())) ? mIngredients.get(id) : null; }
  
  
  public void setIngredients (List<Ingredient> ingredients) {
    mIngredients = ingredients;
    notifyDataSetChanged();
  }
  
  
  public class RecipeHolder extends RecyclerView.ViewHolder {
    
    public TextView   mTextTV;
    
    public RecipeHolder (View itemView) {
      super(itemView);
      mTextTV = itemView.findViewById(android.R.id.text1);
    }
  }
}
