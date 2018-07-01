package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.RecipeActivity;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.utils.Options;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
  
  public static final String TAG = Options.XTAG + IngredientsWidget.class.getSimpleName();
  
  
  private static Recipe mRecipe = null;
  
  
  private static String recipeName (Recipe recipe) { return (recipe == null) ? "(null)" : recipe.getName(); }
  
  
  private static void updateAppWidget (Context context, AppWidgetManager manager, int appWidgetId, Recipe recipe) {
    Log.d(TAG, "updateAppWidget() recipe: " + recipeName(mRecipe) + " -> " + recipeName(recipe));
    if (recipe != null) mRecipe = recipe;
    
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
    
    // Setup ingredients ListView.
    if (mRecipe != null) {
      Log.d(TAG, "updateAppWidget() recipe: " + mRecipe.getName());
      
      // Set IngredientsWidgetService to provide views for ListView.
      Intent serviceIntent = new Intent(context, IngredientsWidgetService.class);
      serviceIntent.putParcelableArrayListExtra(IngredientsWidgetService.EXTRA_INGREDIENTS, mRecipe.getIngredients());
      views.setRemoteAdapter(R.id.widget_listview, serviceIntent);
      
      // Set list view click intent.
      Intent activityIntent = new Intent(context, RecipeActivity.class);
      activityIntent.putExtra(RecipeActivity.EXTRA_RECIPE, mRecipe);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.widget_listview, pendingIntent);
      
    } else {
      
      Log.d(TAG, "updateAppWidget() w/o recipe");
      
      Intent activityIntent = new Intent(context, MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.widget_fallback, pendingIntent);
    }
    
    views.setEmptyView(R.id.widget_listview, R.id.widget_fallback);
    // Setup empty (fallback) view
    
    // Instruct the widget manager to update the widget
    manager.updateAppWidget(appWidgetId, views);
  }
  
  
  public static void updateAllWidgets (Context context, AppWidgetManager manager, int[] appWidgetIds, Recipe recipe) {
    Log.d(TAG, "updateAllWidgets() recipe: " + recipeName(recipe));
    // There may be multiple widgets active, so update all of them.
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, manager, appWidgetId, recipe);
    }
  }
  
  
  @Override
  public void onUpdate (Context context, AppWidgetManager manager, int[] appWidgetIds) {
    Log.d(TAG, "onUpdate() calling update all without recipe");
    updateAllWidgets(context, manager, appWidgetIds, null);
  }
  
  
  @Override
  public void onEnabled (Context context) {
    // Enter relevant functionality for when the first widget is created.
  }
  
  
  @Override
  public void onDisabled (Context context) {
    // Enter relevant functionality for when the last widget is disabled.
  }
}

