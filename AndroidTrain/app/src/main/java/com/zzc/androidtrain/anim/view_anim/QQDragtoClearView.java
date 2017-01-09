package com.zzc.androidtrain.anim.view_anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zzc.androidtrain.R;

/**
 * 仿qq消息拖拽
 * Created by zczhang on 17/1/6.
 */

public class QQDragtoClearView extends FrameLayout {
    /**
     * 最大拖拽长度
     */
    private final int DRAG_MAX_LEN = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
    /**
     * 默认的红点半径大小
     */
    private final int DEFAULT_RADIUS = 40;
    /**
     * 消息数量背景，QQ里面起始用的就是一个图片，而不是在红色背景上画一个数量
     */
    private ImageView mIvMsgCountBg;
    /**
     * 拖拽清除的时候的动画视图
     */
    private ImageView mIvClearAnim;

    /**
     * 手触摸的x，ｙ坐标
     */
    private float mTouchX;
    private float mTouchY;
    /**
     * 初始的x,y坐标，默认安放的位置
     */
    private float mStartX = 300;
    private float mStartY = 300;
    /**
     * 手触摸的坐标和初始点坐标的中间位置
     * mCenterX = (mTouchX + mStartX)/2
     * mCenterY = (mTouchY + mStartY )/2
     * 这个是用来画贝塞尔曲线的一个中转点
     */
    private float mCenterX;
    private float mCenterY;
    private static final int TOUCH_SLOP = 10;

    /**
     * 画贝塞尔曲线的画笔
     */
    private Paint mPaint;
    /**
     * 画贝塞尔曲线的Path
     */
    private Path mPath;

    /**
     * 是否被点中了
     */
    private boolean isTouch;

    /**
     * 默认的半径大小
     */
    private int mRaduis = DEFAULT_RADIUS;

    /**
     * 判断贝塞尔曲线是否断掉了
     */
    private boolean isBroken = false;

    public QQDragtoClearView(Context context) {
        this(context, null);
    }

    public QQDragtoClearView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public QQDragtoClearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, 60);
        mIvClearAnim = new ImageView(getContext());
        mIvClearAnim.setLayoutParams(params);
//        mIvClearAnim.setImageResource(R.drawable.tip_anim);
        mIvClearAnim.setVisibility(INVISIBLE);

        mIvMsgCountBg = new ImageView(getContext());
        mIvMsgCountBg.setLayoutParams(params);
        mIvMsgCountBg.setImageResource(R.drawable.shape_circle_red);
        mIvMsgCountBg.setVisibility(VISIBLE);

        addView(mIvClearAnim);
        addView(mIvMsgCountBg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = (int) event.getX();
        mTouchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //ACTION_DOWN的作用就是判断触摸的点
                Rect rect = new Rect();
                int[] location = new int[2];
                mIvMsgCountBg.getDrawingRect(rect);
                mIvMsgCountBg.getLocationOnScreen(location);
                rect.left = location[0];
                rect.top = location[1];
                rect.right = rect.right + location[0];
                rect.bottom = rect.bottom + location[1];
                if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    //如果isTouch为ture则表示开始绘制开始
                    isTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //当抬起的时候，先判断是否断开了，才会出发动画，并且消息数量提示消失
                //如果没有断开，则回到原点
                if (isBroken) {
                    boolean isPositionNoChanged = false;
                    //如果弹起位置跟起始位置相差不大，则回到起始位置，不消失
                    if (Math.abs(mTouchX - mStartX) < TOUCH_SLOP && Math.abs(mTouchY - mStartY) < TOUCH_SLOP) {
                        isPositionNoChanged = true;
                    }

                    if (isPositionNoChanged) {
                        break;
                    } else {
                        mIvMsgCountBg.setVisibility(INVISIBLE);
                        mIvClearAnim.setVisibility(View.VISIBLE);
                        mIvClearAnim.setX(mTouchX - mIvClearAnim.getWidth() / 2);
                        mIvClearAnim.setY(mTouchY - mIvClearAnim.getHeight() / 2);
//                        mIvClearAnim.setImageResource(R.drawable.tip_anim);

                        ((AnimationDrawable) mIvClearAnim.getDrawable()).stop();
                        ((AnimationDrawable) mIvClearAnim.getDrawable()).start();
                        mPath.reset();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIvMsgCountBg.setVisibility(VISIBLE);
                            }
                        }, 1200);
                    }
                    isBroken = false;
                }
                isTouch = false;
                mIvMsgCountBg.setX(mStartX - mIvMsgCountBg.getWidth() / 2);
                mIvMsgCountBg.setY(mStartY - mIvMsgCountBg.getHeight() / 2);
                break;
        }

        mCenterX = (mTouchX + mStartX) / 2;
        mCenterY = (mTouchY + mStartY) / 2;

        if (isTouch) {
            mIvMsgCountBg.setX(mTouchX - mIvMsgCountBg.getWidth() / 2);
            mIvMsgCountBg.setY(mTouchY - mIvMsgCountBg.getHeight() / 2);
        }
        invalidateView();
        return true;
    }

    public void invalidateView() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mIvClearAnim.setX(mStartX);
        mIvClearAnim.setY(mStartY);
        mIvMsgCountBg.setX(mStartX - mIvMsgCountBg.getWidth() / 2);
        mIvMsgCountBg.setY(mStartY - mIvMsgCountBg.getHeight() / 2);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果在touch中，且线没有断掉，则继续绘制,会泽清楚画布.
        if (isTouch && !isBroken) {
            checkDragLen();
            canvas.drawPath(mPath, mPaint);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
            canvas.drawCircle(mStartX, mStartY, mRaduis, mPaint);
            canvas.drawCircle(mTouchX, mTouchY, mRaduis, mPaint);
        } else {
            //相当于清楚绘制信息
            canvas.drawCircle(mStartX, mStartY, 0, mPaint);
            canvas.drawCircle(mTouchX, mTouchY, 0, mPaint);
            canvas.drawLine(0, 0, 0, 0, mPaint);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        }
    }

    /**
     * 检查拖拽长度是不是够了，超过一定长度就断开，不绘制了
     */
    private void checkDragLen() {
        int len = (int) Math.sqrt(Math.pow(mTouchX - mStartX, 2) + Math.pow(mTouchY - mStartY, 2));
        mRaduis = -len / 15 + DEFAULT_RADIUS;
        //如果长度超过最大长度,isBroken设置为true，在后续就不绘制path了
        if (len > DRAG_MAX_LEN) {
            isBroken = true;
            return;
        }

        //得到绘制贝塞尔曲线需要的四个点
        float offsetX = (float) (mRaduis * Math.sin(Math.atan((mTouchY - mStartY) / (mTouchX - mStartX))));
        float offsetY = (float) (mRaduis * Math.cos(Math.atan((mTouchY - mStartY) / (mTouchX - mStartX))));

        float x1 = mStartX - offsetX;
        float y1 = mStartY + offsetY;

        float x2 = mTouchX - offsetX;
        float y2 = mTouchY + offsetY;

        float x3 = mTouchX + offsetX;
        float y3 = mTouchY - offsetY;

        float x4 = mStartX + offsetX;
        float y4 = mStartY - offsetY;

        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.quadTo(mCenterX, mCenterY, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(mCenterX, mCenterY, x4, y4);
        mPath.lineTo(x1, y1);
    }
}
