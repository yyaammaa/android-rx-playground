package com.yyaammaa.rxplayground.texture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 擬似サーバレスポンス
 */
public class ArticleResponse {

  @SerializedName("title") public String title;
  @SerializedName("description") public String description;
  @SerializedName("sections") public List<SectionResponse> sections;

  public static String toJson(ArticleResponse model) {
    return new Gson().toJson(model, ArticleResponse.class);
  }

  public static ArticleResponse fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, ArticleResponse.class);
  }

  public ArticleResponse() {
  }
}