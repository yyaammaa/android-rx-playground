package com.yyaammaa.rxplayground.RxBus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {

  //private final PublishSubject<Object> mBus = PublishSubject.create();

  // If multiple threads are going to emit events to this
  // then it must be made thread-safe like this instead
  private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

  public RxBus() {
  }

  public void send(Object o) {
    mBus.onNext(o);
  }

  public Observable<Object> toObserverable() {
    return mBus;
  }

  public boolean hasObservers() {
    return mBus.hasObservers();
  }
}