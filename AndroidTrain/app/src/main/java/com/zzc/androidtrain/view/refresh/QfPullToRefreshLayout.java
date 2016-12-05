package com.zzc.androidtrain.view.refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

import com.zzc.androidtrain.util.LogUtil;

import static com.zzc.androidtrain.view.refresh.QfPullToRefreshLayout.PullRefreshState.STATE_DRAGGING;
import static com.zzc.androidtrain.view.refresh.QfPullToRefreshLayout.PullRefreshState.STATE_IDLE;
import static com.zzc.androidtrain.view.refresh.QfPullToRefreshLayout.PullRefreshState.STATE_REFRESHING;
import static com.zzc.androidtrain.view.refresh.QfPullToRefreshLayout.PullRefreshState.STATE_REFRESH_COMPLETED;
import static com.zzc.androidtrain.view.refresh.QfPullToRefreshLayout.PullRefreshState.STATE_REFRESH_PREPARE;

/**
 * 自定义下拉刷新组件
 * <p>
 * Created by zczhang on 16/11/17.
 */

public class QfPullToRefreshLayout extends ViewGroup implements NestedScrollingChild {

    /**
     * 下拉刷新状态
     */
    public enum PullRefreshState {
        //空闲状态
        STATE_IDLE,
        //手指拖动状态
        STATE_DRAGGING,
        //正在刷新状态
        STATE_REFRESHING,
        //刷新准备状态
        STATE_REFRESH_PREPARE,
        //刷新完成
        STATE_REFRESH_COMPLETED
    }

    private static final String TAG = "QfPullToRefreshLayout";
    private static final boolean DEBUG = true;
    //刷新头部视图
    private View mHeaderView = null;
    //刷新子视图
    private View mChildView = null;
    //滑动状态临界值
    private int mScaledTouchSlop = 0;
    //可下拉的最大长度 px
    private int mMaxPullDownDistance = 600;
    //触发刷新的滑动长度的百分比
    private float REFRESH_TRRIGGER_RATIO = 0.8f;
    //刷新停留位置占总长度的百分比
    private float REFRESH_POSITON_TATIO = 0.5f;
    //动画开始时的偏移量
    private int mAnimStartPosition = 0;
    //动画减速插值器
    private Interpolator mDecelerateInterpolator = null;
    //完整动画时长 ms
    private long mAnimTotalDuration = 800;
    //位移到刷新位置动画时长 ms
    private long mAnimRefreshPositionDuration = 400;
    //手指拖动距离占最大下拉距离的百分比
    private float mDragDistanceRatio = 0f;
    //当前子视图据顶部的偏移量
    private int mChildViewCurrentTopOffset = 0;
    //上次move事件的Y坐标
    private float mLastMoveY = 0;
    //当前刷新状态
    private PullRefreshState mRefreshState = STATE_IDLE;
    private ViewParent mParent;
    private boolean mRefreshEnable = false;
    private NestedScrollingChildHelper mScrollingChildHelper;
    private final int[] mScrollOffset = new int[2];

    public QfPullToRefreshLayout(Context context) {
        this(context, null);
    }

