package com.example.pan.autocall;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by PAN on 2018/1/25.
 */

public class TelephoneProcessor {

    private Context context;

    PhoneStateListener listener;

    public TelephoneProcessor(final Context context) {

        this.context = context;

        // 取得电话服务
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
                        Toast.makeText(context, "无任何状态", Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                        Toast.makeText(context, "接起电话", Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
                        Toast.makeText(context, "电话进来时", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        //监听电话的状态
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }


}
