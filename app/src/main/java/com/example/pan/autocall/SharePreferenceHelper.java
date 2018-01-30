package com.example.pan.autocall;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PAN on 2018/1/25.
 */

public class SharePreferenceHelper {
    private static SharePreferenceHelper sharePreferenceHelper;
    private SharedPreferences preferences;
    private Context context;

    private SharePreferenceHelper(Context context) {
        preferences = context.getSharedPreferences("SHARE_TAG", Context.MODE_PRIVATE);
    }

    public static SharePreferenceHelper getInstance(Context context) {
        return sharePreferenceHelper = new SharePreferenceHelper(context);

    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }
}
