package com.evilnut.pinmosdkdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.evilnut.pinmosdk.PinmoApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_test) ImageView ivTest;
    @BindView(R.id.fab_share) FloatingActionButton fabTest;
    @BindView(R.id.fab_check) FloatingActionButton fabTest2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PinmoApp.initApp(MyApplication.pinmoAppOptions);

        Timber.i("Printing sdk params...");
        Timber.d(PinmoApp.get().toString());
    }
}
