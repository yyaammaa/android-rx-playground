package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yyaammaa.rxplayground.util.Logr;
import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WasabeatActivity extends ActionBarActivity {

  public static Intent createIntent(Context caller) {
    return new Intent(caller, WasabeatActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_wasabeat);
    ButterKnife.bind(this);

    setUpViews();
  }

  @OnClick(R.id.act_wasabeat_test_button_1)
  void onTest1Click() {
    get1();
  }

  private void setUpViews() {

  }

  private void get1() {
    Observable<Track> obs = Wasabeat.createApiClient().getTrack(84166);
    obs
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Track>() {
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
          public void onNext(Track track) {
            Logr.e("onNext: " + track.title + ", by " + track.artist.name);
          }
        });

  }

}