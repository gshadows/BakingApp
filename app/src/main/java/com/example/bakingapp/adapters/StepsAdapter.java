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
import com.example.bakingapp.data.Step;
import com.example.bakingapp.utils.Options;

import java.util.List;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {
  public static final String TAG = Options.XTAG + StepsAdapter.class.getSimpleName();
  
  public interface OnClickListener { void onClick (int itemId); }
  
  private Context mContext;
  private OnClickListener mOnClickListener;

  private List<Step> mSteps;
  private RequestOptions mRequestOptions;
  

  public StepsAdapter (Context context, OnClickListener listener) {
    mContext = context;
    mRequestOptions = new RequestOptions()
        .error(R.mipmap.ic_launcher_round)
        .placeholder(R.mipmap.ic_launcher_round)
    ;

    mOnClickListener = listener;
  }
  
  
  @NonNull
  @Override
  public StepHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent, false);
    return new StepHolder(view);
  }
  
  
  @Override
  public void onBindViewHolder (@NonNull StepHolder holder, int position) {
    if ((mSteps == null) || (mSteps.size() < position)) return;
    Step step = mSteps.get(position);
    
    holder.mNameTV.setText(step.getShortDescription());
    
    // Set step preview image. Use app logo if no image available.
    String thumbnail = step.getThumbnailURL();
    if ((thumbnail != null) && !thumbnail.isEmpty()) {
      Glide.with(holder.mImageIV)
          .load(thumbnail)
          .apply(mRequestOptions)
          .into(holder.mImageIV);
    } else {
      // Don't waste time.
      holder.mImageIV.setImageResource(R.mipmap.ic_launcher);
    }
  }


  @Override
  public int getItemCount() {
    return (mSteps != null) ? mSteps.size() : 0;
  }
  
  
  public Step getStep (int id) { return ((mSteps != null) && (id < mSteps.size())) ? mSteps.get(id) : null; }
  
  
  public void setSteps (List<Step> steps) {
    mSteps = steps;
    notifyDataSetChanged();
  }
  
  
  public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
    public TextView   mNameTV;
    public ImageView  mImageIV;
    
    public StepHolder (View itemView) {
      super(itemView);
      mNameTV = itemView.findViewById(R.id.step_name_tv);
      mImageIV = itemView.findViewById(R.id.step_image_iv);
      itemView.setOnClickListener(this);
    }
    
    @Override public void onClick (View v) {
      int pos = getAdapterPosition();
      if ((pos < 0) || (mSteps == null) || (pos > mSteps.size())) return;
      if (mOnClickListener != null) mOnClickListener.onClick(pos);
    }
  }
}
