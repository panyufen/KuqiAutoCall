package com.example.pan.autocall.http.event;


import com.example.pan.autocall.http.bean.base.ReqBean;
import com.example.pan.autocall.http.bean.base.ResBean;

/**
 * Created by Pan on 2017/11/18.
 */

public class ResEvent<T extends ResBean> {

    public ReqBean reqBean;

    public T resBean;

}
