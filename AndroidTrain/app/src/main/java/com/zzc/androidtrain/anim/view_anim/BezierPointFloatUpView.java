package com.zzc.androidtrain.anim.view_anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 利用Bezier曲线实现的圆点浮现效果视图
 * 默认在圆点位置在视图中央
 * <p>
 * Created by zczhang on 17/1/16.
 */

public class BezierPointFloatUpView extends View {
    private static final float FULL_NORMALIZED_TIME = 1F;
    private static final float ZERO_NORMALIZED_TIME = 0F;

    //水平线旋转角度
    private static final float ANGLE_WATER_LINE = 45f;

    private Context mContext;
    private float mCircleRadius;//圆的半径
    private Paint mCirclePaint;//圆的画笔
    private Point mCircleCenterPoint;//圆心

    private ValueAnimator mDropOutAnimator;//出现动画
    private Interpolator mDropOutInterpolator = new LinearInterpolator();//差值器
    private int mDropOutDuration = 5 * 1000;//出现动画时长
    private DropCircle mDropCircle;


    public BezierPointFloatUpView(Context context) {
        this(context, null);
    }

    public BezierPointFloatUpView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BezierPointFloatUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        this.mCirclePaint = new Paint();
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeWidth(dip2dx(context, 2));
        mCirclePaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initCircle(w, h);
        initAnim(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(0, getHeight() - mCircleRadius, getWidth(), getHeight() - mCircleRadius, mCirclePaint);
//        canvas.drawCircle(mDropCircle.circleCenterPoint.x, mDropCircle.circleCenterPoint.y, mDropCircle.circleRadius, mCirclePaint);
    }

    public void performAnim() {
        mDropOutAnimator.removeAllUpdateListeners();
        mDropOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mDropOutAnimator.start();
    }

    public void stopAnim() {
        if (mDropOutAnimator != null) {
            mDropOutAnimator.cancel();
        }
    }


    private void initAnim(int viewWidth, int viewHeight) {
        float circleRadius = mDropCircle.circleRadius;
        //设置动画执行的差值为，当前移动了多少倍的圆点直径
        float endValue = viewHeight / (circleRadius * 2);
        mDropOutAnimator = ValueAnimator.ofFloat(0, endValue);
        mDropOutAnimator.setInterpolator(mDropOutInterpolator);
        mDropOutAnimator.setDuration(mDropOutDuration);
    }

    /**
     * 初始化圆点相关属性
     *
     * @param viewWidth  视图宽度
     * @param viewHeight 视图高度
     */
    private void initCircle(int viewWidth, int viewHeight) {
        if (mDropCircle == null) {
            mDropCircle = new DropCircle();
        }
        //初始化半径和圆心点
//        mDropCircle.circleRadius = circleRadius;
//        mDropCircle.circleCenterPoint = circleCenter;
        //初始化贝塞尔6个点
//        mDropCircle.leftWaterPoint.y = viewHeight;
//        mDropCircle.leftWaterPoint.x =

    }


    private float dip2dx(Context context, float value) {
        return (getScreenDensity(context) * value + 0.5f);
    }

    private float getScreenDensity(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.density;
        } catch (Exception e) {
            return context.getResources().getDisplayMetrics().density;
        }
    }

    private class PointWithAngle {
        public int x;
        public int y;
        public float tan;
    }

    /**
     * 带拖尾效果的圆
     */
    private class DropCircle {
        //未完全脱离状态，圆点和拖尾一起移动
        public static final int STATE_LEAVE = 0;
        //已完全脱离，独自移动
        public static final int STATE_MOVE = 1;
        //半径
        public float circleRadius;
        //圆心点
        public PointWithAngle circleCenterPoint;
        //贝塞尔6个点
        public PointWithAngle leftWaterPoint;
        public PointWithAngle leftControlPoint;
        public PointWithAngle leftDropPoint;

        public PointWithAngle rightWaterPoint;
        public PointWithAngle rightControlPoint;
        public PointWithAngle rightDropPoint;

        //水平线上两个点的终点
        public PointWithAngle mMiddleOfWaterPoints;
        //拖拽路径
        public Path dropPath;
        //开始正常移动的时间点，也就是圆点向上移动的距离为一个圆点直径的距离
        public float mNormalizedTime;
        public int mState;

        public DropCircle() {
            dropPath = new Path();
            initPoint();
        }

        private void initPoint() {
            if (leftWaterPoint == null) {
                leftWaterPoint = new PointWithAngle();
            }

            if (leftControlPoint == null) {
                leftControlPoint = new PointWithAngle();
            }

            if (leftDropPoint == null) {
                leftDropPoint = new PointWithAngle();
            }

            if (rightWaterPoint == null) {
                rightWaterPoint = new PointWithAngle();
            }

            if (rightControlPoint == null) {
                rightControlPoint = new PointWithAngle();
            }

            if (rightDropPoint == null) {
                rightDropPoint = new PointWithAngle();
            }

            if(mMiddleOfWaterPoints == null) {
                mMiddleOfWaterPoints = new PointWithAngle();
            }
        }
    }
}
