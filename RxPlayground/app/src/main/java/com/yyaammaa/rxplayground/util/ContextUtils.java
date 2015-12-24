package com.yyaammaa.rxplayground.util;

import android.content.Context;

public final class ContextUtils {

  private ContextUtils() {
    // No instances.
  }

  public static boolean isUiThread(Context context) {
    return Thread.currentThread().equals(context.getMainLooper().getThread());
  }

}