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
    get2();
  }

  private void test() {

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