package com.yyaammaa.rxplayground.util;

import android.util.Log;

public final class Logr {

  private Logr() {
    // No instances.
  }

  public static void d(String s) {
    Log.d("", s);
  }

  public static void e(String s) {
    Log.e("", s);
  }

}