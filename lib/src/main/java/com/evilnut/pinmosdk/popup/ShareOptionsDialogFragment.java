package com.evilnut.pinmosdk.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.evilnut.pinmosdk.PinmoApp;
import com.evilnut.pinmosdk.PinmoFeed;
import com.evilnut.pinmosdk.R;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

final public class ShareOptionsDialogFragment extends DialogFragment {

    // Link is required with an optional hash tag
    private final View.OnClickListener facebookOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
//            Toast.makeText(activity, "Facebook on click", Toast.LENGTH_SHORT).show();

            final PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null) return;

            final String link = feed.getQuestLink("facebook");
            if (link == null) return;

            ShareLinkContent.Builder builder = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(link));

            if (feed.facebookHashTag != null) {
                String hashTag = feed.facebookHashTag.startsWith("#")
                        ? feed.facebookHashTag
                        : "#" + feed.facebookHashTag;

                hashTag = hashTag.replace(" ", "");

                builder = builder.setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag(hashTag)
                        .build());
            }

            ShareDialog.show(activity, builder.build());
        }
    };

    // Link only
    private final View.OnClickListener linkedinOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
//            Toast.makeText(activity, "Linkedin on click", Toast.LENGTH_SHORT).show();

            final PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null) return;

            final String link = feed.getQuestLink("linkedin");
            if (link == null) return;

            if (Utility.isPackageInstalled("com.twitter.android", activity.getPackageManager())) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.twitter.android");
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(shareIntent);
            } else {
                Toast.makeText(activity, "Twitter not installed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // Link only
    private final View.OnClickListener twitterOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
//            Toast.makeText(activity, "Twitter on click", Toast.LENGTH_SHORT).show();

            final PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null) return;

            final String link = feed.getQuestLink("twitter");
            if (link == null) return;

            if (Utility.isPackageInstalled("com.linkedin.android", activity.getPackageManager())) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.linkedin.android");
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(shareIntent);
            } else {
                Toast.makeText(activity, "Linkedin not installed", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Disposable disposable;
    private ImageView ivBanner;
    private Bitmap thumbnailBitmap;

    // Link requires plus optional title and thumbnail image
    // thumbnail has size limit, so recommends cropping in advance
    private final View.OnClickListener wechatOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
//            Toast.makeText(activity, "Wechat on click", Toast.LENGTH_SHORT).show();

            if (getArguments() == null) return;
            String wechatAppId = getArguments().getString("wechatAppId", null);
            boolean shareToFriend = getArguments().getBoolean("shareToFriend", false);

            if (wechatAppId == null) return;
            IWXAPI wechatApi = WXAPIFactory.createWXAPI(activity, wechatAppId, true);

            final PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null) return;

            final String link = feed.getQuestLink("wechat");
            if (link == null) return;

            WXWebpageObject webpageObject = new WXWebpageObject(link);
            WXMediaMessage msg = new WXMediaMessage(webpageObject);

            if (shareToFriend) {
                // If shared directly to friends, both title and description would be used
                if (feed.momentTitle != null) msg.title = feed.momentTitle;
                if (feed.momentDescription != null) msg.description = feed.momentDescription;
            } else {
                // Wechat moment only display title
                if (feed.momentDescription != null) msg.title = feed.momentDescription;
                else if (feed.momentTitle != null) msg.title = feed.momentTitle;
            }

            // Thumbnail
            if (thumbnailBitmap != null) {
                msg.thumbData = Utility.bmpToByteArray(thumbnailBitmap, false);
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();

            if (shareToFriend) req.scene = SendMessageToWX.Req.WXSceneSession;
            else req.scene = SendMessageToWX.Req.WXSceneTimeline;

            req.transaction = Utility.buildTransaction("img");
            req.message = msg;

            wechatApi.sendReq(req);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share_options, null);

        ivBanner = view.findViewById(R.id.iv_banner);

        view.findViewById(R.id.btn_share_facebook).setOnClickListener(facebookOnClick);
        view.findViewById(R.id.btn_share_wechat).setOnClickListener(wechatOnClick);
        view.findViewById(R.id.btn_share_linkedin).setOnClickListener(linkedinOnClick);
        view.findViewById(R.id.btn_share_twitter).setOnClickListener(twitterOnClick);

        return new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setCancelable(false)
                .setView(view)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Listens on feed fetching status
        // Once a new feed is available, starts downloading images
        PinmoApp.get().getFeedPublisher()
                .subscribe(new Observer<PinmoFeed>() {
                    @Override
                    public void onSubscribe(Disposable d) { disposable = d; }

                    @Override
                    public void onNext(final PinmoFeed pinmoFeed) {
                        // Load and crop image for wechat moment thumbnail image
                        // If image is unavailable, fallback to use background image
                        if (pinmoFeed.getMomentImageUrl() != null
                                || pinmoFeed.getBackgroundImageUrl() != null) {

                            String url = pinmoFeed.getMomentImageUrl();
                            if (url == null) url = pinmoFeed.getBackgroundImageUrl();

                            Picasso.get()
                                    .load(url)
                                    .transform(new Transformation() {
                                        @Override
                                        public Bitmap transform(final Bitmap source) { return Utility.makeThumbnail(source); }

                                        @Override
                                        public String key() { return "thumb_" + pinmoFeed.questId; }
                                    })
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) { thumbnailBitmap = bitmap; }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) { Timber.e(e); }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) { }
                                    });
                        }

                        // Load and fit background image for the popup
                        if (pinmoFeed.getBackgroundImageUrl() != null) {
                            Picasso.get()
                                    .load(pinmoFeed.getBackgroundImageUrl())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.no_image)
                                    .fit()
                                    .centerCrop()
                                    .into(ivBanner);
                        }
                    }

                    @Override
                    public void onError(Throwable e) { Timber.e(e); }

                    @Override
                    public void onComplete() { Timber.e("Unexpected onComplete"); }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) disposable.dispose();
    }

    /**
     * Creates a new instance of {@link ShareOptionsDialogFragment}
     *
     * @param wechatId,      required if want to access features in wechat sdk
     * @param shareToFriend, if true, will share directly to friends instead of moments
     */
    public static ShareOptionsDialogFragment newInstance(@NonNull String wechatId, boolean shareToFriend) {
        ShareOptionsDialogFragment frag = new ShareOptionsDialogFragment();

        Bundle args = new Bundle();
        args.putString("wechatAppId", wechatId);
        args.putBoolean("shareToFriend", shareToFriend);
        frag.setArguments(args);

        return frag;
    }
}
