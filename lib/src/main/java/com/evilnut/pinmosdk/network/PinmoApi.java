package com.evilnut.pinmosdk.network;

import com.evilnut.pinmosdk.network.response.PinmoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PinmoApi {

    @GET("pinsdk")
    Observable<PinmoResponse> getTest(@Query("emailPre") String userEmailPres,
                                      @Query("emailSuf") String userEmailSuf,
                                      @Query("quest_id") Integer questId);
}
