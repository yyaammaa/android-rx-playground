package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.yyaammaa.rxplayground.RxBus.GreetingEvent;
import com.yyaammaa.rxplayground.RxBus.RxBusProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class RxBusActivity extends YouTubeBaseActivity {

  private RxBusActivity self = this;

  private CompositeSubscription mCompositeSubscription;

  public static Intent createIntent(Context caller) {
    return new Intent(caller, RxBusActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_rxbus);
    ButterKnife.bind(this);

    setUpViews();
  }

  @Override
  protected void onResume() {
    super.onResume();

    mCompositeSubscription = new CompositeSubscription();
    mCompositeSubscription.add(
        RxBusProvider.getInstance()
            .toObservable()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> {
              if (o instanceof GreetingEvent) {
                GreetingEvent event = (GreetingEvent) o;
                Toast.makeText(
                    self,
                    "Greeting from " + event.greetingFrom().name(),
                    Toast.LENGTH_SHORT
                ).show();
              } else {
                Toast.makeText(self, "Error?", Toast.LENGTH_SHORT).show();
              }
            })
    );
  }

  @Override
  protected void onPause() {
    super.onPause();

    mCompositeSubscription.unsubscribe();
  }

  @OnClick(R.id.act_rxbus_button_bob)
  void onBobClick() {
    RxBusProvider.getInstance().send(new GreetingEvent(GreetingEvent.Person.BOB));
  }

  private void setUpViews() {
    //
  }

}