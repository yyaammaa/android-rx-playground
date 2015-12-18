package com.yyaammaa.rxplayground.retrofit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public final class GitHub {

  private static final String ENDPOINT = "https://api.github.com/";

  public static GitHubApiClient getApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(GitHub.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(GitHubApiClient.class);
  }
}