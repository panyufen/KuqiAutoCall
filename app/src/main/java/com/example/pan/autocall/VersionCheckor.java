package com.example.pan.autocall;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.pan.autocall.http.OkHttpHelper;
import com.example.pan.autocall.http.bean.reqbean.VersionReqBean;
import com.example.pan.autocall.http.bean.resbean.VersionResBean;
import com.example.pan.autocall.http.event.VersionResEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by PAN on 2018/1/22.
 */

public class VersionCheckor {

    private static VersionCheckor versionCheckor;

    private int currentVersion = 0;

    private Context mContext;

    private CheckInterface callBack;

    private VersionCheckor(Context context) {
        EventBus.getDefault().register(this);
        this.mContext = context;
    }

    public static VersionCheckor getInstance(Context context) {
        if (versionCheckor == null) {
            synchronized (VersionCheckor.class) {
                if (versionCheckor == null) {
                    versionCheckor = new VersionCheckor(context);
                }
            }
        }
        return versionCheckor;
    }

    public void checkVersion(CheckInterface callBack) {
        this.callBack = callBack;
        VersionReqBean versionReqBean = new VersionReqBean();
        OkHttpHelper.getInstance().requestSync(versionReqBean, VersionResEvent.class, VersionResBean.class);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThread(VersionResEvent versionResEvent) {
        VersionResBean versionResBean = (VersionResBean) versionResEvent.resBean;
        if (versionResBean != null && versionResBean.versionCode > getCurrentVersion()) {
            Logger.i("checkVersion " + getCurrentVersion() + " " + versionResBean.versionCode);
            callBack.upgrade(getCurrentVersion(), versionResBean.versionCode);
        }
    }


    private int getCurrentVersion() {
        PackageInfo info = getPackageInfo();
        return info.versionCode;
    }

    public PackageInfo getPackageInfo() {
        PackageInfo pi = null;

        try {
            PackageManager pm = mContext.getPackageManager();
            pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public interface CheckInterface {
        void upgrade(int currentversion, int netVersion);
    }
}
