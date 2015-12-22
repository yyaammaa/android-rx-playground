package com.yyaammaa.rxplayground.wasabeat.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Images {

  @SerializedName("original") public String original;
  @SerializedName("smaller") public String smaller;
  @SerializedName("medium") public String medium;
  @SerializedName("larger") public String larger;
  @SerializedName("waveform") public String waveform;


  public static String toJson(Images model) {
    return new Gson().toJson(model, Images.class);
  }

  public static Images fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Images.class);
  }

  public Images() {
  }
}