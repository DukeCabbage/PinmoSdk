package com.pinmo.pinmosdk;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface PinmoApi {

    @GET("pinmosdk")
    Observable<String> getTest(@Query("email") String userEmail);
}
