package com.zzc.androidtrain.common;

import android.content.Context;

/**
 * 应用内文件管理类(单例)
 * <p>
 * Created by zczhang on 16/4/6.
 */
public class FontManger {
    private static volatile FontManger mInstance = null;
    private float mFontSize;
    private Context mAppContext;

    private FontManger(Context context) {
        this.mAppContext = context.getApplicationContext();
    }

    public static FontManger getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FontManger.class) {
                if (mInstance == null) {
                    mInstance = new FontManger(context);
                }
            }
        }
        return mInstance;
    }

    public synchronized void setFontSizeType(FontSizeType fontSizeType) {
        setFontSize(fontSizeType.value);
    }

    public synchronized void setFontSize(float fontSize) {
        SpManager.getInstance(mAppContext).saveFloat(ConstantValue.SpKey.SPKEY_FONT_SIZE, fontSize);
    }

    public float getFontSize() {
        if(mFontSize == 0) {
            String fontSize = SpManager.getInstance(mAppContext).getString(ConstantValue.SpKey.SPKEY_FONT_SIZE, String.valueOf(FontSizeType.FONT_SIZE_NORMAL.value));
            mFontSize = Float.parseFloat(fontSize);
        }
        return mFontSize;
    }
}
