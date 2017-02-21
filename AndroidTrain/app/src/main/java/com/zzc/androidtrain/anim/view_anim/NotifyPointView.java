package com.zzc.androidtrain.anim.view_anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.zzc.androidtrain.R;

/**
 * 角标通知视图
 * <p>
 * Created by zczhang on 17/1/18.
 */

public class NotifyPointView extends View {
    private static final String TAG = "NotifyPointView";
    private static final int DEFAULT_CIRCLE_RADIUS = 7;//默认圆圈半径 dp
    private static final int DEFAULT_TEXT_SIZE = 7;//默认字号sp
    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;//默认圆圈背景色
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;//默认文本颜色
    private static final int DEFAULT_WATER_ANIMATION_DURATION = 1500;
    private static final float FULL_NORMALIZED_TIME = 1.5F;
    // water drop
    private static final float WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS = 3F;
    private static final float WATER_DROP_UP_WATER_DISTANCE_TO_DROP_RADIUS = 2F;
    private static final float WATER_DROP_BROKEN_UP_TO_WATER_DISTANCE_TO_DROP_RADIUS = 1F;
    private static final float WATER_DROP_BROKEN_REMOVE_TAIL_TO_WATER_DISTANCE_TO_DROP_RADIUS = 0.5F;
    private static final float WATER_DROP_UNDER_WATER_DISTANCE_TO_DROP_RADIUS =
            WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS - WATER_DROP_UP_WATER_DISTANCE_TO_DROP_RADIUS;
//    private static final float WATER_MOVE_SEASON_1 = (WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS -
//            WATER_DROP_BROKEN_UP_TO_WATER_DISTANCE_TO_DROP_RADIUS) / WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS;
//    private static final float WATER_MOVE_SEASON_2 =
//            (WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS
//                    - WATER_DROP_BROKEN_REMOVE_TAIL_TO_WATER_DISTANCE_TO_DROP_RADIUS)
//                    / WATER_DROP_LEAVE_WATER_TOTAL_DISTANCE_TO_DROP_RADIUS;

    private static final float WATER_MOVE_SEASON_1 = 0.8f;

    private static final float WATER_MOVE_SEASON_2 = 1.3f;

    private static final float WATER_MOVE_SEASON_3 = FULL_NORMALIZED_TIME;

    private static final float WATER_POINTS_MAX_INTERVAL_TO_DROP_RADIUS = 2.2F;
    private static final float WATER_POINTS_MIN_INTERVAL_TO_DROP_RADIUS = 1.1F;

    private static final int ERROR_OF_FIT_WATER_FUNC = 1;

    // general
    private static final int FULL_ANGLE = 360;
    private static final int ZERO_ANGLE = 0;
    private static final int HALF_FULL_ANGLE = FULL_ANGLE / 2;
    private static final int QUAR_FULL_ANGLE = FULL_ANGLE / 4;

    private static final float MAX_DIS_TO_CIRCLE_TOP_RATIO = 2f;

    private static final int MIN_DRAW_ANGLE = 1;

    // view
    private RectF mViewRectF;
    // 圆点画笔
    private Paint mWaterPaint;
    //圆点对象
    private WaterDrop mPointDrop;
    //圆点出现动画
    private ValueAnimator mFlowWaterUpAnimator;
    //圆点出现动画时长
    private int mAnimDuration;
    //圆点半径
    private int mPointRadius;
    //圆点颜色
    private int mPointColor;
    //文本画笔
    private Paint mTextPaint;
    //文本内容
    private String mText = "";
    //文本颜色
    private int mTextColor;
    //文本字号
    private int mTextSize;
    //视图偏移量
    private float mInitTranX;
    //抖动动画
    private Animator mShakeAnim;
    //旋转角度
    private float mRotate;

    public NotifyPointView(Context context) {
        this(context, null);
    }

