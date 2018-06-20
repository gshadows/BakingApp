package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


/**
 * POJO generated from JSON with RoboPOJOGenerator plugin: https://github.com/robohorse/RoboPOJOGenerator
 * Parcelable generated from POJO with parcelabler: http://www.parcelabler.com
 */
public class Recipe implements Parcelable {

  @Getter private int              id;
  @Getter private String           name;
  @Getter private List<Ingredient> ingredients;
  @Getter private List<Step>       steps;
  @Getter private int              servings;
  @Getter private String           image;
  
  
  protected Recipe(@NonNull Parcel in) {
    image = in.readString();
    servings = in.readInt();
    name = in.readString();
    if (in.readByte() == 0x01) {
      ingredients = new ArrayList<Ingredient>();
      in.readList(ingredients, Ingredient.class.getClassLoader());
    } else {
      ingredients = null;
    }
    id = in.readInt();
    if (in.readByte() == 0x01) {
      steps = new ArrayList<Step>();
      in.readList(steps, Step.class.getClassLoader());
    } else {
      steps = null;
    }
  }
  
  
  @Override
  public void writeToParcel (@NonNull Parcel dest, int flags) {
    dest.writeString(image);
    dest.writeInt(servings);
    dest.writeString(name);
    if (ingredients == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(ingredients);
    }
    dest.writeInt(id);
    if (steps == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(steps);
    }
  }
  
  
  @Override public int describeContents() { return 0; }
  
  
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
    @Override public @NonNull
    Recipe createFromParcel (@NonNull Parcel in) {
      return new Recipe(in);
    }
    @Override public @NonNull Recipe[] newArray (int size) {
      return new Recipe[size];
    }
  };
}
