package com.yyaammaa.rxplayground.wasabeat.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Urls {

  @SerializedName("resource") public String resource;
  @SerializedName("permalink") public String permalink;
  @SerializedName("sample") public String sample;

  public static String toJson(Urls model) {
    return new Gson().toJson(model, Urls.class);
  }

  public static Urls fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Urls.class);
  }

  public Urls() {
  }
}