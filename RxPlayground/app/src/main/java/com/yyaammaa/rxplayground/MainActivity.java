package com.yyaammaa.rxplayground;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yyaammaa.rxplayground.util.Logr;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_main);
    ButterKnife.bind(this);
    setUpViews();

    test();
  }

  @OnClick(R.id.act_main_test_button_1)
  void onTest1Click() {

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
    retry();
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

  private void setUpViews() {
  }

}
