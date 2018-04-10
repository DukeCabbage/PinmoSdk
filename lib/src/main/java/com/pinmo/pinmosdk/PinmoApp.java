package com.pinmo.pinmosdk;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import retrofit2.Retrofit;
import timber.log.Timber;

public final class PinmoApp {

    private static PinmoApp singleton;

    private final String mAppId;
    private final String mAppSecret;

    private final PinmoApi pinmoApi;

    private PinmoApp(String appId, String appSecret) {
        Timber.i("Init %s, %s", appId, appSecret);

        this.mAppId = appId;
        this.mAppSecret = appSecret;

        Retrofit retrofit = Utility.provideRetrofit(appId, appSecret);
        pinmoApi = retrofit.create(PinmoApi.class);
    }

    public PinmoApi pinmoApi() { return pinmoApi; }

    public void test() {
        Timber.w("Api id: %s, secret: %s", mAppId, mAppSecret);
    }

    public DialogFragment getShareDialog(@NonNull final String userEmail) {
        return ShareOptionsDialogFragment.newInstance(userEmail);
    }

    public DialogFragment getSuccessDialog() {
        return ShareSuccessDialogFragment.newInstance();
    }

    public static PinmoApp get() {
        if (singleton == null) throw new IllegalStateException("Not initialized");
        else return singleton;
    }

    public static void initApp(String appId, String appSecret) {
        if (singleton == null) {
            synchronized (PinmoApp.class) {
                if (singleton == null) {
                    singleton = new PinmoApp(appId, appSecret);
                }
            }
        }
    }
}
