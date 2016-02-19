package com.exe.googleplay.util;

import android.util.Log;

/**
 * Created by user on 2016/2/18.
 */
public class LogUtil {
    private static boolean isDebug = true;

    /**
     * 打印i级别的log
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * 使用object的类名作为Tag
     * @param object
     * @param msg
     */
    public static void i(Object object, String msg) {
        if (isDebug) {
            Log.i(object.getClass().getSimpleName(), msg);
        }
    }

    /**
     * 打印e级别的log
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    /**
     * 使用object的类名作为Tag
     * @param object
     * @param msg
     */
    public static void e(Object object, String msg) {
        if (isDebug) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }
}
