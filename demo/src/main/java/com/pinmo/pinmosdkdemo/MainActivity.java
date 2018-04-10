package com.pinmo.pinmosdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.pinmo.pinmosdk.PinmoApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_test)
    ImageView ivTest;

    @OnClick(R.id.fab_test)
    void fabOnClick() {
        PinmoApp.get().getShareDialog("test123@gmail.com")
                .show(getSupportFragmentManager(), "share");
    }

    @OnClick(R.id.fab_test_2)
    void fab2OnClick() {
        PinmoApp.get().getSuccessDialog()
                .show(getSupportFragmentManager(), "success");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PinmoApp.initApp("Hello", "World");
        PinmoApp.get().test();

//        Picasso.get()
//                .load("http://i.imgur.com/dks888jd.png")
//                .placeholder(R.drawable.ic_image_24dp)
//                .fit().centerCrop()
//                .error(R.drawable.ic_error_24dp)
//                .into(ivTest, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Timber.d("onSuccess");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Timber.d(e);
//                    }
//                });
    }
}
