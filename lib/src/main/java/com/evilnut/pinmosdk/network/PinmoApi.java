package com.evilnut.pinmosdk.network;

import com.evilnut.pinmosdk.network.response.PinmoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PinmoApi {

    @GET("pinsdk")
    Observable<PinmoResponse> getTest(@Query("email") String userEmail,
                                      @Query("quest_id") Integer questId);
}
