package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder> {
  public static final String TAG = Options.XTAG + RecipesAdapter.class.getSimpleName();
  
  public interface OnClickListener { void onRecipeClick (int itemId); }
  
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
    
    holder.mTitleTV.setText(recipe.getName());
    
    // Get image URL and check it.
    String image = recipe.getImage();
    if (image != null) {
      if (image.isEmpty() || Utils.probablyVideoFile(image) || Utils.probablyAudioFile(image)) image = null;
    }
  
    // Set recipe preview image. Use app logo if no image available.
    if (image != null) {
      Glide.with(holder.mImageIV)
          .load(image)
          .apply(mRequestOptions)
          .into(holder.mImageIV);
    } else {
      // Do not waste time calling Glide for null.
      holder.mImageIV.setImageResource(R.mipmap.ic_launcher);
    }
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
      if (mOnClickListener != null) mOnClickListener.onRecipeClick(getAdapterPosition());
    }
  }
}
