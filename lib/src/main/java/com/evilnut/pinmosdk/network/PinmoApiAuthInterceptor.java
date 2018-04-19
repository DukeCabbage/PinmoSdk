package com.evilnut.pinmosdk.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Appends APP ID and APP SECRET at the end of the url as query params
 */
final class PinmoApiAuthInterceptor implements Interceptor {

    private final String appId;
    private final String appSecret;

    PinmoApiAuthInterceptor(@NonNull String appId, @NonNull String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = builder.build();
        final String oldUrl = request.url().toString();
        String newUrl = oldUrl;

        Timber.v("Original url: %s", oldUrl);

        newUrl += "&appId=" + appId;
        newUrl += "&appSecret=" + appSecret;

        Request newRequest = builder.url(newUrl).build();
        Timber.v("Authenticated url: %s", newRequest.url().toString());

        return chain.proceed(newRequest);
    }
}
