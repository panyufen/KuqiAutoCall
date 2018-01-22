package com.example.pan.autocall.http.bean.reqbean;

import com.example.pan.autocall.http.base.HttpMethodType;
import com.example.pan.autocall.http.bean.base.ReqBean;

/**
 * Created by PAN on 2018/1/22.
 */

public class VersionReqBean extends ReqBean {

    //https://raw.githubusercontent.com/panyufen/panyufen.github.io/master/kuqi/config.json

    @Override
    public HttpMethodType getMethodType() {
        return HttpMethodType.GET;
    }

    @Override
    public String getPath() {
        return "/kuqi/version.html";
    }
}
