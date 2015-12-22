package com.yyaammaa.rxplayground.wasabeat.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Track {

  @SerializedName("id") public int id;
  @SerializedName("title") public String title;
  @SerializedName("mix_title") public String mixTitle;
  @SerializedName("duration") public int duration;
  @SerializedName("images") public Images images;
  @SerializedName("urls") public Urls urls;
  @SerializedName("artist") public Artist artist;

  public static String toJson(Track model) {
    return new Gson().toJson(model, Track.class);
  }

  public static Track fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Track.class);
  }

  public Track() {
  }
}