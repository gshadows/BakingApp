package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import lombok.Getter;


/**
 * POJO generated from JSON with RoboPOJOGenerator plugin: https://github.com/robohorse/RoboPOJOGenerator
 * Parcelable generated from POJO with parcelabler: http://www.parcelabler.com
 */
public class Ingredient implements Parcelable {

  @Getter private float   quantity;
  @Getter private String  measure;
  @Getter private String  ingredient;
  
  
  protected Ingredient (@NonNull Parcel in) {
    quantity = in.readFloat();
    measure = in.readString();
    ingredient = in.readString();
  }
  
  
  @Override
  public void writeToParcel (@NonNull Parcel dest, int flags) {
    dest.writeFloat(quantity);
    dest.writeString(measure);
    dest.writeString(ingredient);
  }
  
  
  @Override public int describeContents() { return 0; }
  
  
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
    @Override public @NonNull Ingredient createFromParcel (@NonNull Parcel in) {
      return new Ingredient(in);
    }
    @Override public @NonNull Ingredient[] newArray (int size) {
      return new Ingredient[size];
    }
  };
}
