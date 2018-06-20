package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import lombok.Getter;


/**
 * POJO generated from JSON with RoboPOJOGenerator plugin: https://github.com/robohorse/RoboPOJOGenerator
 * Parcelable generated from POJO with parcelabler: http://www.parcelabler.com
 */
public class Step implements Parcelable {

  @Getter private int    id;
  @Getter private String shortDescription;
  @Getter private String description;
  @Getter private String videoURL;
  @Getter private String thumbnailURL;
  
  
  protected Step (@NonNull Parcel in) {
    videoURL = in.readString();
    description = in.readString();
    id = in.readInt();
    shortDescription = in.readString();
    thumbnailURL = in.readString();
  }
  
  
  @Override
  public void writeToParcel (@NonNull Parcel dest, int flags) {
    dest.writeString(videoURL);
    dest.writeString(description);
    dest.writeInt(id);
    dest.writeString(shortDescription);
    dest.writeString(thumbnailURL);
  }
  
  
  @Override public int describeContents() { return 0; }
  
  
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
    @Override public @NonNull Step createFromParcel (@NonNull Parcel in) {
      return new Step(in);
    }
    @Override public @NonNull Step[] newArray (int size) {
      return new Step[size];
    }
  };
}
