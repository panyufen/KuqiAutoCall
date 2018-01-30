package com.example.pan.autocall.database;

import cn.bmob.v3.BmobObject;

/**
 * Created by PAN on 2018/1/25.
 */

public class User extends BmobObject {

    private String username;
    private String mobilePhoneNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
