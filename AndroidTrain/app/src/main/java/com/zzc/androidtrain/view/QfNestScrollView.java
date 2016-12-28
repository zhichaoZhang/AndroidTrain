package com.zzc.androidtrain.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.zzc.androidtrain.util.LogUtil;

/**
 * 自定义嵌套ScrollView
 * 当滚动到顶部时，不处理触摸事件
 * <p>
 * Created by zczhang on 16/11/19.
 */

public class QfNestScrollView extends ScrollView {
    private static final String TAG = "QfNestScrollView";

    public QfNestScrollView(Context context) {
        super(context);
    }

    public QfNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QfNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QfNestScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float mInterceptDownY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInterceptDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                float yDiff = moveY - mInterceptDownY;
                LogUtil.d(TAG, "onInterceptTouchEvent: getScrollY = " + getScrollY());
                LogUtil.d(TAG, "onInterceptTouchEvent: yDiff = " + yDiff);
                if(yDiff >= 0 && getScrollY() == 0) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        int action = MotionEventCompat.getActionMasked(ev);
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mInterceptDownY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveY = ev.getY();
//                float yDiff = moveY - mInterceptDownY;
//                LogUtil.d(TAG, "onTouchEvent: getScrollY = " + getScrollY());
//                LogUtil.d(TAG, "onTouchEvent: yDiff = " + yDiff);
//                if(yDiff >= 0 && getScrollY() == 0) {
//                    return false;
//                }
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
}
