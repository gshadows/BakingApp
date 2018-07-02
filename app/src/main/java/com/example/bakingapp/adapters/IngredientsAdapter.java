package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.Ingredient;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;

import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecipeHolder> {
  public static final String TAG = Options.XTAG + IngredientsAdapter.class.getSimpleName();
  
  private @NonNull Context mContext;
  private List<Ingredient> mIngredients;
  
  
  public IngredientsAdapter (@NonNull Context context) {
    mContext = context;
  }
  
  
  @NonNull
  @Override
  public RecipeHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);
    return new RecipeHolder(view);
  }
  
  
  @Override
  public void onBindViewHolder (@NonNull RecipeHolder holder, int position) {
    if ((mIngredients == null) || (mIngredients.size() < position)) return;
    final Ingredient ingredient = mIngredients.get(position);
    if (ingredient != null) holder.mTextTV.setText(Utils.getIngredientLine(mContext, ingredient));
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
