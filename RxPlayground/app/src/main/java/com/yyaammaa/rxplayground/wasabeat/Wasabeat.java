package com.yyaammaa.rxplayground.wasabeat;

import com.yyaammaa.rxplayground.retrofit.GitHubApiClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public final class Wasabeat {

  private static final String ENDPOINT = "http://api.wasabeat.com/v1/";

  public static WasabeatApiClient createApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Wasabeat.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(WasabeatApiClient.class);
  }

}