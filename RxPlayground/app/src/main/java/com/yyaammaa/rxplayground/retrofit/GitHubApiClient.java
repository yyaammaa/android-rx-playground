package com.yyaammaa.rxplayground.retrofit;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GitHubApiClient {

  // e.g. https://api.github.com/users/:username/gists

  // e.g. https://api.github.com/gists/f36f5dba0c2ae784688c
  @GET("gists/{id}")
  Observable<Gist> getGistById(@Path("id") String id);

}