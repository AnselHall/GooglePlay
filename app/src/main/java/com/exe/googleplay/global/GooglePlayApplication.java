package com.exe.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.bmob.v3.Bmob;

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
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "55e436a3761f8b7d4d314f95e129e392");
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
