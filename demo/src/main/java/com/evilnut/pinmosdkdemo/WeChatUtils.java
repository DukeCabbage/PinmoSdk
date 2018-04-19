package com.evilnut.pinmosdkdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 微信跳转相关工具类
 * Created by Spencer on 7/18/16.
 */
public final class WeChatUtils {

    /**
     * 扫一扫
     */
    public static final String SCAN = "weixin://dl/scan";

    /**
     * 反馈
     */
    public static final String FEEDBACK = "weixin://dl/feedback";

    /**
     * 朋友圈
     */
    public static final String MOMENTS = "weixin://dl/moments";

    /**
     * 设置
     */
    public static final String SETTINGS = "weixin://dl/settings";

    /**
     * 消息通知设置
     */
    public static final String NOTIFICATIONS = "weixin://dl/notifications";

    /**
     * 聊天设置
     */
    public static final String CHAT = "weixin://dl/chat";

    /**
     * 通用设置
     */
    public static final String GENERAL = "weixin://dl/general";

    /**
     * 公众号
     */
    public static final String OFFICIALACCOUNTS = "weixin://dl/officialaccounts";

    /**
     * 游戏
     */
    public static final String GAMES = "weixin://dl/games";

    /**
     * 帮助
     */
    public static final String HELP = "weixin://dl/help";

    /**
     * 个人信息
     */
    public static final String PROFILE = "weixin://dl/profile";

    /**
     * 功能插件
     */
    public static final String FEATURES = "weixin://dl/features";

    /**
     * 修改用户名
     */
    public static final String SET_NAME = "weixin://dl/setname";

    /**
     * 我的二维码
     */
    public static final String MY_QRCODE = "weixin://dl/myQRcode";

    /**
     * 我的地址
     */
    public static final String MY_ADDRESS = "weixin://dl/myaddress";

    /**
     * 相册
     */
    public static final String POSTS = "weixin://dl/posts";

    /**
     * 收藏
     */
    public static final String FAVORITES = "weixin://dl/favorites";

    /**
     * 优惠券
     */
    public static final String CARD = "weixin://dl/card";

    private WeChatUtils() {
        // no instance
    }

    /**
     * 跳转到微信某页面
     *
     * @param context
     */
    public static void jump(Context context, String wechatScheme) {
        if (context == null) {
            throw new NullPointerException("context is null.");
        } else if (TextUtils.isEmpty(wechatScheme)) {
            throw new NullPointerException("wechat scheme is empty or null");
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(wechatScheme));
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        context.startActivity(intent);
    }

    /**
     * 打开微信
     */
    public static void open(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }
}