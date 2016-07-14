package com.zzc.androidtrain.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

/**
 * Created by zczhang on 16/5/23.
 */
public class MyScaleTextView extends TextView{
    private static final String TAG = "MyScaleTextView";
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    public MyScaleTextView(Context context) {
        super(context);
        init(context);
    }

    public MyScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScaleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyScaleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        Log.d(TAG, "init: ");
        System.out.println("-------init------");
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: "+event.getAction());
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }
}
