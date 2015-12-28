package com.yyaammaa.rxplayground.util;

import android.util.Log;

public final class Logr {

  private static final String TAG = "RxPlayground";

  private Logr() {
    // No instances.
  }

  public static void d(String s) {
    Log.d(TAG, s);
  }

  public static void e(String s) {
    Log.e(TAG, s);
  }

}