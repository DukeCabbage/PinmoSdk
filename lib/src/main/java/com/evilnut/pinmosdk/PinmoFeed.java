package com.evilnut.pinmosdk;

import android.support.annotation.Nullable;

import com.evilnut.pinmosdk.network.response.PinmoResponse;
import com.evilnut.pinmosdk.network.response.Quest;

import java.util.HashMap;
import java.util.Map;

public final class PinmoFeed {

    public final String status;
    public final String errorMessage;

    public final String questId;
    public final String facebookHashTag;
    public final String momentTitle;
    public final String momentDescription;
    private final String backgroundImage;
    private final String momentImage;
    private final Map<String, String> questLinks;

    private PinmoFeed(String status,
                      String questId,
                      Map<String, String> questLinks,
                      String backgroundImage,
                      String facebookHashTag,
                      String momentImage,
                      String momentTitle,
                      String momentDescription) {
        this.status = status;
        this.errorMessage = null;

        this.questId = questId;
        this.questLinks = questLinks;
        this.backgroundImage = backgroundImage;
        this.facebookHashTag = facebookHashTag;
        this.momentImage = momentImage;
        this.momentTitle = momentTitle;
        this.momentDescription = momentDescription;
    }

    private PinmoFeed(String status,
                      String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;

        this.questId = null;
        this.questLinks = null;
        this.backgroundImage = null;
        this.facebookHashTag = null;
        this.momentImage = null;
        this.momentTitle = null;
        this.momentDescription = null;
    }

    @Nullable
    public String getQuestLink(final String key) {

        return getQuestLink(key, true);
    }

    @Nullable
    public String getQuestLink(final String key, final boolean fallback) {
        if (questLinks == null) return null;

        String link = questLinks.get(key);
        if (!fallback || link != null) return link;
        else return questLinks.get("default");
    }

    @Nullable
    public String getBackgroundImageUrl() {
        if (backgroundImage == null) return null;
        return PinmoApp.get().getEndpoint() + "public/landing_page/" + backgroundImage;
    }

    @Nullable
    public String getMomentImageUrl() {
        if (momentImage == null) return null;
        return PinmoApp.get().getEndpoint() + "public/quests_app/quest" + questId + "/" + momentImage;
    }

    static PinmoFeed fromResponse(PinmoResponse res) {
        if (res.status.equals("success")) {
            try {
                Quest quest = res.data.quest;

                Map<String, String> links = new HashMap<>();
                links.put("default", quest.fbQuest.questLink);

                if (quest.wechatQuest.questLink != null)
                    links.put("wechat", quest.wechatQuest.questLink);

                return new PinmoFeed(res.status,
                        quest.questId,
                        links,
                        quest.backgroundImage,
                        null,
                        quest.appMomentImage,
                        quest.tempName,
                        quest.tempContent);

            } catch (Exception e) {
                return fromError(e);
            }
        } else {
            return new PinmoFeed(res.status, "Request error");
        }
    }

    static PinmoFeed fromError(Throwable error) {
        return new PinmoFeed("error", error.getMessage());
    }

    static PinmoFeed mock() {
        Map<String, String> links = new HashMap<>();
        links.put("default", "https://www.ylffg.com/xFb/GcPNstTygsnTv4FM");
        links.put("wechat", "https://www.ylffg.com/x/EHWw5RiK74");

        return new PinmoFeed("success",
                "74",
                links,
                "74_iXDEyTn3zecuOBNM/background/big.png",
                "Brentwood Luxury",
                "152357885620180412172056182.png",
                "Brentwood性价比最高豪华公寓Boreaux，抢房倒计时，预约就现在！！！",
                "Brentwood性价比最高豪华公寓Boreaux，抢房倒计时，预约就现在！！！");
    }
}
