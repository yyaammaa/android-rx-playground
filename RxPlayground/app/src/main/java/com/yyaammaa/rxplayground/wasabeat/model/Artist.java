package com.yyaammaa.rxplayground.wasabeat.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Artist {

  @SerializedName("id") public int id;
  @SerializedName("name") public String name;
  @SerializedName("description") public String description;

  public static String toJson(Artist model) {
    return new Gson().toJson(model, Artist.class);
  }

  public static Artist fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Artist.class);
  }

  public Artist() {
  }
}