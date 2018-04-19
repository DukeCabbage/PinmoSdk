package com.evilnut.pinmosdk.network.response;

import com.google.gson.annotations.SerializedName;

public class Quest {

    @SerializedName("app_moment_content")
    public String appMomentContent;

    @SerializedName("app_moment_image")
    public String appMomentImage;

    @SerializedName("fb_quest")
    public FbQuest fbQuest;
}