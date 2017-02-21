package com.zzc.androidtrain.anim.view_anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 消息点动画
 * <p>
 * 由左下角移动到右上角
 * <p>
 * Created by zczhang on 17/1/6.
 */

public class NearMsgView extends View {
    private static final String TAG = "NearMsgView";
    private static final double PIE = 3.1415936;
    private static final int DEFAULT_CIRCLE_RADIUS = 7;//默认圆圈半径 dp
    private static final int DEFAULT_TEXT_SIZE = 7;//默认字号sp
    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;//默认圆圈背景色
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;//默认文件颜色
    private static final int DEFAULT_START_CIRCLE_RADIUS = 3;//默认起始位置圆圈半径 dp
    private static final int DEFAULT_ANIM_DURATION = 7000;//默认动画执行时长 ms
    private static final Interpolator DEFAULT_ANIM_INTER = new LinearInterpolator();//默认动画执行的差值器
    private Context mContext = null;
    private int mFinalCircleRadius;//最终圆点半径
    private int mOriginCircleRadius;//起始点半径
    private int mTextSize;//字号
    private int mTextColor;//文字颜色
    private int mCircleColor;//圆圈颜色
    private int mAnimDuration;//动画时长
    private String mText;//文本
    private Interpolator mAnimInterpolator;//动画差值器
    private ValueAnimator mTransitionAnimator = null;//位移动画
    private boolean isShown = false;//是否已经显示

    private Point mStartPoint = new Point();//起点
    private Point mEndPoint = new Point();//终点
    private Point mMidPoint = new Point();//中间点

    private Paint mPaint = null;//画圆和画曲线的画笔
    private Paint mTextPaint = null;//画数字的画笔
    private Path mBezierPath = null;//整个粘连区域
    private int mCircleRadius;//动画过程中的圆半径

    public NearMsgView(Context context) {
        this(context, null);
    }

