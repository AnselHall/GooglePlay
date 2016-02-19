package com.exe.googleplay.util;

import android.graphics.drawable.Drawable;

import com.exe.googleplay.global.GooglePlayApplication;

/**
 * Created by user on 2016/2/18.
 */
public class CommonUtil {

    /**
     * 在主线程中执行Runnable
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        GooglePlayApplication.getMainHandler().post(runnable);
    }

    /**
     * 获取字符串资源
     * @param id
     * @return
     */
    public static String getStringi(int id) {
        return GooglePlayApplication.getContext().getResources().getString(id);
    }

    /**
     * 获取图片资源
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return GooglePlayApplication.getContext().getResources().getDrawable(id);
    }

    /**
     * 获取字符串数组资源
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return GooglePlayApplication.getContext().getResources().getStringArray(id);
    }

//    public static String[] getStringArray
}
