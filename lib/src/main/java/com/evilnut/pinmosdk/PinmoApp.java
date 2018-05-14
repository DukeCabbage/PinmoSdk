package com.evilnut.pinmosdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.evilnut.pinmosdk.network.PinmoApi;
import com.evilnut.pinmosdk.network.PinmoRetrofitProvider;
import com.evilnut.pinmosdk.network.response.PinmoResponse;
import com.evilnut.pinmosdk.popup.ShareOptionsDialogFragment;
import com.evilnut.pinmosdk.popup.ShareSuccessDialogFragment;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Entry point of this SDK
 */
public final class PinmoApp {

    private static PinmoApp singleton;

    private final String endpoint;
    private final PinmoApi pinmoApi;
    private final String wechatAppId;

    private BehaviorSubject<PinmoFeed> feedPublisher;

    private PinmoApp(final PinmoAppOptions options) {
        String appId = options.appId;
        String appSecret = options.appSecret;
        this.endpoint = options.pinmoEndpoint;
        wechatAppId = options.wechatAppId;

        Retrofit retrofit = PinmoRetrofitProvider.provide(appId, appSecret, endpoint);
        pinmoApi = retrofit.create(PinmoApi.class);
    }

    /**
     * Lazy init a BehaviorSubject if not initialized yet or terminated for some reason.
     * {@link ShareOptionsDialogFragment} will listen on this broadcaster for update.
     * Also exposed to any client app wishes to subscribe to the status changes.
     */
    public BehaviorSubject<PinmoFeed> getFeedPublisher() {
        if (feedPublisher == null || feedPublisher.hasComplete() || feedPublisher.hasThrowable()) {
            feedPublisher = BehaviorSubject.create();
//            feedPublisher = BehaviorSubject.createDefault(PinmoFeed.mock());
//            feedPublisher = BehaviorSubject.createDefault(PinmoFeed.fromError(new RuntimeException("Mock error")));
        }
        return feedPublisher;
    }

    public String getEndpoint() { return endpoint; }

    @Nullable
    public PinmoFeed getFeed() {
        return feedPublisher.getValue();
    }

    public void fetchFeed(@NonNull final String userEmail) {

        final String[] pair = userEmail.split("@");
        if (pair.length != 2) throw new IllegalArgumentException("Invalid email");

        pinmoApi.getTest(pair[0], pair[1], null)
//        pinmoApi.getTest(userEmail, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<PinmoResponse, PinmoFeed>() {
                    @Override
                    public PinmoFeed apply(PinmoResponse pinmoResponse) { return PinmoFeed.fromResponse(pinmoResponse); }
                })
                .onErrorReturn(new Function<Throwable, PinmoFeed>() {
                    @Override
                    public PinmoFeed apply(Throwable throwable) { return PinmoFeed.fromError(throwable); }
                })
                .subscribe(new Observer<PinmoFeed>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(PinmoFeed feed) { getFeedPublisher().onNext(feed); }

                    @Override
                    public void onError(Throwable e) { Timber.e(e); }

                    @Override
                    public void onComplete() { }
                });
    }

    public DialogFragment getShareDialog(boolean shareToFriend) {
        return ShareOptionsDialogFragment.newInstance(wechatAppId, shareToFriend);
    }

    public DialogFragment getSuccessDialog() {
        return ShareSuccessDialogFragment.newInstance();
    }

    // region Static
    public static PinmoApp get() {
        if (singleton == null) throw new IllegalStateException("Not initialized");
        else return singleton;
    }

    public static void initApp(final PinmoAppOptions options) {
        if (singleton == null) {
            synchronized (PinmoApp.class) {
                if (singleton == null) {
                    Timber.i("Build type: %s", BuildConfig.BUILD_TYPE);
                    Timber.i("Version %s, code %d", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);

                    singleton = new PinmoApp(options);
                }
            }
        }
    }

    // endregion
}
