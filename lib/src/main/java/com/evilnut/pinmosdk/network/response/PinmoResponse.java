package com.evilnut.pinmosdk.network.response;

import com.google.gson.annotations.SerializedName;

public class PinmoResponse {

    @SerializedName("data")
    public Data data;

    @SerializedName("status")
    public String status;
}