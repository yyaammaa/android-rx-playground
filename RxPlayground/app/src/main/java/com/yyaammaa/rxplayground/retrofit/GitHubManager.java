package com.yyaammaa.rxplayground.retrofit;

import com.yyaammaa.rxplayground.util.Logr;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class GitHubManager {

  public GitHubManager() {
  }

  public Observable<Gist> get404andRetryGetToken() {

    GitHubApiClient client = GitHub.createApiClient();
    Observable<Gist> obs = client.get404();
    obs.subscribeOn(Schedulers.io())
        // observeOnの前に置かないと、refreshTokenAndRetry()が
        // UIスレッドで実行されてしまうので注意な
        .onErrorResumeNext(refreshTokenAndRetry(obs))
        .observeOn(AndroidSchedulers.mainThread())
    ;
    return obs;

//    return Observable.defer(new Func0<Observable<Gist>>() {
//      @Override
//      public Observable<Gist> call() {
//        GitHubApiClient client = GitHub.createApiClient();
//        Observable<Gist> obs = client.get404();
//        obs.subscribeOn(Schedulers.io())
//            // observeOnの前に置かないと、refreshTokenAndRetry()が
//            // UIスレッドで実行されてしまうので注意な
//            .onErrorResumeNext(refreshTokenAndRetry(obs))
//            .observeOn(AndroidSchedulers.mainThread());
//
//        return obs;
//      }
//    });
  }

  private <T> Func1<Throwable, ? extends Observable<? extends T>> refreshTokenAndRetry(
      final Observable<T> toBeResumed) {
    return new Func1<Throwable, Observable<? extends T>>() {
      @Override
      public Observable<? extends T> call(Throwable throwable) {
        // Here check if the error thrown really is a 401
//        if (isHttp401Error(throwable)) {
//          return refreshToken().flatMap(new Func1<AuthToken, Observable<? extends T>>() {
//            @Override
//            public Observable<? extends T> call(AuthToken token) {
//              return toBeResumed;
//            }
//          });
//        }

        // deferが望ましい ("HeaderにTokenを再度セットしてもう1回やる" とかのユースケースなら)
        // ref: http://stackoverflow.com/questions/26201420/retrofit-with-rxjava-handling-network-exceptions-globally#comment54457784_26201962
        Logr.e("refreshTokenAndRetry: call: getAuthToken success");
        return toBeResumed;

        // re-throw this error because it's not recoverable from here
        // Logr.e("refreshTokenAndRetry: call: getAuthToken failed");
        // return Observable.error(throwable);
      }
    };
  }
}