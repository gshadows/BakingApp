package com.example.bakingapp.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

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
  
  
  // Current step's list position.
  public static final int LIST_POSITION_MIDDLE = 0;
  public static final int LIST_POSITION_FIRST  = 1;
  public static final int LIST_POSITION_LAST   = 2;
  public static final int LIST_POSITION_ONLY   = (LIST_POSITION_FIRST | LIST_POSITION_LAST);
  
  /**
   * Detect if current list item is FIRST and/or LAST in steps list.
   * @return Corresponding flags set.
   */
  public static int getListPositionFlags (List list, int position) {
    int flags = 0;
    if (position <= 0) flags |= LIST_POSITION_FIRST;
    if (position >= (list.size() - 1)) flags |= LIST_POSITION_LAST;
    return flags;
  }
  
  
}