    public QfPullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public QfPullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QfPullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化视图
     *
     * @param context 上下文
     * @param attrs   视图属性集合
     */
    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        LogUtil.d(TAG, "init: mScaledTouchSlop = %d", mScaledTouchSlop);
        mDecelerateInterpolator = new DecelerateInterpolator(3f);
    }

    /**
     * 设置刷新状态
     *
     * @param refreshing true则开始刷新 false则结束刷新
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            animateToRefreshPosition();
        } else {
            notifyRefreshCompleted();
        }
    }

    public void setEnable(boolean enable) {
        this.mRefreshEnable = enable;
    }

    /**
     * 当渲染xml中所有View后判断子View数量
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (isDebug()) {
            LogUtil.d(TAG, "the child count = %d", childCount);
        }
        //如果只有一个字view，则认为是滑动子视图
        if (childCount == 1) {
            mChildView = getChildAt(0);
        }
//        if(childCount != 2) {
//            throw new IllegalStateException("you must set a header view and a content view for QfPullToRefreshLayout.");
//        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mParent = getParent();
    }

    /**
     * 在这里做一些释放资源的操作
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isDebug()) {
            LogUtil.d(TAG, "onMeasure parentView : width = %d, height = %d, paddingTop = %d, paddingLeft = %d, paddingRight = %d, paddingBottom = " +
                            "%d.",
                    getMeasuredWidth(), getMeasuredHeight(), getPaddingTop(), getPaddingLeft(), getPaddingRight(), getPaddingBottom());
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        LogUtil.d(TAG, "parent view measure height mode = %d.", heightMode);
        LogUtil.d(TAG, "parent view measure height size = %d.", heightSize);

        //测试子视图
        //测试头部视图

        if (mChildView != null) {
//            measureChild();
//            measureChildren();
//            measureChildWithMargins();
        }
        //测量刷新子视图
        if (mChildView != null) {
            measureContentView(mChildView, widthMeasureSpec, heightMeasureSpec);
        }

        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            measureWidth = Math.max(measureWidth, childWidth);
            measureHeight = Math.max(measureHeight, childHeight);
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    /**
     * 测量刷新子视图
     *
     * @param mChildView
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureContentView(View mChildView, int widthMeasureSpec, int heightMeasureSpec) {

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.getMode(widthMeasureSpec));
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingBottom() - getPaddingTop(), MeasureSpec.getMode(heightMeasureSpec));
//        MarginLayoutParams childLp = (MarginLayoutParams) mChildView.getLayoutParams();
//        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
//                                                        getPaddingLeft() + getPaddingRight() + childLp.leftMargin + childLp.rightMargin,
//                                                        childLp.width);
//        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
//                                                        getPaddingTop() + getPaddingBottom() + childLp.topMargin + childLp.bottomMargin,
//                                                        childLp.height);
        mChildView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    /**
     * 布局子视图
     */
    private void layoutChildren() {
        if (mChildView != null) {
            //计算出子View的四个边界
//            MarginLayoutParams lp = (MarginLayoutParams) mChildView.getLayoutParams();
            int parentWidth = getMeasuredWidth();
            int parentHeight = getMeasuredHeight();
            int left = getPaddingLeft();
            int right = left + parentWidth - getPaddingRight();
            int top = getPaddingTop();
            int bottom = top + parentHeight - getPaddingBottom();

            mChildView.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float mInterceptDownY = 0f;
    private boolean mIsBeingDragged = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        if(!mRefreshEnable) {
//            return false;
//        }

        if (mRefreshState == STATE_REFRESHING || canChildScrollUp()) {
            return false;
        }

//        if (getTranslationY() == 0) {
//            LogUtil.d(TAG, "onInterceptTouchEvent: getTranslationY = " + getTranslationY());
//            if (mParent != null) {
//                mParent.requestDisallowInterceptTouchEvent(true);
//            }
//        } else {
//            return false;
//        }

        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d(TAG, "onInterceptTouchEvent Action_down:");
                mIsBeingDragged = false;
                mInterceptDownY = ev.getY();
                mLastMoveY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.d(TAG, "onInterceptTouchEvent Action_move:");
                float moveY = ev.getY();
                float yDiff = moveY - mInterceptDownY;
                if (yDiff > mScaledTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMoveY = event.getY();
                cancelMoveToStartAnim();
                LogUtil.d(TAG, "onTouchEvent: action_down_y = %f", mLastMoveY);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                LogUtil.d(TAG, "onTouchEvent: action_move_y = %f", moveY);
                float yDiff = moveY - mLastMoveY;
                //如果已经滑动到临界点，则不能继续滑动
                if (yDiff > 0 && mChildViewCurrentTopOffset >= mMaxPullDownDistance) {
                    return false;
                }

                if (yDiff < 0 && mChildViewCurrentTopOffset <= 0) {
                    mParent.requestDisallowInterceptTouchEvent(false);
                }

//                LogUtil.d(TAG, "onTouchEvent: action_move_diff = %f", yDiff);
                //如果单次滑动位移超出剩余可滑动位移，则取最小值
                if (mChildViewCurrentTopOffset + yDiff < 0) {
                    yDiff = -mChildViewCurrentTopOffset;
                }
                if (mChildViewCurrentTopOffset + yDiff > mMaxPullDownDistance) {
                    yDiff = mMaxPullDownDistance - mChildViewCurrentTopOffset;
                }

                setChildViewScrollDistance(yDiff);
                mLastMoveY = moveY;

                //计算拖动距离占可下拉距离的百分比
                mDragDistanceRatio = ((float) mChildViewCurrentTopOffset) / mMaxPullDownDistance;
                //当滑动百分比超过触发刷新的临界值并且当前没有处于刷新状态时，通知header视图可以刷新。否则同时刷新取消
                if (mRefreshState != STATE_REFRESHING) {
                    if (mDragDistanceRatio >= REFRESH_TRRIGGER_RATIO) {
                        notifyRefreshPrepare();
                    } else {
                        notifyRefreshCancel();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mRefreshState == STATE_REFRESH_PREPARE || mRefreshState == STATE_REFRESHING) {
                    //如果手指抬起时，处于刷新准备状态，则回弹到刷新位置
                    animateToRefreshPosition();
                } else {
                    //手指抬起或滑动取消，回弹到顶部
                    animateToStartPosition();
                    mRefreshState = STATE_IDLE;
                }
                break;
        }
        return true;
    }

    /**
     * 通知刷新取消
     */
    private void notifyRefreshCancel() {
        if (mRefreshState != STATE_DRAGGING) {
            mRefreshState = STATE_DRAGGING;
            LogUtil.d(TAG, "notifyRefreshPrepare: 小于刷新临界值，转换为刷新取消状态");
        }
    }


    /**
     * 通知刷新准备
     */
    private void notifyRefreshPrepare() {
        if (mRefreshState != STATE_REFRESH_PREPARE) {
            mRefreshState = STATE_REFRESH_PREPARE;
            LogUtil.d(TAG, "notifyRefreshPrepare: 到达刷新临界值，转换为可刷新状态");
        }
    }

    /**
     * 通知刷新完成
     */
    private void notifyRefreshCompleted() {
        if (mRefreshState == STATE_REFRESHING) {
            mRefreshState = STATE_REFRESH_COMPLETED;
            animateToStartPosition();
        }
    }

    //执行回弹到开始位置的动画
    private void animateToStartPosition() {
        this.mAnimStartPosition = mChildViewCurrentTopOffset;
        //动画时长要根据手指下拉长度来计算
        long animDuration = (long) (mAnimTotalDuration * mDragDistanceRatio);
        mAnimToStartPosition.cancel();
        mAnimToStartPosition.reset();
        mAnimToStartPosition.setDuration(animDuration);
        mAnimToStartPosition.setInterpolator(mDecelerateInterpolator);
        mChildView.clearAnimation();
        mChildView.startAnimation(mAnimToStartPosition);
    }

    //执行回弹到刷新位置的动画
    private void animateToRefreshPosition() {
        LogUtil.d(TAG, "animateToRefreshPosition: 动画回弹到刷新位置");
        this.mAnimStartPosition = (int) (mChildViewCurrentTopOffset - mMaxPullDownDistance * REFRESH_POSITON_TATIO);
        mDragDistanceRatio = mDragDistanceRatio == 0 ? REFRESH_POSITON_TATIO : mDragDistanceRatio;
        long animDuration = (long) (mAnimRefreshPositionDuration * mDragDistanceRatio);
        mAnimToRefreshPosition.cancel();
        mAnimToRefreshPosition.reset();
        mAnimToRefreshPosition.setDuration(animDuration);
        mAnimToRefreshPosition.setAnimationListener(mToRefreshListener);
        mAnimToRefreshPosition.setInterpolator(mDecelerateInterpolator);
        mChildView.clearAnimation();
        mChildView.startAnimation(mAnimToRefreshPosition);
    }

    //设置childView距顶部的偏移量
    private void setChildViewScrollDistance(float distance) {
        mChildView.offsetTopAndBottom((int) distance);
        mChildViewCurrentTopOffset = mChildView.getTop();
//        LogUtil.d(TAG, "setChildViewScrollDistance: mChildViewCurrentTopOffset = %d", mChildViewCurrentTopOffset);
    }

    private boolean isDebug() {
        return DEBUG;
    }

    //滑动到开始位置的动画
    private final Animation mAnimToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStartPosition(interpolatedTime);
        }
    };

    //滑动到刷新位置的动画
    private final Animation mAnimToRefreshPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            moveToRefreshPosition(interpolatedTime);
        }
    };

    private Animation.AnimationListener mToRefreshListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mRefreshState = STATE_REFRESHING;
        }
    };

    //具体执行位移操作
    private void moveToStartPosition(float interpolatedTime) {
        int targetTop = mAnimStartPosition - (int) (mAnimStartPosition * interpolatedTime);
//        LogUtil.d(TAG, "moveToStartPosition: targetTop = %d", targetTop);
        int offset = targetTop - mChildView.getTop();
//        LogUtil.d(TAG, "moveToStartPosition: offset = %d", offset);
        setChildViewScrollDistance(offset);
    }

    //具体执行位移操作
    private void moveToRefreshPosition(float interpolatedTime) {
        int targetTop = mAnimStartPosition - (int) (mAnimStartPosition * interpolatedTime);
        LogUtil.d(TAG, "moveToStartPosition: targetTop = %d", targetTop);
        int offset = (int) (targetTop - (mChildView.getTop() - mMaxPullDownDistance * REFRESH_POSITON_TATIO));
        LogUtil.d(TAG, "moveToStartPosition: offset = %d", offset);
        setChildViewScrollDistance(offset);
    }

    private void cancelMoveToStartAnim() {
        mAnimToStartPosition.cancel();
        mChildView.clearAnimation();
    }

    private boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, -1);
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return mScrollingChildHelper;
    }



}
