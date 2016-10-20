package com.zzc.androidtrain.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zzc.androidtrain.R;


/**
 * 自定义拖动条
 * <p>
 * Created by zczhang on 16/9/30.
 */
public class SeekBarCustom extends View {
    //默认圆形滑块半径
    private static final int DEFAULT_THUMB_RADIUS = 30;
    //默认进度条高度
    private static final int DEFAULT_PROGRESS_HEIGHT = 20;
    //当前滑块位置
    private float mCurThumbPos = 0;
    //进度变更之前的值
    private int mLastValue = 0;
    //画笔
    private Paint mPaint = null;
    //绘制区域
    private RectF mDrawRect = null;
    //滑块半径
    private int mThumbRadius = DEFAULT_THUMB_RADIUS;
    //进度条高度
    private int mProgressHeight = DEFAULT_PROGRESS_HEIGHT;
    //进度条Y坐标
    private float mProgressY = 0;
    //阴影半径
    private int mShadowRadius = 0;
    //阴影x/y方向上的偏移
    private int mShadowX = 0;
    private int mShadowY = 5;
    //阴影颜色
    private int mShadowColor = Color.BLACK;
    //滑块颜色
    private int mThumbColor = Color.DKGRAY;
    //进度条颜色
    private int mProgressColor = Color.GRAY;
    //第二进度条颜色
    private int mSecondProColor = Color.BLUE;
    //进度条的最大值
    private int mMaxValue = 10;
    //进度条的最小值
    private int mMinValue = 0;
    //当前进度条的值
    private int mCurrentValue = 0;
    //每一个值代表的长度
    private float mLengthPerValue = 0;
    private int mScaledSlop = 0;
    //进度条起始X坐标
    private float mProgressStartX = 0;
    //进度条末尾X坐标
    private float mProgressEndX = 0;
    private OnSeekBarChanged mChangedListener;

    public SeekBarCustom(Context context) {
        super(context);
        initView(context, null);
    }

