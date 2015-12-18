package com.yyaammaa.rxplayground;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yyaammaa.rxplayground.util.Logr;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

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
    hello("john", "mike");
  }

  private void test() {
    // numbers(0, 1, 2, 3, 4, 5);
    list(Arrays.asList("one", "two", "three"));
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
