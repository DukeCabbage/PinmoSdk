package com.pinmo.pinmosdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public final class ShareSuccessDialogFragment extends DialogFragment {

    private static final String strStoreLink = "market://details?id=com.google.android.apps.maps";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share_success, null);

        final Button btnDownload = view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(strStoreLink));
                startActivity(intent);

                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setCancelable(false)
                .setView(view)
                .create();
    }

    static ShareSuccessDialogFragment newInstance() {
        ShareSuccessDialogFragment frag = new ShareSuccessDialogFragment();

        return frag;
    }
}
