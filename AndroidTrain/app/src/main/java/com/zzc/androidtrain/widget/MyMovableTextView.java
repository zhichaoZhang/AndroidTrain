package com.zzc.androidtrain.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * 可移动的TextView
 * <p/>
 * Created by zczhang on 16/5/20.
 */
public class MyMovableTextView extends TextView {
    private static final String TAG = "MyMovableTextView";
    private float mLastTouchX, mLastTouchY;
    //移动后新坐标
    private float mPosX;
    private float mPosY;

    private final int INVALID_POINTER_ID = -1;
    //当前活动的手指id
    private int mActivePointerId = INVALID_POINTER_ID;

    private int scaledSlop;


    public MyMovableTextView(Context context) {
        super(context);
        init(context);
    }

    public MyMovableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyMovableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyMovableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    void init(Context context) {
        ViewConfiguration view = ViewConfiguration.get(context);
        scaledSlop = view.getScaledEdgeSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                return true;
//                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

//                if(dx > scaledSlop || dy > scaledSlop) {
//                    mPosX += dx;
//                    mPosY += dy;
//
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
//                    layoutParams.leftMargin = (int) mPosX;
//                    layoutParams.topMargin = (int) mPosY;
//                    setLayoutParams(layoutParams);
//                    invalidate();
//                }

//                if (dx > 10 && dy > 10) {
//                    invalidate();
//                }

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        super.onDraw(canvas);
    }

    @Override
    public float getTranslationX() {
        return mPosX;
//        return super.getTranslationX();
    }

    @Override
    public float getTranslationY() {
        return mPosY;
//        return super.getTranslationY();
    }
}
