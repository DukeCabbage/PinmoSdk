package com.evilnut.pinmosdkdemo;

import android.app.Application;

import com.evilnut.pinmosdk.PinmoAppOptions;

import timber.log.Timber;

public class MyApplication extends Application {

    private static final String END_POINT = "https://www.ylffg.com/";
    private static final String PINMO_APP_ID = "m18nybh2t";
    private static final String PINMO_APP_SECRET = "s6bed2r4z31nw7b";

    private static final String WECHAT_APP_ID = "wx1433c78ec80557b7";
//    private static final String WECHAT_APP_SECRET = "";

    public static PinmoAppOptions pinmoAppOptions = new PinmoAppOptions.Builder()
            .appId(PINMO_APP_ID)
            .appSecret(PINMO_APP_SECRET)
            .pinmoEndpoint(END_POINT)
            .wechatAppId(WECHAT_APP_ID)
//            .wechatAppSecret(WECHAT_APP_SECRET)
            .build();

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
