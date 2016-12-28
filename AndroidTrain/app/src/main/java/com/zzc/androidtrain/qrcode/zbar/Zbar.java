package com.zzc.androidtrain.qrcode.zbar;

/**
 * Zbar工具类Java类调用入口
 *
 * Created by zczhang on 16/12/4.
 */

public class Zbar {
    static {
        System.loadLibrary("zbar");
        System.loadLibrary("iconv");
    }

    public native String decode(int width, int height, byte[] data);
}
