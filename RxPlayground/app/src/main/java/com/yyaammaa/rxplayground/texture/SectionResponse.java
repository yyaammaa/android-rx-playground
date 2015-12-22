package com.yyaammaa.rxplayground.texture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 擬似サーバレスポンス
 */
public class SectionResponse {

  @SerializedName("track_id") public int trackId;
  @SerializedName("title") public String title;
  @SerializedName("text") public String text;

  public static String toJson(SectionResponse model) {
    return new Gson().toJson(model, SectionResponse.class);
  }

  public static SectionResponse fromJson(String jsonString) {
    return new Gson().fromJson(jsonString, SectionResponse.class);
  }

  public SectionResponse() {
  }
}