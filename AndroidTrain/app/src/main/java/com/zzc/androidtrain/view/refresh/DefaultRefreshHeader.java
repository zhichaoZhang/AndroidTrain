package com.zzc.androidtrain.view.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 默认下拉刷新头部
 *
 * Created by zczhang on 16/11/17.
 */

public class DefaultRefreshHeader extends View implements QfRefreshHeaderHandler {
    public DefaultRefreshHeader(Context context) {
        super(context);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefreshReset() {

    }

    @Override
    public void onRefreshPrepare() {

    }

    @Override
    public void onRefreshBegin() {

    }

    @Override
    public void onRefreshComplete() {

    }

    @Override
    public void onPullPositionChanged(float y) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
