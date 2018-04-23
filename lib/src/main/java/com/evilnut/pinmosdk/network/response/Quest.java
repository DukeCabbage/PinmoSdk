package com.evilnut.pinmosdk.network.response;

import com.google.gson.annotations.SerializedName;

public class Quest {

    @SerializedName("id")
    public String questId;

    @SerializedName("background_image")
    public String backgroundImage;

    @SerializedName("temp_name")
    public String tempName;

    @SerializedName("temp_content")
    public String tempContent;

    @SerializedName("app_moment_image")
    public String appMomentImage;

    @SerializedName("fb_quest")
    public FbQuest fbQuest;

    @SerializedName("wechat_quest")
    public WechatQuest wechatQuest;
}