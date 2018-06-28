package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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
  
  
  private static void updateAppWidget (Context context, AppWidgetManager manager, int appWidgetId, Recipe recipe) {
    
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
    
    // Setup ingredients ListView.
    if (recipe != null) {
      // Set IngredientsWidgetService to provide views for ListView.
      Intent serviceIntent = new Intent(context, IngredientsWidgetService.class);
      serviceIntent.putParcelableArrayListExtra(IngredientsWidgetService.EXTRA_INGREDIENTS, recipe.getIngredients());
      views.setRemoteAdapter(R.id.widget_listview, serviceIntent);
      
      // Set list view click intent.
      Intent activityIntent = new Intent(context, RecipeActivity.class);
      activityIntent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.widget_listview, pendingIntent);
    }
  
    // Setup empty (fallback) view
    {
      Intent activityIntent = new Intent(context, MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.widget_fallback, pendingIntent);
      views.setEmptyView(R.id.widget_listview, R.id.widget_fallback);
    }
    
    // Instruct the widget manager to update the widget
    manager.updateAppWidget(appWidgetId, views);
  }
  
  
  public static void updateAllWidgets (Context context, AppWidgetManager manager, int[] appWidgetIds, Recipe recipe) {
    // There may be multiple widgets active, so update all of them.
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, manager, appWidgetId, recipe);
    }
  }
  
  
  @Override
  public void onUpdate (Context context, AppWidgetManager manager, int[] appWidgetIds) {
    // Do not do automatic update. Wait for user recipe selection in app.
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

