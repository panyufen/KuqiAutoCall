package com.example.pan.autocall;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Random;

import cn.bmob.v3.Bmob;

/**
 * Created by PAN on 2018/1/19.
 */

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {
    private SharedPreferences sharedPreference;
    private String usrid = "usreid";

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        sharedPreference = getSharedPreferences("SHARE_TAG", Context.MODE_PRIVATE);
        int userId = new Random().nextInt(65535);
        int shareid = sharedPreference.getInt(usrid, 0);
        if (shareid == 0) {
            sharedPreference.edit().putInt(usrid, userId).apply();
        } else {
            userId = shareid;
        }
        Log.i("userid ", String.valueOf(userId));

        MobclickAgent.onProfileSignIn(String.valueOf(userId));

        Bmob.initialize(this, "906d4ff7ff66f16af28e7254dc54a14e");

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        MobclickAgent.reportError(this, throwable);
    }
}
