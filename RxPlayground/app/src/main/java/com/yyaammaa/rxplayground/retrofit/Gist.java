package com.yyaammaa.rxplayground.retrofit;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

// eg. https://api.github.com/gists/f36f5dba0c2ae784688c
public class Gist {

  @SerializedName("id") public String id;
  @SerializedName("description") public String description;
  @SerializedName("url") public String url;

  public static String toJson(Gist model) {
    return new Gson().toJson(model, Gist.class);
  }

  public static Gist fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Gist.class);
  }

  public Gist() {
  }

}