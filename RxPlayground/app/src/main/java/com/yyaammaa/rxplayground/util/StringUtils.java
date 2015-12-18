package com.yyaammaa.rxplayground.util;

public final class StringUtils {

  private StringUtils() {
    // No instances.
  }

  /**
   * nullもしくは空文字だったらtrueを返す
   */
  public static boolean isNullOrEmpty(String string) {
    return string == null || string.length() < 1;
  }

  /**
   * nullの場合は空文字を返す。そうじゃない場合はなにもせずに返す
   */
  public static String nullToEmpty(String string) {
    if (string == null) {
      return "";
    }
    return string;
  }

  /**
   * 1つでもnullもしくは空文字だったらtrueを返す
   */
  public static boolean hasNullOrEmpty(String... strings) {
    if (strings == null) {
      return true;
    }

    for (String string : strings) {
      if (isNullOrEmpty(string)) {
        return true;
      }
    }

    return false;
  }

}