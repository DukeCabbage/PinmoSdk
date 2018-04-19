package com.evilnut.pinmosdk.popup;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;

final class Utility {

    static Bitmap makeThumbnail(final Bitmap source) {
        int width = source.getHeight();
        int height = source.getWidth();

        float ratio = (float) Math.min(width, height) / (float) Math.max(width, height);

        int thumbWidth, thumbHeight;
        if (width > height) {
            thumbWidth = 128;
            thumbHeight = (int) Math.ceil(128 * ratio);
        } else {
            thumbWidth = (int) Math.ceil(128 * ratio);
            thumbHeight = 128;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, thumbWidth, thumbHeight, true);
        if (scaledBitmap != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }

        return scaledBitmap;
    }

    static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
