package com.yyaammaa.rxplayground.texture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {

  @SerializedName("title") public String title;
  @SerializedName("description") public String description;
  @SerializedName("sections") public List<Section> sections;

  public static String toJson(Article model) {
    return new Gson().toJson(model, Article.class);
  }

  public static Article fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, Article.class);
  }

  public Article() {
  }
}