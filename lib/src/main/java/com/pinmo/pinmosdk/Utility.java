package com.pinmo.pinmosdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utility {
    private static final String END_POINT = "https://www.ylffg.com/";

    static Retrofit provideRetrofit(String appId, String appSecret) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        PinmoApiAuthInterceptor authInterceptor = new PinmoApiAuthInterceptor(appId, appSecret);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(END_POINT)
                .build();
    }

//    @NonNull
//    static <T> T checkNotNull(@Nullable final T reference, final Object errorMessage) {
//        if (reference == null) {
//            throw new NullPointerException(String.valueOf(errorMessage));
//        }
//        return reference;
//    }
//
//    @NonNull
//    static <T> T checkNotNull(@Nullable final T reference) {
//        if (reference == null) {
//            throw new NullPointerException();
//        }
//        return reference;
//    }
}
