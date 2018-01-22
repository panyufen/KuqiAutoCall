package com.example.pan.autocall.http.event;


import com.example.pan.autocall.http.bean.base.ResBean;

/**
 * Created by Pan on 2017/11/18.
 */
public class ReqEvent {

    //默认异步执行
    public ExecutionType execType = ExecutionType.CONCURRENT;
    public Class<? extends ResEvent> resEventClazz;
    public Class<? extends ResBean> resBeanClazz;

    public Object reqBean;

    public enum ExecutionType {
        CONCURRENT,
        SERIAL
    }
}
