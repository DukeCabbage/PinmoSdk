package com.evilnut.pinmosdk.network.response;

import com.google.gson.annotations.SerializedName;

public class WechatQuest {

    @SerializedName("quest_link")
    public String questLink;

    @SerializedName("quest_link_code")
    public String questLinkCode;
}