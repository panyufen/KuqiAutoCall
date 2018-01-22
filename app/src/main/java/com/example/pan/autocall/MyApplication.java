package com.example.pan.autocall;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by PAN on 2018/1/19.
 */

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        MobclickAgent.reportError(this, throwable);
    }
}
