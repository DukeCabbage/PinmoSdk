package com.evilnut.pinmosdk.network.response;

import com.google.gson.annotations.SerializedName;

public class PinmoResponse {

    @SerializedName("data")
    public Data data;

    @SerializedName("image_path")
    public String imagePath;

    @SerializedName("type")
    public String type;

    @SerializedName("status")
    public String status;
}