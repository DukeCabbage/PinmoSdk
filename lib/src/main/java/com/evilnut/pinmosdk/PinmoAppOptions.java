package com.evilnut.pinmosdk;

public final class PinmoAppOptions {

    // Pinmo
    public final String appId;
    public final String appSecret;
    public final String pinmoEndpoint;

    // Wechat
    public final String wechatAppId;
//    public final String wechatAppSecret;

    private PinmoAppOptions(final Builder builder) {
        // TODO: Argument checking

        this.appId = builder.appId;
        this.appSecret = builder.appSecret;
        this.pinmoEndpoint = builder.pinmoEndpoint;
        this.wechatAppId = builder.wechatAppId;
//        this.wechatAppSecret = builder.wechatAppSecret;
    }

    public static final class Builder {
        private String appId;
        private String appSecret;
        private String pinmoEndpoint;

        private String wechatAppId;
//        private String wechatAppSecret;

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder appSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public Builder pinmoEndpoint(String pinmoEndpoint) {
            this.pinmoEndpoint = pinmoEndpoint;
            return this;
        }

        public Builder wechatAppId(String wechatAppId) {
            this.wechatAppId = wechatAppId;
            return this;
        }

//        public Builder wechatAppSecret(String wechatAppSecret) {
//            this.wechatAppSecret = wechatAppSecret;
//            return this;
//        }

        public PinmoAppOptions build() {
            return new PinmoAppOptions(this);
        }
    }
}
