package com.evilnut.pinmosdkdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evilnut.pinmosdk.BuildConfig;
import com.evilnut.pinmosdk.PinmoApp;
import com.evilnut.pinmosdk.PinmoFeed;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab_share) FloatingActionButton fabTest;
    @BindView(R.id.fab_check) FloatingActionButton fabTest2;
    @BindView(R.id.et_user_email) EditText etUserEmail;
    @BindView(R.id.btn_refresh) Button btnRefresh;
    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_version) TextView tvVersion;

    private Disposable disposable;

    @OnClick(R.id.fab_share)
    void fabOnClick() {
        PinmoApp.get().getShareDialog(false)
                .show(getSupportFragmentManager(), "share");
    }

    @OnClick(R.id.fab_check)
    void fab2OnClick() {
        PinmoApp.get().getSuccessDialog()
                .show(getSupportFragmentManager(), "success");
    }

    @OnClick(R.id.btn_refresh)
    void refreshOnClick() {
        final String input = etUserEmail.getText().toString();
        if (input.trim().length() == 0) {
            etUserEmail.setError("Empty email");
            return;
        }

        tvStatus.setText("Status: loading\n");
        PinmoApp.get().fetchFeed(input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PinmoApp.initApp(MyApplication.pinmoAppOptions);

        btnRefresh.performClick();

        tvVersion.setText("Ver. " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) disposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PinmoApp.get().getFeedPublisher()
                .subscribe(new Observer<PinmoFeed>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        tvStatus.setText("Status: \n");
                    }

                    @Override
                    public void onNext(final PinmoFeed feed) { tvStatus.setText(debugMessage(feed)); }

                    @Override
                    public void onError(Throwable e) { Timber.e(e); }

                    @Override
                    public void onComplete() {}
                });
    }

    private static String debugMessage(final PinmoFeed feed) {
        String result = "Status: " + feed.status + "\n";
        result += "Error message: " + feed.errorMessage + "\n";
        result += "QuestId: " + feed.questId + "\n";
        result += "\n";

        result += "QuestLinks\n";
        if (feed.getQuestLink("default", false) != null) {
            result += "Default: " + feed.getQuestLink("default") + "\n";
        }

        if (feed.getQuestLink("facebook", false) != null) {
            result += "Facebook: " + feed.getQuestLink("facebook") + "\n";
        }

        if (feed.getQuestLink("wechat", false) != null) {
            result += "Wechat: " + feed.getQuestLink("wechat") + "\n";
        }

        if (feed.getQuestLink("linkedin", false) != null) {
            result += "Linkedin: " + feed.getQuestLink("linkedin") + "\n";
        }

        if (feed.getQuestLink("twitter", false) != null) {
            result += "Twitter: " + feed.getQuestLink("twitter") + "\n";
        }

        result += "Background image: " + feed.getBackgroundImageUrl() + "\n";
        result += "Facebook hashTag: " + feed.facebookHashTag + "\n";
        result += "Moment thumbnail: " + feed.getMomentImageUrl() + "\n";
        result += "Moment title: " + feed.momentTitle + "\n";

        return result;
    }
}