package com.yyaammaa.rxplayground.retrofit;

import android.content.Context;

import com.yyaammaa.rxplayground.util.Logr;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class GitHub {

  private static final String ENDPOINT = "https://api.github.com/";

  public static GitHubApiClient createApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(GitHub.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(GitHubApiClient.class);
  }

  public static <T> Observable<T> getRefreshTokenAndResume(
      Context context,
      Observable<T> toBeResumed) {

    // ここはUIスレッドになるらしい (というか、observeOnの設定次第)
    boolean isUiThread = Thread.currentThread().equals(
        context.getMainLooper().getThread());
    Logr.e("getRefreshTokenAndResume: isUiThread = " + isUiThread);

    // これでも通信はできるが、UIスレッドをblockingすることになるのではないかなきっと
    Gist gist = GitHub.createApiClient().getGistById("f36f5dba0c2ae784688c")
        .subscribeOn(Schedulers.io())
        .toBlocking().single();
    if (gist.id != null) {
      Logr.e("getRefreshTokenAndResume: success");
      return toBeResumed;
    }

    Logr.e("getRefreshTokenAndResume: fail");
    return Observable.error(
        new Exception("getRefreshTokenAndResume failed"));
  }

  public static <T> Func1<Throwable, Observable<? extends T>> getRefreshTokenAndResume2(
      final Context context, final Observable<T> toBeResumed) {
    return new Func1<Throwable, Observable<? extends T>>() {
      @Override
      public Observable<? extends T> call(Throwable throwable) {

        // ここはUIスレッドではない (というか、subscribeOnの設定次第)
        boolean isUiThread = Thread.currentThread().equals(
            context.getMainLooper().getThread());
        Logr.e("getRefreshTokenAndResume2: call = " + isUiThread);

        Gist gist = GitHub.createApiClient().getGistById("f36f5dba0c2ae784688c")
            .toBlocking().single();
        if (gist.id != null) {
          Logr.e("getRefreshTokenAndResume2: success");
          return toBeResumed;
        }

        Logr.e("getRefreshTokenAndResume2: fail");
        return Observable.error(
            new Exception("getRefreshTokenAndResume2 failed"));
      }
    };
  }

}