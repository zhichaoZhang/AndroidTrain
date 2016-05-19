package com.zzc.androidtrain.common;

/**
 * 字体大小
 * <p>
 * Created by zczhang on 16/4/6.
 */
public enum FontSizeType {
    FONT_SIZE_SMALL(0.5f),
    FONT_SIZE_NORMAL(1.0f),
    FONT_SIZE_LARGE(2.0f),
    FONT_SIZE_EXTRA_LATGE(3.0f);

    public float value;

    FontSizeType(float scale) {
        this.value = scale;
    }

}
