package com.evilnut.pinmosdk.popup;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;

final class Utility {

    static Bitmap makeThumbnail(final Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        float ratio = (float) Math.min(width, height) / (float) Math.max(width, height);

        Timber.v("Original image dimension: %d, %d, ratio: %f", width, height, ratio);

        int thumbWidth, thumbHeight;
        if (width > height) {
            thumbWidth = 128;
            thumbHeight = (int) Math.ceil(128 * ratio);
        } else {
            thumbWidth = (int) Math.ceil(128 * ratio);
            thumbHeight = 128;
        }

        Timber.v("Cropped dimension: %d, %d", thumbWidth, thumbHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, thumbWidth, thumbHeight, true);

        Timber.v("Scaling done");

        if (scaledBitmap != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }

        return scaledBitmap;
    }

    // If fill is true, smaller dimension of the source bitmap will be used, extra will be cropped out
    // If fill is false, bigger dimension will be used, and extra space will be padded
    static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle, final boolean fill) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        boolean landscape = width > height;

        int hi = landscape ? width : height;
        int lo = landscape ? height : width;

        int offSet = (hi - lo) /2;

        Timber.v("landscape: %b", landscape);
        Timber.v("offSet %d", offSet);

        Bitmap localBitmap = Bitmap.createBitmap(hi, hi, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        if (fill) {
            Rect src = landscape
                    ? new Rect(offSet, 0, hi - offSet, lo)
                    : new Rect(0, offSet, lo, hi - offSet);

            localCanvas.drawBitmap(bmp, src, new Rect(0, 0, hi, hi), null);
        } else {
            Rect dest = landscape
                    ? new Rect(0, offSet, hi, hi - offSet)
                    : new Rect(offSet, 0, hi - offSet, hi);

            localCanvas.drawBitmap(bmp, null, dest, null);
        }


        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                localByteArrayOutputStream);
        localBitmap.recycle();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();

        if (needRecycle) bmp.recycle();
        try {
            localByteArrayOutputStream.close();
            return arrayOfByte;
        } catch (Exception e) {
            Timber.e(e);
            return new byte[]{};
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
            Timber.w(e);
            return false;
        }
    }
}
