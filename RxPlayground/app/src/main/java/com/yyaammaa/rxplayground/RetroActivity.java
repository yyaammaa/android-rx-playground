package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yyaammaa.rxplayground.retrofit.Gist;
import com.yyaammaa.rxplayground.retrofit.GitHub;
import com.yyaammaa.rxplayground.retrofit.GitHubApiClient;
import com.yyaammaa.rxplayground.util.Logr;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RetroActivity extends ActionBarActivity {

  public static Intent createIntent(Context caller) {
    return new Intent(caller, RetroActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_retro);
    ButterKnife.bind(this);

    test();
  }

  @OnClick(R.id.act_retro_test_button_1)
  void onTest1Click() {
    //get1();
    //get2();
    //get3();
    get4();
  }

  private void test() {
  }

  private void get4() {
    final Context context = getApplicationContext();
    Observable<Gist> obs = GitHub.createApiClient().getGistById("aa");
    obs
        .subscribeOn(Schedulers.io())
            //  .onErrorResumeNext(GitHub.getRefreshTokenAndResume(getApplicationContext(), obs))
        .onErrorResumeNext(GitHub.getRefreshTokenAndResume2(getApplicationContext(), obs))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Gist>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Gist gist) {
            Logr.e("onNext: " + gist.id);
          }
        });

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

  private void get3() {

    // このやりかただと何故かNetworkOnMainThreadエラーがでてしまう... 謎。↓でも解決できない
    // http://stackoverflow.com/questions/33266886/networkonmainthread-rxjava-retrofit-lollipop
//    Observable<Gist> deferred = Observable.defer(new Func0<Observable<Gist>>() {
//      @Override
//      public Observable<Gist> call() {
//        Logr.e("defer: Call");
//        return new GitHubManager().get404andRetryGetToken();
//      }
//    });
//    deferred
//        .unsubscribeOn(Schedulers.io())
//        .subscribe(new Observer<Gist>() {
//      @Override
//      public void onCompleted() {
//        Logr.e("onCompleted");
//      }
//
//      @Override
//      public void onError(Throwable throwable) {
//        Logr.e("onError");
//        throwable.printStackTrace();
//      }
//
//      @Override
//      public void onNext(Gist gist) {
//        Logr.e("onNext");
//      }
//    });

    GitHubApiClient client = GitHub.createApiClient();
    Observable<Gist> obs = client.get404();
    obs.subscribeOn(Schedulers.io())
        // observeOnの前に置かないと、refreshTokenAndRetry()が
        // UIスレッドで実行されてしまうので注意な
        .onErrorResumeNext(refreshTokenAndRetry(obs))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Gist>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Gist gist) {
            Logr.e("onNext");
          }
        });
  }

  private void get2() {
    GitHubApiClient client = GitHub.createApiClient();
    Observable<List<Gist>> obs = client.getGistsByUsername("yamabit");
    obs.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(new Func1<List<Gist>, Observable<Gist>>() {
          @Override
          public Observable<Gist> call(List<Gist> gists) {
            return Observable.from(gists);
          }
        })
        .map(new Func1<Gist, Gist>() {
          @Override
          public Gist call(Gist gist) {
            gist.url = "wooooo!! " + gist.url;
            return gist;
          }
        })
        .subscribe(new Observer<Gist>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Gist gist) {
            Logr.e(gist.url);
          }
        });
//        .subscribe(new Observer<List<Gist>>() {
//          @Override
//          public void onCompleted() {
//            Logr.e("onCompleted");
//          }
//
//          @Override
//          public void onError(Throwable throwable) {
//            Logr.e("onError");
//            throwable.printStackTrace();
//          }
//
//          @Override
//          public void onNext(List<Gist> gists) {
//            Logr.e("size = " + gists.size());
//          }
//        });
  }

  private void get1() {
    GitHubApiClient client = GitHub.createApiClient();

    // TODO: 404エラーとかどこでハンドリング？

    // https://api.github.com/gists/f36f5dba0c2ae784688c
    Observable<Gist> obs = client.getGistById("f36f5dba0c2ae784688c");
    obs.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
//        .map(new Func1<Gist, Gist>() {
//          @Override
//          public Gist call(Gist gist) {
//            gist.url = "this is url: " + gist.url;
//            return gist;
//          }
//        })
        .subscribe(new Observer<Gist>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Gist gist) {
            Logr.e("onNext: " + gist.url);
          }
        });
  }
}