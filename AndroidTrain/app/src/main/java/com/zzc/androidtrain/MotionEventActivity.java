package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zzc.androidtrain.util.DeviceUtil;
import com.zzc.androidtrain.util.Toaster;
import com.zzc.androidtrain.widget.MyMovableTextView;
import com.zzc.androidtrain.widget.MyRelativeLayout;
import com.zzc.androidtrain.widget.MyScaleTextView;

/**
 * 检测用户触摸动作和手势
 * <p/>
 * MotionEventCompat 提供了一些静态工具类,可以获得与触摸事件相关的动作
 * <p/>
 * GestureDetectorCompat 可以简单的检测常见手势(双击/长按/快速滑动),并且无需自行处理单个触摸事件
 */
public class MotionEventActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private static final String TAG = "MotionEventActivity";
    MyScaleTextView tvTouchEventTest2;
    MyMovableTextView tvTouchEventTest;
    MyRelativeLayout rlRoot;
    private GestureDetectorCompat mDetector;
    private int mActionBarSize, mStatusBarHeight;
    private int mActivePointerId;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MotionEventActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_event);
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        mStatusBarHeight = DeviceUtil.getStatusBarHeight(this);
        System.out.println("mActionBarSize----->"+mActionBarSize);
        System.out.println("mStatusBarHeight----->"+mStatusBarHeight);
        styledAttributes.recycle();
        initView();
    }

    int locationX;
    int locationY;
    int width;
    int height;

    private void initView() {
        tvTouchEventTest = (MyMovableTextView) findViewById(R.id.tv_touch_event_test);
//        tvTouchEventTest.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //getActionMasked方法,设计用来处理多点触摸.他会返回执行过的动作的掩码值,不包括点的索引位.
//                int action = MotionEventCompat.getActionMasked(event);
//                switch (action) {
//                    case (MotionEvent.ACTION_DOWN):
//                        Toaster.showShortToast(getBaseContext(), "Action Down1");
//                        return true;
//                    case (MotionEvent.ACTION_MOVE):
//                        Toaster.showShortToast(getBaseContext(), "Action Move1");
//                        return false;
//                    case (MotionEvent.ACTION_UP):
//                        return false;
//                    case (MotionEvent.ACTION_CANCEL):
//                        //Action_Cancel手势,不会由用户产生,而是由程序产生
//                        Toaster.showShortToast(getBaseContext(), "Action Cancel1");
//                        return false;
//                    case (MotionEvent.ACTION_OUTSIDE):
//                        //Action_Outside貌似也不会触发
//                        Toaster.showShortToast(getBaseContext(), "Action Outside1");
//                        return false;
//                    default:
//                        return false;
//                }
//            }
//        });

        tvTouchEventTest2 = (MyScaleTextView) findViewById(R.id.tv_touch_event_test2);
//        tvTouchEventTest2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //getActionMasked方法,只适用于单点检测,因为其内部已屏蔽掉了索引信息
//                int action = MotionEventCompat.getActionMasked(event);
//                switch (action) {
//                    case (MotionEvent.ACTION_DOWN):
//                        Toaster.showShortToast(getBaseContext(), "Action Down2");
//                        return true;
//                    case (MotionEvent.ACTION_MOVE):
//                        Toaster.showShortToast(getBaseContext(), "Action Move2");
//                        return true;
//                    case (MotionEvent.ACTION_UP):
//                        Toaster.showShortToast(getBaseContext(), "Action Up2");
//                        return true;
//                    case (MotionEvent.ACTION_CANCEL):
//                        //Action_Cancel手势,不会由用户产生,而是由程序产生.只有当父控件拦截后续事件后,系统会告知子控件当前事件流结束
//                        Toaster.showShortToast(getBaseContext(), "Action Cancel2");
//                        return false;
//                    case (MotionEvent.ACTION_OUTSIDE):
//                        //Action_Outside貌似也不会触发
//                        Toaster.showShortToast(getBaseContext(), "Action Outside2");
//                        return false;
//                    default:
//                        return false;
//                }
//            }
//        });

        rlRoot = (MyRelativeLayout)findViewById(R.id.rl_root);
//        rlRoot.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("----event----->"+event.getAction());
////                float x = event.getX();
////                float y = event.getY();
////                tvTouchEventTest.animate().translationX(x).setDuration(10).start();
////                tvTouchEventTest.animate().translationY(y).setDuration(10).start();
//                return false;
//            }
//        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("----event2----->"+event.getAction());
//
//        mDetector.onTouchEvent(event);

//        final int action = MotionEventCompat.getActionMasked(event);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
//            }
//            case MotionEvent.ACTION_MOVE: {
//                int index = event.findPointerIndex(mActivePointerId);
//                float x = event.getX(index);
//                float y = event.getY(index);
//                float view_y = y - mActionBarSize - mStatusBarHeight;
//                System.out.println("----event2----->"+x + ":" + y);
//                tvTouchEventTest.setX(x);
//                tvTouchEventTest.setY(view_y);
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toaster.showShortToast(this, "onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toaster.showShortToast(this, "onDoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Toaster.showShortToast(this, "onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toaster.showShortToast(this, "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Toaster.showShortToast(this, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toaster.showShortToast(this, "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Toaster.showShortToast(this, "onScroll");
//        tvTouchEventTest.setX(e2.getX());
//        tvTouchEventTest.setY(e2.getY());
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toaster.showShortToast(this, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toaster.showShortToast(this, "onFling");
        return false;
    }

    /**
     * 实现简单的手势监听,可以按需复写
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }
    }
}