    public NotifyPointView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NotifyPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mViewRectF = new RectF();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotifyPointView);
        if (typedArray != null) {
            this.mPointColor = typedArray.getColor(R.styleable.NotifyPointView_npv_point_color, DEFAULT_CIRCLE_COLOR);
            this.mPointRadius = typedArray.getDimensionPixelSize(R.styleable.NotifyPointView_npv_point_radius, dipToPx(context,
                    DEFAULT_CIRCLE_RADIUS));
            this.mTextSize = typedArray.getDimensionPixelSize(R.styleable.NotifyPointView_npv_text_size, dipToPx(context, DEFAULT_TEXT_SIZE));
            this.mTextColor = typedArray.getColor(R.styleable.NotifyPointView_npv_text_color, DEFAULT_TEXT_COLOR);
            this.mAnimDuration = typedArray.getInt(R.styleable.NotifyPointView_npv_anim_duration, DEFAULT_WATER_ANIMATION_DURATION);
            this.mRotate = typedArray.getFloat(R.styleable.NotifyPointView_npv_rotate, 0);

            typedArray.recycle();
        }

        mWaterPaint = new Paint();
        mWaterPaint.setColor(mPointColor);
        mWaterPaint.setAntiAlias(true);
        mWaterPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setDither(true);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        initShakeAnim();
    }

    private void initShakeAnim() {
        mInitTranX = getTranslationX();
        mShakeAnim = ObjectAnimator.ofFloat(this, "translationX", mInitTranX + 4f, mInitTranX - 4f, mInitTranX + 4f, mInitTranX - 4f, mInitTranX)
                .setDuration(500);
        mShakeAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewRectF.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateWaterDrop();
        if (mPointDrop != null && mPointDrop.mDropPath != null && mWaterPaint != null) {
            canvas.drawPath(mPointDrop.mDropPath, mWaterPaint);
        }

        paintText(canvas);
    }

    //画数字
    private void paintText(Canvas canvas) {
        if (mTextPaint != null && !TextUtils.isEmpty(mText)) {
            canvas.save();
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float baseLineY = mPointDrop.mDropCircleRectF.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            float textX = mPointDrop.dropX;

            canvas.rotate(mRotate, mPointDrop.dropX, mPointDrop.mDropCircleRectF.centerY());

            canvas.drawText(mText, textX, baseLineY, mTextPaint);
            canvas.restore();
        }
    }

    private int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    private float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    /**
     * 开启动画
     */
    public void show() {
        show("");
    }


    public void show(String text) {
        this.mText = text;
        stopAnim();

        performWaterDropAnimation();
    }

    /**
     * 隐藏
     */
    public void hide() {
        this.mText = "";
        this.mPointDrop.mDropPath.reset();
        refreshSelf();
    }

    public void setColor(int color) {
        this.mPointColor = color;
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public void setTextSize(int size) {
        this.mTextSize = size;
    }

    public void setAnimDuration(int duration) {
        this.mAnimDuration = duration;
    }

    public void setPointRadius(int pointRadius) {
        this.mPointRadius = pointRadius;
    }

    /**
     * 刷新文本显示
     *
     * @param text
     */
    public void refreshText(String text) {
        this.mText = text;
        refreshSelf();
    }

    private void refreshSelf() {
        invalidate();
    }

    private void performWaterDropAnimation() {
        performOneWaterDropAnimation(0.5f, 0.2f, mAnimDuration);
    }

    /**
     * perform one water drop animation
     *
     * @param dropCenterXRatio 圆心横坐标
     * @param dropRadiusRatio  半径比例
     * @param totalTime        总时间
     */
    private void performOneWaterDropAnimation(final float dropCenterXRatio, final float dropRadiusRatio, final int totalTime) {
        if (mPointDrop == null) {
            mPointDrop = new WaterDrop();
        }

        if (mPointRadius == 0) {
            mPointRadius = (int) (getWidth() * dropRadiusRatio);
        }
        mPointDrop.dropRadius = mPointRadius;
        mPointDrop.dropX = (int) (dropCenterXRatio * getWidth());

        float endValue = getHeight() / (mPointDrop.dropRadius * 2f);
//        Log.d(TAG, "performOneWaterDropAnimation: endValue = " + endValue);
//        Log.d(TAG, "performOneWaterDropAnimation: mPointDrop.dropX = " + mPointDrop.dropX);
//        Log.d(TAG, "performOneWaterDropAnimation: mPointDrop.dropRadius = " + mPointDrop.dropRadius);
//        Log.d(TAG, "performOneWaterDropAnimation: getHeight() = " + getHeight());
        mFlowWaterUpAnimator = ValueAnimator.ofFloat(0, endValue);
        mFlowWaterUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointDrop.mNormalizedTime = (float) animation.getAnimatedValue();
                if (mPointDrop.mNormalizedTime > FULL_NORMALIZED_TIME) {
                    mPointDrop.mState = WaterDrop.STATE_MOVE;
                } else {
                    mPointDrop.mState = WaterDrop.STATE_LEAVE_WATER;
                }
                refreshSelf();
            }
        });
        mFlowWaterUpAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startShakeAnim();
            }
        });
        mFlowWaterUpAnimator.setInterpolator(new AccelerateInterpolator());
        mFlowWaterUpAnimator.setDuration(totalTime);

        mFlowWaterUpAnimator.start();
    }

    /**
     * 位移动画结束后，抖动一下
     */
    private void startShakeAnim() {
        if (mShakeAnim != null) {
            mShakeAnim.start();
        }
    }


    /**
     * 结束动画
     */
    public void stopAnim() {
        if (mFlowWaterUpAnimator != null) {
            mFlowWaterUpAnimator.cancel();
        }
    }


    private void updateWaterDrop() {
        if (mPointDrop == null) {
            return;
        }

        float dropNormalizeTime = mPointDrop.mNormalizedTime;

        if (mPointDrop.mDropCircleRectF == null) {
            mPointDrop.mDropCircleRectF = new RectF();
        }

        int halfWaterPointInterval = 0;
        // calculate interval distance between two water points
        if (dropNormalizeTime <= WATER_MOVE_SEASON_1) {
            float adjustNormalizeTime = dropNormalizeTime / WATER_MOVE_SEASON_1;
            halfWaterPointInterval = (int) (mPointDrop.dropRadius * (adjustNormalizeTime * (WATER_POINTS_MIN_INTERVAL_TO_DROP_RADIUS -
                    WATER_POINTS_MAX_INTERVAL_TO_DROP_RADIUS) + WATER_POINTS_MAX_INTERVAL_TO_DROP_RADIUS));
        } else {
            halfWaterPointInterval = (int) (mPointDrop.dropRadius
                    * WATER_POINTS_MIN_INTERVAL_TO_DROP_RADIUS);
        }

        mPointDrop.mLeftWaterPoint.x = mPointDrop.dropX - halfWaterPointInterval;
        mPointDrop.mRightWaterPoint.x = mPointDrop.dropX + halfWaterPointInterval;

        // Calculate waterPoints and angles
        float leftTime = calculateTbyX(0, 0,
                0, getWidth(), mPointDrop.mLeftWaterPoint.x);
        mPointDrop.mLeftWaterPoint.y = (int) getPointOnWater(getHeight(),
                getHeight(), getHeight(), getHeight(), leftTime);
        float rightTime = calculateTbyX(0, 0,
                0, getWidth(), mPointDrop.mRightWaterPoint.x);
        mPointDrop.mRightWaterPoint.y =
                (int) getPointOnWater(getHeight(), getHeight(),
                        getHeight(), getHeight(), rightTime);

        // Calculate the intersection of the vertical line of the circle and the water surface
        float middleTime = calculateTbyX(0, 0, 0, getWidth(), mPointDrop.mInterDropAndWater.x);
        mPointDrop.mInterDropAndWater.x = mPointDrop.dropX;
        mPointDrop.mInterDropAndWater.y = (int) getPointOnWater(getHeight(),
                getHeight(), getHeight(), getHeight(), middleTime);


        int detDisOfCircleCenter = (int) (dropNormalizeTime
                * mPointDrop.dropRadius * 2);


        int circleCenY = (int) (mPointDrop.mInterDropAndWater.y - (detDisOfCircleCenter - mPointDrop.dropRadius *
                WATER_DROP_UNDER_WATER_DISTANCE_TO_DROP_RADIUS));
//        Log.d(TAG, "updateWaterDrop: circleCenY= " + circleCenY);
        int circleCenX = mPointDrop.dropX;

        mPointDrop.mDropCircleRectF.set(circleCenX - mPointDrop.dropRadius, circleCenY - mPointDrop.dropRadius,
                circleCenX + mPointDrop.dropRadius, circleCenY + mPointDrop.dropRadius);

        // State is move up and down beyond water, just draw the water drop
        if (mPointDrop.mState == WaterDrop.STATE_MOVE) {
            mPointDrop.mDropPath.reset();
            mPointDrop.mDropPath.addArc(mPointDrop.mDropCircleRectF, ZERO_ANGLE, FULL_ANGLE);
            return;
        }

        // Calculate the middle point of water points
        mPointDrop.mMiddleOfWaterPoints.x = (mPointDrop.mLeftWaterPoint.x + mPointDrop.mRightWaterPoint.x) / 2;
        mPointDrop.mMiddleOfWaterPoints.y = (mPointDrop.mLeftWaterPoint.y + mPointDrop.mRightWaterPoint.y) / 2;
        mPointDrop.mMiddleOfWaterPoints.tan =
                (float) (mPointDrop.mLeftWaterPoint.y - mPointDrop.mRightWaterPoint.y)
                        / (mPointDrop.mLeftWaterPoint.x - mPointDrop.mRightWaterPoint.x);

        // Calculate cycle points
        int disToCircleTop = (int) Math.min(Math.max((mPointDrop.mInterDropAndWater.y - mPointDrop.mDropCircleRectF.top) / 2 * 1.5f, 0),
                mPointDrop.dropRadius * MAX_DIS_TO_CIRCLE_TOP_RATIO);

        int angleTmp = (int) (Math.toDegrees(Math.acos(
                (float) (mPointDrop.dropRadius - disToCircleTop) / mPointDrop.dropRadius)));
        int angleOfRightRadiusToHor =
                (int) (QUAR_FULL_ANGLE - Math.toDegrees(Math.atan(mPointDrop.mMiddleOfWaterPoints.tan)) - angleTmp);
        int angleOfLeftRadiusToHor = HALF_FULL_ANGLE - angleTmp * 2 - angleOfRightRadiusToHor;

        mPointDrop.mRightDropPoint.x =
                (int) (circleCenX + Math.cos(Math.toRadians(angleOfRightRadiusToHor)) * mPointDrop.dropRadius);
        mPointDrop.mRightDropPoint.y =
                (int) (circleCenY - Math.sin(Math.toRadians(angleOfRightRadiusToHor)) * mPointDrop.dropRadius);
        mPointDrop.mRightDropPoint.tan =
                (float) Math.tan(Math.toRadians(QUAR_FULL_ANGLE - angleOfRightRadiusToHor));


        mPointDrop.mLeftDropPoint.x =
                (int) (circleCenX - Math.cos(Math.toRadians(angleOfLeftRadiusToHor)) * mPointDrop.dropRadius);
        mPointDrop.mLeftDropPoint.y =
                (int) (circleCenY - Math.sin(Math.toRadians(angleOfLeftRadiusToHor)) * mPointDrop.dropRadius);
        mPointDrop.mLeftDropPoint.tan =
                (float) Math.tan(Math.toRadians(HALF_FULL_ANGLE - (QUAR_FULL_ANGLE - angleOfLeftRadiusToHor)));

        // Calculate control points
        // control point is the intersection of the tangent to the point on water point and the tangent to the point on drop
        // l_w: y - y_w = k_w * (x - x_w) -> y = y_w + k_w * (x - x_w)
        // l_d: y - y_d = k_d * (x - x_d) -> y = y_d + k_d * (x - x_d)
        // so, y_w + k_w * (x - x_w) = y_d + k_d * (x - x_d)
        // x = (y_w - y_d - k_w * x_w + k_d * x_d) / (k_d - k_w)
        float k_w1 = mPointDrop.mLeftWaterPoint.tan;
        float k_d1 = mPointDrop.mLeftDropPoint.tan;
        mPointDrop.mLeftControlPoint.x =
                (int) ((mPointDrop.mLeftWaterPoint.y - mPointDrop.mLeftDropPoint.y
                        - k_w1 * mPointDrop.mLeftWaterPoint.x + k_d1 * mPointDrop.mLeftDropPoint.x) / (k_d1 - k_w1));
        adjustPointBByPointA(mPointDrop.mLeftWaterPoint, mPointDrop.mLeftControlPoint, false, 0);
        mPointDrop.mLeftControlPoint.y =
                (int) (mPointDrop.mLeftWaterPoint.y + k_w1
                        * (mPointDrop.mLeftControlPoint.x - mPointDrop.mLeftWaterPoint.x));

        float k_w2 = mPointDrop.mRightWaterPoint.tan;
        float k_d2 = mPointDrop.mRightDropPoint.tan;
        mPointDrop.mRightControlPoint.x =
                (int) ((mPointDrop.mRightWaterPoint.y - mPointDrop.mRightDropPoint.y
                        - k_w2 * mPointDrop.mRightWaterPoint.x + k_d2 * mPointDrop.mRightDropPoint.x) / (k_d2 - k_w2));
        adjustPointBByPointA(mPointDrop.mRightWaterPoint, mPointDrop.mRightControlPoint, true, 0);
        mPointDrop.mRightControlPoint.y =
                (int) (mPointDrop.mRightWaterPoint.y
                        + k_w2 * (mPointDrop.mRightControlPoint.x - mPointDrop.mRightWaterPoint.x));


        // Generate water drop path
        if (mPointDrop.mDropPath == null) {
            mPointDrop.mDropPath = new Path();
        } else {
            mPointDrop.mDropPath.reset();
        }

        if (!mViewRectF.contains(mPointDrop.mLeftControlPoint.x, mPointDrop.mLeftControlPoint.y)) {
            mPointDrop.mLeftControlPoint.x = (mPointDrop.mLeftWaterPoint.x + mPointDrop.mLeftDropPoint.x) / 2;
            mPointDrop.mLeftControlPoint.y = (mPointDrop.mLeftWaterPoint.y + mPointDrop.mLeftDropPoint.y) / 2;
        }

        if (!mViewRectF.contains(mPointDrop.mRightControlPoint.x, mPointDrop.mRightControlPoint.y)) {
            mPointDrop.mRightControlPoint.x = (mPointDrop.mRightWaterPoint.x + mPointDrop.mRightDropPoint.x) / 2;
            mPointDrop.mRightControlPoint.y = (mPointDrop.mRightWaterPoint.y + mPointDrop.mRightDropPoint.y) / 2;
        }

        // adjust control point
        if (mPointDrop.mRightControlPoint.x - mPointDrop.mLeftControlPoint.x <= 0) {
            mPointDrop.mRightControlPoint.x =
                    (mPointDrop.mRightControlPoint.x + mPointDrop.mLeftControlPoint.x) / 2;
            mPointDrop.mLeftControlPoint.x =
                    mPointDrop.mRightControlPoint.x;
            mPointDrop.mRightControlPoint.y =
                    (mPointDrop.mRightControlPoint.y + mPointDrop.mLeftControlPoint.y) / 2;
            mPointDrop.mLeftControlPoint.y =
                    mPointDrop.mRightControlPoint.y;
        }

        if (dropNormalizeTime >= WATER_MOVE_SEASON_2) {
            float adjustSeason2Time =
                    (dropNormalizeTime - WATER_MOVE_SEASON_2) / (WATER_MOVE_SEASON_3 - WATER_MOVE_SEASON_2);
            int topX = (mPointDrop.mLeftDropPoint.x + mPointDrop.mRightDropPoint.x) / 2;
            int detDis = (int) (-WATER_DROP_BROKEN_REMOVE_TAIL_TO_WATER_DISTANCE_TO_DROP_RADIUS
                    * mPointDrop.dropRadius * (1f - adjustSeason2Time));
            int topY = Math.max(mPointDrop.mInterDropAndWater.y + detDis, 0);


            int topToWaterLeftLen = (int) Math.sqrt(Math.pow(topX - mPointDrop.mLeftWaterPoint.x, 2)
                    + Math.pow(topY - mPointDrop.mLeftWaterPoint.y, 2));
            int topToWaterRightLen = (int) Math.sqrt(Math.pow(topX - mPointDrop.mRightWaterPoint.x, 2)
                    + Math.pow(topY - mPointDrop.mRightWaterPoint.y, 2));
            int totalLen = topToWaterLeftLen + topToWaterRightLen;
            float leftFactor = (float) topToWaterLeftLen / totalLen;
            float rightFactor = (float) topToWaterRightLen / totalLen;

            int totalTopLen = (int) (halfWaterPointInterval * 0.5f);
            int topLeftDetX = (int) (totalTopLen * leftFactor);
            int topRightDetX = (int) (totalTopLen * rightFactor);
            int topLeftDetY = (int) (topLeftDetX * mPointDrop.mMiddleOfWaterPoints.tan);
            int topRightDetY = (int) (topRightDetX * mPointDrop.mMiddleOfWaterPoints.tan);
            int leftTopConX = topX - topLeftDetX;
            int leftTopConY = topY - topLeftDetY;
            int rightTopConX = topX + topRightDetX;
            int rightTopConY = topY + topRightDetY;

            int totalAdjustLen = (int) (halfWaterPointInterval * 1.5f);
            int LeftBottomDetX = (int) (totalAdjustLen * leftFactor);
            int LeftBottomDetY = (int) (LeftBottomDetX * mPointDrop.mMiddleOfWaterPoints.tan);
            int RightBottomDetX = (int) (totalAdjustLen * rightFactor);
            int RightBottomDetY = (int) (RightBottomDetX * mPointDrop.mMiddleOfWaterPoints.tan);

            int leftBottomConX = mPointDrop.mLeftWaterPoint.x + LeftBottomDetX;
            int leftBottomConY = mPointDrop.mLeftWaterPoint.y + LeftBottomDetY;
            int rightBottomConX = mPointDrop.mRightWaterPoint.x - RightBottomDetX;
            int rightBottomConY = mPointDrop.mRightWaterPoint.y - RightBottomDetY;

            mPointDrop.mDropPath.moveTo(mPointDrop.mLeftWaterPoint.x, mPointDrop.mLeftWaterPoint.y);
            mPointDrop.mDropPath.cubicTo(leftBottomConX, leftBottomConY,
                    leftTopConX, leftTopConY, topX, topY);
            mPointDrop.mDropPath.cubicTo(rightTopConX, rightTopConY,
                    rightBottomConX, rightBottomConY, mPointDrop.mRightWaterPoint.x,
                    mPointDrop.mRightWaterPoint.y);
            mPointDrop.mDropPath.addArc(mPointDrop.mDropCircleRectF, ZERO_ANGLE, FULL_ANGLE);
        } else {
            mPointDrop.mDropPath.moveTo(mPointDrop.mLeftWaterPoint.x, mPointDrop.mLeftWaterPoint.y);
            mPointDrop.mDropPath.quadTo(mPointDrop.mLeftControlPoint.x, mPointDrop.mLeftControlPoint.y,
                    mPointDrop.mLeftDropPoint.x, mPointDrop.mLeftDropPoint.y);
            mPointDrop.mDropPath.arcTo(mPointDrop.mDropCircleRectF, angleOfLeftRadiusToHor + HALF_FULL_ANGLE,
                    Math.max(angleTmp * 2, MIN_DRAW_ANGLE), false);
            mPointDrop.mDropPath.quadTo(mPointDrop.mRightControlPoint.x, mPointDrop.mRightControlPoint.y,
                    mPointDrop.mRightWaterPoint.x, mPointDrop.mRightWaterPoint.y);
        }

        Log.d(TAG, "updateWaterDrop: dropNormalizeTime = " + dropNormalizeTime);
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mLeftWaterPoint = " + mPointDrop.mLeftWaterPoint);
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mRightWaterPoint = " + mPointDrop.mRightWaterPoint);
//
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mLeftControlPoint = " + mPointDrop.mLeftControlPoint);
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mRightControlPoint = " + mPointDrop.mRightControlPoint);
//
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mLeftDropPoint = " + mPointDrop.mLeftDropPoint);
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mRightDropPoint = " + mPointDrop.mRightDropPoint);

        Log.d(TAG, "updateWaterDrop: mPointDrop.mDropCircleRectF = " + mPointDrop.mDropCircleRectF.toString());
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mInterDropAndWater = " + mPointDrop.mInterDropAndWater);
//        Log.d(TAG, "updateWaterDrop: mPointDrop.mMiddleOfWaterPoints = " + mPointDrop.mMiddleOfWaterPoints);

//        Log.d(TAG, "updateWaterDrop: ----------------------分割线----------------");

    }

    /**
     * Binary search method
     *
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     * @param x
     * @return
     */
    private float calculateTbyX(float p0, float p1, float p2, float p3, float x) {
        float t = 0;
        float t_pre = 0;
        float t_next = 1;

        int maxTimes = 10;
        int times = 0;
        float tmpX = x;
        t = (tmpX - p0) / (p3 - p0);

        while (times < maxTimes) {
            tmpX = getPointOnWater(p0, p1, p2, p3, t);
            float det = x - tmpX;
            if (Math.abs(det) < ERROR_OF_FIT_WATER_FUNC) {
                return t;
            }
            times++;

            if (det > 0) {
                t_pre = t;
                t = (t + t_next) / 2;
            } else {
                t_next = t;
                t = (t_pre + t) / 2;
            }
        }
        return t;
    }

    /**
     * Get point (x or y) of water path
     *
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     * @param t
     * @return
     */
    private float getPointOnWater(float p0, float p1, float p2, float p3, float t) {
        float one_t = 1 - t;
        return p0 * one_t * one_t * one_t + 3 * p1 * t * one_t * one_t + 3 * p2 * t * t * one_t + p3 * t * t * t;
    }

    private void adjustPointBByPointA(PointWithAngle pointA, PointWithAngle pointB, boolean onLeft, int minDet) {
        if (onLeft) {
            int det = pointA.x - pointB.x;
            pointB.x = pointA.x - Math.max(det, minDet);
        } else {
            int det = pointB.x - pointA.x;
            pointB.x = pointA.x + Math.max(det, minDet);
        }
    }

    /**
     * Get tan of water path
     *
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     * @param t
     * @return
     */
    private float getPointAngleOnWater(float p0, float p1, float p2, float p3, float t) {
        float one_t = 1 - t;
        return 3 * (p1 - p0) * one_t * one_t + 6 * (p2 - p1) * t * one_t + 3 * (p3 - p2) * t * t;
    }


    private class PointWithAngle {
        public int x;
        public int y;
        public float tan;

        public PointWithAngle() {
            this.x = 0;
            this.y = 0;
            this.tan = 0.0f;
        }

        @Override
        public String toString() {
            return "PointWithAngle{" +
                    "x=" + x +
                    ", y=" + y +
                    ", tan=" + tan +
                    '}';
        }
    }

    private class WaterDrop {
        public static final int STATE_LEAVE_WATER = 0;
        public static final int STATE_MOVE = 1;

        public Path mDropPath;
        public PointWithAngle mLeftWaterPoint;
        public PointWithAngle mLeftControlPoint;
        public PointWithAngle mLeftDropPoint;
        public PointWithAngle mRightWaterPoint;
        public PointWithAngle mRightControlPoint;
        public PointWithAngle mRightDropPoint;
        public PointWithAngle mInterDropAndWater;
        public PointWithAngle mMiddleOfWaterPoints;
        public RectF mDropCircleRectF;
        public int dropRadius;
        public int dropX;
        public float mNormalizedTime;
        public int mState;

        public WaterDrop() {
            init();
        }

        private void init() {
            mDropPath = new Path();
            mDropCircleRectF = new RectF();
            initPoint();
        }

        private void initPoint() {
            if (mLeftWaterPoint == null) {
                mLeftWaterPoint = new PointWithAngle();
            }

            if (mLeftControlPoint == null) {
                mLeftControlPoint = new PointWithAngle();
            }

            if (mLeftDropPoint == null) {
                mLeftDropPoint = new PointWithAngle();
            }

            if (mRightWaterPoint == null) {
                mRightWaterPoint = new PointWithAngle();
            }

            if (mRightControlPoint == null) {
                mRightControlPoint = new PointWithAngle();
            }

            if (mRightDropPoint == null) {
                mRightDropPoint = new PointWithAngle();
            }

            if (mInterDropAndWater == null) {
                mInterDropAndWater = new PointWithAngle();
            }

            if (mMiddleOfWaterPoints == null) {
                mMiddleOfWaterPoints = new PointWithAngle();
            }
        }
    }
}