    public SeekBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SeekBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        mDrawRect = new RectF();
        mPaint = new Paint();
        mScaledSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SeekBarCustom);
        if (typedArray != null) {
            mProgressColor = typedArray.getColor(R.styleable.SeekBarCustom_progress_color, Color.GRAY);
            mSecondProColor = typedArray.getColor(R.styleable.SeekBarCustom_progress_second_color, Color.BLUE);
            mThumbColor = typedArray.getColor(R.styleable.SeekBarCustom_thumb_color, Color.DKGRAY);
            mShadowColor = typedArray.getColor(R.styleable.SeekBarCustom_shadow_color, Color.BLACK);
            mProgressHeight = typedArray.getDimensionPixelSize(R.styleable.SeekBarCustom_progress_height, DEFAULT_PROGRESS_HEIGHT);
            mThumbRadius = typedArray.getDimensionPixelSize(R.styleable.SeekBarCustom_thumb_radius, DEFAULT_THUMB_RADIUS);
            mShadowX = typedArray.getDimensionPixelSize(R.styleable.SeekBarCustom_shadow_x, 0);
            mShadowY = typedArray.getDimensionPixelSize(R.styleable.SeekBarCustom_shadow_y, 5);
            mMaxValue = typedArray.getInteger(R.styleable.SeekBarCustom_progress_max, 10);
            mMinValue = typedArray.getInteger(R.styleable.SeekBarCustom_progress_min, 0);
            mCurrentValue = typedArray.getInteger(R.styleable.SeekBarCustom_progress_current_value, 0);
            typedArray.recycle();
        }
    }

    public void setChangedListener(OnSeekBarChanged mChangedListener) {
        this.mChangedListener = mChangedListener;
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public void setMinValue(int mMinValue) {
        this.mMinValue = mMinValue;
    }

    public void setCurrentValue(int mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
    }

    public int getProgressValue() {
        return mCurrentValue;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawRect.left = getPaddingLeft();
        mDrawRect.right = w - getPaddingRight();
        mDrawRect.top = getPaddingTop();
        mDrawRect.bottom = h - getPaddingTop();

        mShadowRadius = mProgressHeight / 2;
        mProgressY = h / 2;
        mProgressStartX = mShadowRadius + mThumbRadius;
        mProgressEndX = mDrawRect.right - mShadowRadius - mThumbRadius;

        mLengthPerValue = (mProgressEndX - mProgressStartX) / (mMaxValue - mMinValue);
        mCurThumbPos = (mCurrentValue - mMinValue) * mLengthPerValue + mProgressStartX;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mThumbRadius * 2;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return mProgressHeight * 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mProgressHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShadowLayer(mShadowRadius, mShadowX, mShadowY, mShadowColor);

        //画进度条的前半段
        mPaint.setColor(mSecondProColor);
        canvas.drawLine(mProgressStartX, mProgressY, mCurThumbPos, mProgressY, mPaint);

        //画进度条的后半段
        mPaint.setColor(mProgressColor);
        canvas.drawLine(mCurThumbPos, mProgressY, mProgressEndX, mProgressY, mPaint);
        //画滑块
        mPaint.setColor(mThumbColor);
        canvas.drawCircle(mCurThumbPos, mProgressY, mThumbRadius, mPaint);
        //通知进度更改
        if (mChangedListener != null && mCurrentValue != mLastValue) {
            mChangedListener.onProgressChanged(this, mCurrentValue);
            mLastValue = mCurrentValue;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchDownX = 0;
        event.getRawX();
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                touchDownX = event.getX();
                if (mChangedListener != null) {
                    mChangedListener.onStartTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float touchMoveX = event.getX();
                if (Math.abs(touchDownX - touchMoveX) > mScaledSlop) {
                    onTouchMove(touchMoveX);
                }
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX());
                if (mChangedListener != null) {
                    mChangedListener.onEndTrackingTouch(this);
                }
                break;
        }
        return true;
    }

    private void moveThumbTo(float validateX) {
        mCurThumbPos = validateX;
        invalidate();
    }

    private void onTouchMove(float touchMoveX) {
        float validateX = touchX2ValidateX(touchMoveX);
        calculateCurProgress(validateX);
        moveThumbTo(validateX);
    }

    private void onTouchUp(float x) {
        float validateX = touchX2ValidateX(x);
        calculateCurProgress(validateX);
        //手指抬起时,滑块滚动到最近的刻度数
        float differ = validateX % mLengthPerValue;
        float halfValue = mLengthPerValue * 0.5f;
        if (differ < halfValue) {
            moveThumbTo(touchX2ValidateX(validateX - differ));
        } else {
            moveThumbTo(touchX2ValidateX(validateX + mLengthPerValue - differ));
        }
    }

    private void calculateCurProgress(float validateX) {
        float xOnProgress = validateX - mProgressStartX;
        int tempProgress = (int) (xOnProgress / mLengthPerValue) + mMinValue;
        float differ = xOnProgress % mLengthPerValue;
        float halfValue = mLengthPerValue * 0.5f;
        if (validateX == mProgressEndX) {
            mCurrentValue = mMaxValue;
            return;
        }
        if (validateX == mProgressStartX) {
            mCurrentValue = mMinValue;
            return;
        }

        if (differ < halfValue) {
            mCurrentValue = tempProgress;
        } else {
            mCurrentValue = tempProgress + 1;
        }
    }

    //有效的滑动横坐标,需要去除超出进度条区域的滑动
    private float touchX2ValidateX(float touchX) {
        touchX = Math.max(touchX, mProgressStartX);
        touchX = Math.min(touchX, mProgressEndX);
        return touchX;
    }

    public interface OnSeekBarChanged {

        void onProgressChanged(SeekBarCustom seekBarCustom, int progress);

        void onStartTrackingTouch(SeekBarCustom seekBarCustom);

        void onEndTrackingTouch(SeekBarCustom seekBarCustom);
    }

    /**
     * Default implementation
     */
    public static class OnSeekBarChangListener implements OnSeekBarChanged {

        @Override
        public void onProgressChanged(SeekBarCustom seekBarCustom, int progress) {

        }

        @Override
        public void onStartTrackingTouch(SeekBarCustom seekBarCustom) {

        }

        @Override
        public void onEndTrackingTouch(SeekBarCustom seekBarCustom) {

        }
    }
}
