package com.evilnut.pinmosdk;

import com.evilnut.pinmosdk.network.response.PinmoResponse;

public final class PinmoFeed {

    public final String status;
    public final String errorMessage;

    public final String contentUrl;
    public final String imageUrl;
    public final String momentTitle;
    public final String momentDescription;

    private PinmoFeed(String status,
                      String contentUrl,
                      String imageUrl,
                      String momentTitle,
                      String momentDescription) {
        this.status = status;
        this.errorMessage = null;

        this.contentUrl = contentUrl;
        this.imageUrl = imageUrl;
        this.momentTitle = momentTitle;
        this.momentDescription = momentDescription;
    }

    private PinmoFeed(String status,
                      String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;

        this.contentUrl = null;
        this.imageUrl = null;
        this.momentTitle = null;
        this.momentDescription = null;
    }

    static PinmoFeed fromResponse(PinmoResponse res) {
        if (res.status.equals("success") && res.data.quest.fbQuest.questLink != null) {
            return new PinmoFeed(res.status,
                    res.data.quest.fbQuest.questLink,
                    res.imagePath,
                    res.data.quest.fbQuest.questLink,
                    res.data.quest.appMomentContent);
        } else {
            return new PinmoFeed(res.status, "Status not success");
        }
    }

    static PinmoFeed fromError(Throwable error) {
        return new PinmoFeed("failed", error.getMessage());
    }

    static PinmoFeed mock() {
        return new PinmoFeed("success",
                "https://www.ylffg.com/xFb/GcPNstTygsnTv4FM",
                "https://www.mikestewart.ca/wp-content/uploads/2017/12/Bordeaux-Prelim-Rendering-768x920.jpg",
                "Brentwood Luxury Living",
                "Brentwood性价比最高豪华公寓Boreaux，抢房倒计时，预约就现在！！！");
    }
}
