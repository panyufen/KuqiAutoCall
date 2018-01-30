package com.example.pan.autocall.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.pan.autocall.Logger;
import com.example.pan.autocall.SharePreferenceHelper;

public class PhoneStateReceive extends BroadcastReceiver {

    private int mCurrentState = TelephonyManager.CALL_STATE_IDLE;
    private int mOldState = TelephonyManager.CALL_STATE_IDLE;

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }
        Logger.i("action: " + intent.getAction());
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            mOldState = SharePreferenceHelper.getInstance(mContext.getApplicationContext()).getInt(Config.FLAG_CALL_STATE);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    mCurrentState = TelephonyManager.CALL_STATE_IDLE;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mCurrentState = TelephonyManager.CALL_STATE_OFFHOOK;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    mCurrentState = TelephonyManager.CALL_STATE_RINGING;
                    break;
            }

            if (mOldState == TelephonyManager.CALL_STATE_IDLE && mCurrentState == TelephonyManager.CALL_STATE_OFFHOOK) {
                Logger.i("onCallStateChanged: 接通");
                SharePreferenceHelper.getInstance(mContext.getApplicationContext()).putInt(Config.FLAG_CALL_STATE, mCurrentState);
            } else if (mOldState == TelephonyManager.CALL_STATE_OFFHOOK && mCurrentState == TelephonyManager.CALL_STATE_IDLE) {
                Logger.i("onCallStateChanged: 挂断");
                SharePreferenceHelper.getInstance(mContext.getApplicationContext()).putInt(Config.FLAG_CALL_STATE, mCurrentState);
            }
        }
    }

    public class Config {
        public static final String FLAG_CALL_STATE = "FLAG_CALL_STATE";
    }
}
