package com.pinmo.pinmosdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final public class ShareOptionsDialogFragment extends DialogFragment {

    @Nullable private Disposable disposable;

    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        try {
            String userEmail = Objects.requireNonNull(getArguments()).getString("userEmail", null);
            email = Objects.requireNonNull(userEmail, "Missing user email");
        } catch (Exception e) {
            Timber.e(e);
            this.dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share_options, null);

        fetchData();

        return new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setCancelable(false)
                .setView(view)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null) disposable.dispose();
    }

    private void fetchData() {
        PinmoApi api = PinmoApp.get().pinmoApi();
        api.getTest(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) { disposable = d; }

                    @Override
                    public void onNext(String s) { Timber.d(s); }

                    @Override
                    public void onError(Throwable e) { Timber.e(e); }

                    @Override
                    public void onComplete() { }
                });
    }

    static ShareOptionsDialogFragment newInstance(@NonNull String userEmail) {
        ShareOptionsDialogFragment frag = new ShareOptionsDialogFragment();

        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        frag.setArguments(args);

        return frag;
    }
}
