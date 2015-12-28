package com.yyaammaa.rxplayground;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.yyaammaa.rxplayground.retrofit.AuthorizationFailedException;
import com.yyaammaa.rxplayground.util.Logr;
import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity {

  @Bind(R.id.act_main_test_button_0) Button mButton0;
  @Bind(R.id.act_main_test_button_1) Button mButton1;

  private MainActivity self = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_main);
    ButterKnife.bind(self);
    setUpViews();

    test();
  }

  @OnClick(R.id.act_main_test_button_2)
  void onTest2Click() {
    startActivity(WasabeatActivity.createIntent(this));
  }

  @OnClick(R.id.act_main_test_button_3)
  void onTest3Click() {
    startActivity(Wasabeat2Activity.createIntent(this));
  }

  private void test() {
    //hello("john", "mike");
    // numbers(0, 1, 2, 3, 4, 5);
    //list(Arrays.asList("one", "two", "three"));
    //async();
    //async2();
//    mergeTest();
    // mergeTest2();
    //toList();
    //  toList2();
    // retry();
  }

  private void retry() {
    Observable<String> one = getAsync1withError().subscribeOn(Schedulers.newThread());
    one.onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread())
        .retry(2)
        .subscribe(new Observer<String>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
          }

          @Override
          public void onNext(String s) {
            Logr.e(s);
          }
        });
  }

  private void toList2() {
    Observable<String> one = getAsync1().subscribeOn(Schedulers.newThread());
    Observable<String> two = getAsync2().subscribeOn(Schedulers.newThread());
    Observable.merge(one, two)
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread())
        .toList()
        .subscribe(new Observer<List<String>>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
          }

          @Override
          public void onNext(List<String> strings) {
            Logr.e("onNext");
            for (String s : strings) {
              Logr.e(s);
            }
          }
        })
    ;
  }

  private void toList() {
    Observable<String> one = getAsync1().subscribeOn(Schedulers.newThread());
    one.onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread())
        .toList()
        .subscribe(new Observer<List<String>>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
          }

          @Override
          public void onNext(List<String> strings) {
            Logr.e("onNext");
            for (String s : strings) {
              Logr.e(s);
            }
          }
        })
    ;

  }

  private void async2() {
    Observable<String> res = getAsync1();
    res.onBackpressureBuffer()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String s) {
            return !"this is 4".equals(s);
          }
        })
        .subscribe(new Subscriber<String>() {
          @Override
          public void onCompleted() {
            Logr.e("done");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("error");
          }

          @Override
          public void onNext(String s) {
            Logr.e(s);
          }
        })
    ;
  }

  private void mergeTest2() {
    Observable<String> one = getAsync1().subscribeOn(Schedulers.newThread());
    Observable<String> two = getAsync2().subscribeOn(Schedulers.newThread());
    Observable<String> merged = Observable.merge(two, one);
    merged.onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("error");
          }

          @Override
          public void onNext(String s) {
            Logr.e(s);
          }
        })
    ;
  }

  private void mergeTest() {
    Observable<String> one = getAsync1();
    Observable<String> two = getAsync2();

    // これだとtwo, oneの順に実行される (並行じゃない) > おなじスレッドで実行されてるから
    Observable<String> merged = Observable.merge(two, one);
    merged.onBackpressureBuffer()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("error");
          }

          @Override
          public void onNext(String s) {
            Logr.e(s);
          }
        });

//    Observable<String> zip = Observable.zip(one, two, new Func2<String, String, String>() {
//      @Override
//      public String call(String s, String s2) {
//        return s + " -- " + s2;
//      }
//    });
//    zip.onBackpressureBuffer()
//        .subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Observer<String>() {
//          @Override
//          public void onCompleted() {
//            Logr.e("onCompleted");
//          }
//
//          @Override
//          public void onError(Throwable throwable) {
//            Logr.e("error");
//          }
//
//          @Override
//          public void onNext(String s) {
//            Logr.e(s);
//          }
//        });

  }

  private Observable<String> getAsync2() {
    return Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> subscriber) {
            for (int i = 0; i < 10; i++) {
              subscriber.onNext("async2: " + i);
              try {
                Thread.sleep(700);
              } catch (InterruptedException ignore) {
                ignore.printStackTrace();
              }
            }
            subscriber.onCompleted();
          }
        });
  }

  private Observable<String> getAsync1() {
    return Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> subscriber) {
            for (int i = 0; i < 10; i++) {
              subscriber.onNext("this is " + i);
              try {
                Thread.sleep(500);
              } catch (InterruptedException ignore) {
                ignore.printStackTrace();
              }
            }
            subscriber.onCompleted();
          }
        });
  }

  private Observable<String> getAsync1withError() {
    return Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> subscriber) {
            for (int i = 0; i < 10; i++) {
              subscriber.onNext("this is " + i);
              if (i == 5) {
                throw new NullPointerException();
              }
              try {
                Thread.sleep(500);
              } catch (InterruptedException ignore) {
                ignore.printStackTrace();
              }
            }
            subscriber.onCompleted();
          }
        });
  }

  private void async() {
    Observable.create(
        new Observable.OnSubscribe<Integer>() {
          @Override
          public void call(Subscriber<? super Integer> subscriber) {
            for (int i = 0; i < 100; i++) {
              subscriber.onNext(i);
              try {
                Thread.sleep(1000);
              } catch (InterruptedException ignore) {
                ignore.printStackTrace();
              }
            }
            subscriber.onCompleted();
          }
        })
        .onBackpressureBuffer() // これを入れないとダメ ref: http://qiita.com/petitviolet/items/f6d9395d40ee0bbef94b
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError: " + throwable.getMessage());
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Integer i) {
            Logr.e(i.toString());
          }
        })
    ;
  }

  private void list(List<String> list) {
    Observable.from(list)
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String s) {
            return !"two".equals(s);
          }
        })
        .subscribe(
            new Observer<String>() {
              @Override
              public void onCompleted() {
                Logr.e("onCompleted");
              }

              @Override
              public void onError(Throwable throwable) {
              }

              @Override
              public void onNext(String s) {
                Logr.e("onNext: " + s);
              }
            }
        )
