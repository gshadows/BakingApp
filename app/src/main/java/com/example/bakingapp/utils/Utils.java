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
  
  
  /**
   * Attempt to detect if the name belongs to a video file by extension.
   * @param name File name.
   * @return True if this file could be video.
   */
  public static boolean probablyVideoFile (String name) {
    if (name.endsWith(".mp4")) return true;
    if (name.endsWith(".mpeg")) return true;
    if (name.endsWith(".mpeg2")) return true;
    if (name.endsWith(".ogm")) return true;
    if (name.endsWith(".ts")) return true;
    if (name.endsWith(".avi")) return true;
    if (name.endsWith(".mkv")) return true;
    if (name.endsWith(".flv")) return true;
    if (name.endsWith(".webm")) return true;
    return false;
  }
  
  
  /**
   * Attempt to detect if the name belongs to a audio file by extension.
   * @param name File name.
   * @return True if this file could be audio.
   */
  public static boolean probablyAudioFile (String name) {
    if (name.endsWith(".mp3")) return true;
    if (name.endsWith(".wav")) return true;
    if (name.endsWith(".ogg")) return true;
    if (name.endsWith(".ac3")) return true;
    if (name.endsWith(".asf")) return true;
    if (name.endsWith(".wma")) return true;
    if (name.endsWith(".flac")) return true;
    return false;
  }
  
  
  /**
   * Attempt to detect if the name belongs to a image file by extension.
   * @param name File name.
   * @return True if this file could be image.
   */
  public static boolean probablyImageFile (String name) {
    if (name.endsWith(".jpg")) return true;
    if (name.endsWith(".jpeg")) return true;
    if (name.endsWith(".gif")) return true;
    if (name.endsWith(".png")) return true;
    if (name.endsWith(".bmp")) return true;
    if (name.endsWith(".tif")) return true;
    if (name.endsWith(".tiff")) return true;
    if (name.endsWith(".webp")) return true;
    return false;
  }
  
}
