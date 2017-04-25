package com.lychee.controllerview;

import android.app.Application;

import com.lychee.controllerview.controllerView.Controller;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

/**
 * Created by lychee on 17-2-21.
 */

public class PlusApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initWeex();
    }


    private void initWeex() {
        InitConfig config = new InitConfig.Builder()
                .build();
        WXSDKEngine.initialize(this, config);
        try {
            WXSDKEngine.registerComponent("controller", Controller.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}
