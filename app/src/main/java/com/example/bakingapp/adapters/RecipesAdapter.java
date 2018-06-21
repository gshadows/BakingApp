package com.example.bakingapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.utils.Options;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder> {
  public static final String TAG = Options.XTAG + RecipesAdapter.class.getSimpleName();
  
  public interface OnClickListener { void onClick (int itemId); }
  
  private Context mContext;
  private OnClickListener mOnClickListener;

  private ArrayList<Recipe> mRecipes;
  private RequestOptions mRequestOptions;
  

  public RecipesAdapter (Context context, OnClickListener listener) {
    mContext = context;
    mRequestOptions = new RequestOptions()
        .error(R.mipmap.ic_launcher_round)
        .placeholder(R.mipmap.ic_launcher_round)
        //.centerCrop()
        //.centerInside()
        //.fitCenter()
        //.circleCrop()
    ;

    mOnClickListener = listener;
  }
  
  
  @NonNull
  @Override
  public RecipeHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
    return new RecipeHolder(view);
  }
  
  
  @Override
  public void onBindViewHolder (@NonNull RecipeHolder holder, int position) {
    
    if ((mRecipes == null) || (mRecipes.size() < position)) return;
    final Recipe recipe = mRecipes.get(position);
    
    Log.d(TAG, "Recipe #" + position);
    Log.d(TAG, "ID:    " + recipe.getId());
    Log.d(TAG, "Image: " + recipe.getImage());
    Log.d(TAG, "Name:  " + recipe.getName());
    Log.d(TAG, "Servs: " + recipe.getServings());
    Log.d(TAG, "Ingr:  " + recipe.getIngredients().size());
    Log.d(TAG, "Steps: " + recipe.getSteps().size());
    
    holder.mTitleTV.setText(recipe.getName());
    
    // Set recipe preview image. Use app logo if no image available.
    Glide.with(holder.mImageIV)
        .load(recipe.getImage())
        .apply(mRequestOptions)
        .listener(new RequestListener<Drawable>() {
          @Override public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Log.w(TAG, "Image loading failed: " + recipe.getImage());
            return true;
          }
          @Override public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return true;
          }
        })
        .into(holder.mImageIV);
  }


  @Override
  public int getItemCount() {
    return (mRecipes != null) ? mRecipes.size() : 0;
  }
  
  
  public Recipe getRecipe (int id) { return ((mRecipes != null) && (id < mRecipes.size())) ? mRecipes.get(id) : null; }
  
  
  public ArrayList<Recipe> getRecipes() { return mRecipes; }
  
  
  public void setRecipes (ArrayList<Recipe> recipes) {
    mRecipes = recipes;
    notifyDataSetChanged();
  }
  
  
  public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
    public TextView   mTitleTV;
    public ImageView  mImageIV;
    
    public RecipeHolder (View itemView) {
      super(itemView);
      mTitleTV = itemView.findViewById(R.id.recipe_name_tv);
      mImageIV = itemView.findViewById(R.id.recipe_image_iv);
      itemView.setOnClickListener(this);
    }
    
    @Override public void onClick (View v) {
      if (mOnClickListener != null) mOnClickListener.onClick(getAdapterPosition());
    }
  }
}
