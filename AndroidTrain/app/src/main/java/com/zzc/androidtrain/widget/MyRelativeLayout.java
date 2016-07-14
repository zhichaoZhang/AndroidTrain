package com.zzc.androidtrain.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 自定义RelativeLayout测试事件分发
 *
 * Created by zczhang on 16/5/20.
 */
public class MyRelativeLayout extends RelativeLayout{
    private static final String TAG = "MyRelativeLayout";
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch(action){
//            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG,"2:onInterceptTouchEvent action:ACTION_DOWN");
//                break;
////            return true;
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG,"2:onInterceptTouchEvent action:ACTION_MOVE");
////                break;
////            return true;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG,"2:onInterceptTouchEvent action:ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG,"2:onInterceptTouchEvent action:ACTION_CANCEL");
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        switch(action){
//            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG,"2:onTouchEvent action:ACTION_DOWN");
//                //return false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG,"2:onTouchEvent action:ACTION_MOVE");
////                return false;
//            break;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG,"2:onTouchEvent action:ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG,"2:onTouchEvent action:ACTION_CANCEL");
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}
