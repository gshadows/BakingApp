package com.example.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.utils.Options;
import com.example.bakingapp.utils.Utils;


public class IngredientsWidgetService extends RemoteViewsService {
  
  @Override public RemoteViewsFactory onGetViewFactory (Intent intent) {
    return new IngredientsListRVFactory(getApplicationContext());
  }
  
  
  /**
   * This factory supplies Ingredients Widget's ListView with RemoteViews.
   */
  private static class IngredientsListRVFactory implements RemoteViewsService.RemoteViewsFactory {
    
    public static final String TAG = Options.XTAG + IngredientsListRVFactory.class.getSimpleName();
    
    
    private Context mContext;
    private String[] mIngredients;
    
    
    public IngredientsListRVFactory (@NonNull Context context) {
      //Log.d(TAG, "ctor()");
      mContext = context;
      //onDataSetChanged(); // Attempt to restore from shared preferences.
    }
  
  
    @Override public void onDataSetChanged() {
      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
      if (sharedPrefs == null) {
        Log.e(TAG, "SharedPreferences in null!");
        return;
      }
  
      String ingredientsSerialized = sharedPrefs.getString(Options.KEY_INGREDIENTS, null);
      Log.d(TAG, "onDataSetChanged() ingredients restored: " + ingredientsSerialized);
      mIngredients = Utils.deserializeIngredients(ingredientsSerialized);
      Log.d(TAG, "onDataSetChanged() intent ingredients size: " + ((mIngredients == null) ? "(null)" : mIngredients.length));
    }
    
    
    @Override
    public RemoteViews getViewAt (int position) {
      Log.d(TAG, "getViewAt() " + position);
      // Create RemoteViews for the grid cell layout.
      RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
      
      // Get ingredients ling.
      if ((position >= 0) && (position < mIngredients.length)) {
        Log.d(TAG, "getViewAt() set text to: " + mIngredients[position]);
        views.setTextViewText(android.R.id.text1, mIngredients[position]);
      } else {
        Log.e(TAG, "getViewAt() position out of range");
      }
      
      return views;
    }
    
    
    @Override
    public int getCount() {
      if (mIngredients == null) {
        Log.w(TAG, "getCount() returns 0 because mIngredients is null");
        return 0;
      }
      Log.d(TAG, "getCount() returns " + ((mIngredients == null) ? 0 : mIngredients.length));
      return (mIngredients == null) ? 0 : mIngredients.length;
    }
    
    
    @Override public void         onCreate() {}
    @Override public void         onDestroy() {}
    @Override public RemoteViews  getLoadingView()          { return null; }
    @Override public int          getViewTypeCount()        { return 1; }
    @Override public long         getItemId (int position)  { return 0; }
    @Override public boolean      hasStableIds()            { return false; }
    
  }
}
