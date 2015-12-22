package com.yyaammaa.rxplayground.texture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

public class Section {

  @SerializedName("track") public Track track;
  @SerializedName("title") public String title;
  @SerializedName("text") public String text;

  public static String toJson(Section model) {
    return new Gson().toJson(model, Section.class);
  }

  public static Section fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Section.class);
  }

  public Section() {
  }
}