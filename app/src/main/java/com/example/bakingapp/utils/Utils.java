package com.example.bakingapp.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


public class Utils {

  /**
   * Convert measure code (like "TBLSP", "TSP" etc) to a human-readable format.
   * @param context Context need to access string resources.
   * @param measure Original short measure code.
   * @return Human-readabe measure name.
   */
  public static String makeHumanReadableMeasureString (Context context, String measure) {
    // TODO: Convert measure to a human-readable format.
    return measure;
  }


  /**
   * Get first visible position for given RecyclerView.
   * @param recyclerView
   * @return
   */
  public static int getRecyclerViewPosition (RecyclerView recyclerView) {
    // Here I use LinearLayoutManager because it is base class that defines findFirstVisibleXXX()
    // methods. GridLayoutManager just a subclass of liner and should work well.
    LinearLayoutManager lm = (LinearLayoutManager)recyclerView.getLayoutManager();
    int pos = lm.findFirstCompletelyVisibleItemPosition();
    if (pos == NO_POSITION) pos = lm.findFirstVisibleItemPosition(); // If all items partially invisible.
    return pos;
  }




}
