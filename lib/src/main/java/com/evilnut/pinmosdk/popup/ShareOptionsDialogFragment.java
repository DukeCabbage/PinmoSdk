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

    private Disposable disposable;

    private ImageView ivBanner;
    private Bitmap thumbnailBitmap;

    private final View.OnClickListener facebookOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
            Toast.makeText(activity, "Facebook on click", Toast.LENGTH_SHORT).show();

            PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null || feed.contentUrl == null) return;

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag("#ConnectTheWorld")
                            .build())
                    .setContentUrl(Uri.parse(feed.contentUrl))
                    .build();
            ShareDialog.show(activity, content);
        }
    };

    private final View.OnClickListener wechatOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
            Toast.makeText(activity, "Wechat on click", Toast.LENGTH_SHORT).show();

            if (getArguments() == null) return;
            String wechatAppId = getArguments().getString("wechatAppId", null);
            boolean shareToFriend = getArguments().getBoolean("shareToFriend", false);

            if (wechatAppId == null) return;
            IWXAPI wechatApi = WXAPIFactory.createWXAPI(activity, wechatAppId, true);

            PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null || feed.contentUrl == null) return;

            WXWebpageObject webpageObject = new WXWebpageObject(feed.contentUrl);
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

    private final View.OnClickListener linkedinOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
            Toast.makeText(activity, "Linkedin on click", Toast.LENGTH_SHORT).show();

            PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null || feed.contentUrl == null) return;

            if (Utility.isPackageInstalled("com.twitter.android", activity.getPackageManager())) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.twitter.android");
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, feed.contentUrl);
                startActivity(shareIntent);
            } else {
                Toast.makeText(activity, "Twitter not installed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final View.OnClickListener twitterOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) return;
            Toast.makeText(activity, "Twitter on click", Toast.LENGTH_SHORT).show();

            PinmoFeed feed = PinmoApp.get().getFeed();
            if (feed == null || feed.contentUrl == null) return;

            if (Utility.isPackageInstalled("com.linkedin.android", activity.getPackageManager())) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.linkedin.android");
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, feed.contentUrl);
                startActivity(shareIntent);
            } else {
                Toast.makeText(activity, "Linkedin not installed", Toast.LENGTH_SHORT).show();
            }
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
        PinmoApp.get().getFeedPublisher()
                .subscribe(new Observer<PinmoFeed>() {
                    @Override
                    public void onSubscribe(Disposable d) { disposable = d; }

                    @Override
                    public void onNext(final PinmoFeed pinmoFeed) {
                        if (pinmoFeed.imageUrl == null) return;
                        Picasso.get()
                                .load(pinmoFeed.imageUrl)
                                .transform(new Transformation() {
                                    @Override
                                    public Bitmap transform(final Bitmap source) { return Utility.makeThumbnail(source); }

                                    @Override
                                    public String key() { return "thumb_" + pinmoFeed.imageUrl; }
                                })
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) { thumbnailBitmap = bitmap; }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) { Timber.e(e); }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                                });

                        Picasso.get()
                                .load(pinmoFeed.imageUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.no_image)
                                .fit()
                                .centerCrop()
                                .into(ivBanner);
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


    public static ShareOptionsDialogFragment newInstance(@NonNull String wechatId, boolean shareToFriend) {
        ShareOptionsDialogFragment frag = new ShareOptionsDialogFragment();

        Bundle args = new Bundle();
        args.putString("wechatAppId", wechatId);
        args.putBoolean("shareToFriend", shareToFriend);
        frag.setArguments(args);

        return frag;
    }
}