//        .subscribe(new Action1<String>() {
//          @Override
//          public void call(String s) {
//            Logr.e(s);
//          }
//        })
    ;
  }

  private void numbers(Integer... ints) {
//    Observable.from(ints)
//        .filter(new Func1<Integer, Boolean>() {
//          @Override
//          public Boolean call(Integer integer) {
//            return integer % 2 == 0;
//          }
//        })
//        .subscribe(new Action1<Integer>() {
//          @Override
//          public void call(Integer integer) {
//            Logr.e(integer.toString());
//          }
//        });

//    Observable.from(ints)
//        .map(new Func1<Integer, String>() {
//          @Override
//          public String call(Integer integer) {
//            return integer % 2 == 0 ? "even" : "odd";
//          }
//        })
//        .subscribe(new Action1<String>() {
//          @Override
//          public void call(String s) {
//            Logr.e(s);
//          }
//        });

    Observable.from(ints)
        .filter(new Func1<Integer, Boolean>() {
          @Override
          public Boolean call(Integer integer) {
            return integer > 2;
          }
        })
        .map(new Func1<Integer, String>() {
          @Override
          public String call(Integer integer) {
            return integer % 2 == 0 ? "even" : "odd";
          }
        })
        .subscribe(new Action1<String>() {
          @Override
          public void call(String s) {
            Logr.e(s);
          }
        });
  }

  private void hello(String... names) {
    Observable.from(names)
        .subscribe(new Action1<String>() {
          @Override
          public void call(String s) {
            Logr.e("hello! " + s);
          }
        });
  }

  private void playground(View v) {
//    Observable.from(Arrays.asList(0, 1, 2, 3, 4, 5))
//        .map(i -> "this is " + String.valueOf(i))
//        .subscribe(
//            s -> Logr.e("onNext: " + s),
//            throwable -> Logr.e(throwable.toString()),
//            () -> Logr.e("completed")
//        );

    Observable<Track> deferred = Observable.defer(() -> {
      Response<Track> response = null;
      try {
        response = Wasabeat.createApiClient().getTrackResponse(404).toBlocking().single();
      } catch (Throwable t) {
        t.printStackTrace();
        return Observable.error(t);
      }

      boolean isSuccess = response.isSuccess();
      int statusCode = response.code();
      Logr.e("isSuccess = " + isSuccess + ", statusCode = " + statusCode);
      if (isSuccess) {
        return Observable.just(response.body());
      } else {
        return Observable.error(new AuthorizationFailedException());
      }
    });

    deferred.subscribeOn(Schedulers.io())
        .onErrorResumeNext(throwable -> {
          if (throwable instanceof AuthorizationFailedException) {
            Logr.e("onErrorResumeNext: AuthorizationFailedException");
          }

          return Observable.error(throwable);
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Track>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
          }

          @Override
          public void onNext(Track track) {
            Logr.e("onNext: " + track.title);
          }
        });

//    Wasabeat.createApiClient().getTrackResponse(84166)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Observer<Response<Track>>() {
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
//          public void onNext(Response<Track> trackResponse) {
//            int statusCode = trackResponse.code();
//            boolean isSuccess = trackResponse.isSuccess();
//            Logr.e("onNext: statusCode = " + statusCode + ", isSuccess = " + isSuccess);
//
//            if (isSuccess) {
//              Track track = trackResponse.body();
//              Logr.e("track title = " + track.title);
//            }
//          }
//        });
  }

  private void setUpViews() {
    mButton0.setOnClickListener(self::playground);
    mButton1.setOnClickListener(v -> startActivity(RetroActivity.createIntent(self)));
  }

}
