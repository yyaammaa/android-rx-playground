package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yyaammaa.rxplayground.retrofit.Gist;
import com.yyaammaa.rxplayground.retrofit.GitHub;
import com.yyaammaa.rxplayground.retrofit.GitHubApiClient;
import com.yyaammaa.rxplayground.util.Logr;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
    get1();
  }

  private void test() {

  }

  private void get1() {

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(GitHub.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    GitHubApiClient client = retrofit.create(GitHubApiClient.class);

    // https://api.github.com/gists/f36f5dba0c2ae784688c
    Observable<Gist> obs = client.getGistById("f36f5dba0c2ae784688c");
    obs.subscribeOn(Schedulers.io())
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
            Logr.e("onNext: " + gist.url);
          }
        });
  }
}