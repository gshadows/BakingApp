package com.example.bakingapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


public class Utils {
  public static final String TAG = Options.XTAG + Utils.class.getSimpleName();
  
  /**
   * [de]serealization splitter character.
   * Even if this char appears in input String it could be safely dropped because it is invalid :)
   */
  private static final String SECRET_CHAR = "\uFFFF";
  
  
  /**
   * Create ingredient description line using given Ingredient data structure.
   * @param context    Context need to access string resources.
   * @param ingredient Ingredient object.
   * @return Generated string.
   */
  public static @NonNull String getIngredientLine (@NonNull Context context, @NonNull Ingredient ingredient) {
    // Prepare measurement string.
    String measure = ingredient.getMeasure().toLowerCase();
    
    // Ensure 1st character in upper case.
    String ingName = ingredient.getIngredient();
    if (Character.isLowerCase(ingName.charAt(0))) {
      char[] chars = ingName.toCharArray();
      chars[0] = Character.toUpperCase(chars[0]);
      ingName = String.valueOf(chars);
    }
    
    return context.getString (R.string.ingredient_line_format, ingName, ingredient.getQuantity(), measure);
  }
  
  
  /**
   * This is used to pass ingredients list to the widget.
   * Because every ingredient is finally just a formatted string (@see getIngredientLine) it is
   * easy to concatenate all strings.
   * @param context     Context need to access string resources.
   * @param ingredients Ingredient list to serialize.
   * @return Serialized ingredients String concatenated with special character.
   */
  public static @NonNull String serializeIngredients (@NonNull Context context, ArrayList<Ingredient> ingredients) {
    if ((ingredients == null) || (ingredients.size() < 1)) return "";
    StringBuilder sb = new StringBuilder();
    for (Ingredient ingredient : ingredients) {
      sb.append(getIngredientLine(context, ingredient).replaceAll(SECRET_CHAR, ""));
      sb.append(SECRET_CHAR);
    }
    return sb.toString();
  }
  
  
  /**
   * This is used to pass ingredients list to the widget.
   * Because every ingredient is finally just a formatted string (@see getIngredientLine) it is
   * easy to concatenate all strings.
   * @param str Serialized ingredients String concatenated with special character.
   * @return Deserialized array of ingredients strings.
   */
  public static String[] deserializeIngredients (String str) {
    if ((str == null) || (str.length() < 1)) return null;
    return str.split(SECRET_CHAR);
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
