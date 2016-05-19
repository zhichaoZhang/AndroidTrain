package com.zzc.androidtrain.widget;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * 自定义View的轮廓提供者
 *
 * Created by zczhang on 16/5/8.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DefineOutLineProvider extends ViewOutlineProvider{
    @Override
    public void getOutline(View view, Outline outline) {

    }
}