    public NearMsgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NearMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        Log.d(TAG, "initView: ");
        this.mContext = context;
        this.mCircleColor = DEFAULT_CIRCLE_COLOR;
        this.mFinalCircleRadius = dip2Px(context, DEFAULT_CIRCLE_RADIUS);
        this.mOriginCircleRadius = dip2Px(context, DEFAULT_START_CIRCLE_RADIUS);
        this.mTextSize = DEFAULT_TEXT_SIZE;
        this.mTextColor = DEFAULT_TEXT_COLOR;
        this.mAnimDuration = DEFAULT_ANIM_DURATION;
        this.mAnimInterpolator = DEFAULT_ANIM_INTER;
        mPaint = new Paint();
        mPaint.setColor(mCircleColor);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setDither(true);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mBezierPath = new Path();
    }

    private int dip2Px(Context context, int dpValue) {
        if (context == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dpValue + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: ");
        super.onSizeChanged(w, h, oldw, oldh);
        int left = getLeft();
        int bottom = getBottom();
        int top = getTop();
        int right = getRight();
        Log.i(TAG, "onSizeChanged: left = " + left + "; bottom = " + bottom + "; top = " + top + "; right = " + right);
        mStartPoint.x = mFinalCircleRadius;
        mStartPoint.y = bottom - top - mFinalCircleRadius;
        mEndPoint.x = right - left - mFinalCircleRadius;
        mEndPoint.y = mFinalCircleRadius;
        mTransitionAnimator = ValueAnimator.ofInt(mOriginCircleRadius, mFinalCircleRadius)
                .setDuration(mAnimDuration);
        mTransitionAnimator.setInterpolator(mAnimInterpolator);
        Log.i(TAG, "onSizeChanged: startPoint = " + mStartPoint.toString());
        Log.i(TAG, "onSizeChanged: endPoint = " + mEndPoint.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (mFinalCircleRadius != 0) {
//            canvas.drawCircle(mStartPoint.x, mStartPoint.y, mFinalCircleRadius, mPaint);
//        }
//        if (mCircleRadius != 0 && mMidPoint.x != 0 && mMidPoint.y != 0) {
//            canvas.drawCircle(mMidPoint.x, mMidPoint.y, mCircleRadius, mPaint);
//        }
        canvas.drawPath(mBezierPath, mPaint);
//        paintText(canvas);
    }

    //画数字
    private void paintText(Canvas canvas) {
        if (mTextPaint != null && !TextUtils.isEmpty(mText)) {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float baseLineY = mMidPoint.y - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            canvas.drawText(mText, mMidPoint.x, baseLineY, mTextPaint);
        }
    }

    /**
     * 设置要显示的文本
     *
     * @param text 文本
     */
    public void setText(String text) {
        this.mText = text;
    }

    /**
     * 获取正在显示的文本内容
     *
     * @return 文本
     */
    public String getText() {
        return mText;
    }

    /**
     * 刷新文本内容
     *
     * @param text 文本
     */
    public void refreshText(String text) {
        setText(text);
        refreshSelf();
    }

    /**
     * 显示此视图
     */
    public void show() {
        startShowAnim();
    }


    /**
     * 是否已经显示
     *
     * @return isShown
     */
    public boolean isShown() {
        return isShown;
    }

    private void startShowAnim() {
        if (mTransitionAnimator != null) {
            mTransitionAnimator.cancel();
            clearBezierPath();
            mTransitionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int tempRadius = (int) animation.getAnimatedValue();
                    float animFaction = animation.getAnimatedFraction();
                    NearMsgView.this.updateCircleProperty(tempRadius, animFaction);
                    NearMsgView.this.updateTextSize(animFaction);
                    NearMsgView.this.refreshSelf();
                }
            });
            mTransitionAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearBezierPath();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mTransitionAnimator.start();
        }
    }

    //更新字号
    private void updateTextSize(float animFaction) {
        if (mTextPaint != null) {
            int textSize = (int) (mTextSize * animFaction);
            mTextPaint.setTextSize(dip2Px(mContext, textSize));
        }
    }

    private void refreshSelf() {
        invalidate();
    }

    //更新圆圈属性，包括半径，横纵坐标，实现放大和位移动画
    private void updateCircleProperty(int radius, float faction) {
        mCircleRadius = radius;
        int startX = mStartPoint.x;
        int startY = mStartPoint.y;
        int endX = mEndPoint.x;
        int endY = mEndPoint.y;
        mMidPoint.x = (int) (startX + faction * (endX - startX));
        mMidPoint.y = (int) (startY + faction * (endY - startY));
        calculateBezierPath(radius, mMidPoint);
    }

    //计算上下两条曲线
    private void calculateBezierPath(int radius, Point midPoint) {
        //起点
        Point upPointStart = new Point();
        Point downPointStart = new Point();
        double originCos45Len = mFinalCircleRadius * Math.cos(toRadians(45));
        upPointStart.x = (int) (mStartPoint.x - originCos45Len);
        upPointStart.y = (int) (mStartPoint.y - originCos45Len);
        downPointStart.x = (int) (mStartPoint.x + originCos45Len);
        downPointStart.y = (int) (mStartPoint.y + originCos45Len);

        //终点
        Point upPointEnd = new Point();
        Point downPointEnd = new Point();
        double endCos45Len = (mFinalCircleRadius - radius) * Math.cos(toRadians(45));
        upPointEnd.x = (int) (midPoint.x - endCos45Len);
        upPointEnd.y = (int) (midPoint.y - endCos45Len);
        downPointEnd.x = (int) (midPoint.x + endCos45Len);
        downPointEnd.y = (int) (midPoint.y + endCos45Len);
        Log.d(TAG, "calculateBezierPath: upPointEnd = " + upPointEnd);
        Log.d(TAG, "calculateBezierPath: downPointEnd = " + downPointEnd);

        //当两个终点相当接近时，粘滞部分与移动的圆脱离
        if (Math.abs(downPointEnd.x - upPointEnd.x) < 7) {
            return;
        } else {
            clearBezierPath();
        }

        //控制点 取两个圆的圆心连线的中点
        Point controlPoint = new Point();
        controlPoint.x = mStartPoint.x + (midPoint.x - mStartPoint.x) / 2;
        controlPoint.y = mStartPoint.y + (midPoint.y - mStartPoint.y) / 2;

        mBezierPath.moveTo(upPointStart.x, upPointStart.y);
        mBezierPath.quadTo(controlPoint.x, controlPoint.y, upPointEnd.x, upPointEnd.y);

//        mBezierPath.lineTo(downPointEnd.x, downPointEnd.y);
        contactDoubleEndPoint(mBezierPath, upPointEnd, downPointEnd);

        mBezierPath.quadTo(controlPoint.x, controlPoint.y, downPointStart.x, downPointStart.y);

        mBezierPath.close();
    }

    private void contactDoubleEndPoint(Path mBezierPath, Point upPointEnd, Point downPointEnd) {
//        RectF arcRect = new RectF(upPointEnd.x, upPointEnd.y, downPointEnd.x, downPointEnd.y);
//        mBezierPath.arcTo(arcRect, -135f, 45f);
//        mBezierPath.addCircle();

        mBezierPath.lineTo(downPointEnd.x, downPointEnd.y);
    }

    /**
     * 角度转换成弧度
     *
     * @param angle 角度值
     * @return 对应弧度制
     */
    private double toRadians(double angle) {
        return angle * PIE / 180f;
    }

    private void clearBezierPath() {
        mBezierPath.reset();
    }

    public void hide() {
        clearBezierPath();
        mMidPoint.set(0, 0);
        refreshSelf();
    }
}