package com.zzc.androidtrain.jnitest;

import android.graphics.Bitmap;

/**
 * Created by zczhang on 16/7/7.
 */
public class BitmapTest {

    protected static native void blurBitmap(Bitmap bitmap, int r);
}
