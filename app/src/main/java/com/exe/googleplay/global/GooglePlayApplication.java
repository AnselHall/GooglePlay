package com.exe.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by user on 2016/2/18.
 */
public class GooglePlayApplication extends Application {
    private static GooglePlayApplication mContext;//项目中共有上下文对象
    private static Handler mainHandler;//主线程Handler

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        mainHandler = new Handler();
    }

    /**
     * 获取全局上下文对象
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取主线程Handler
     * @return
     */
    public static Handler getMainHandler() {
        return mainHandler;
    }
}
