package com.zzc.androidtrain.recycleview_drag_drop;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.zzc.androidtrain.util.LogUtil;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * 下拉刷新行为
 * <p>
 * Created by zczhang on 16/11/26.
 */

public class PullDownRefreshBehavior extends CoordinatorLayout.Behavior<ViewGroup> {
    private static final String TAG = "PullDownRefreshBehavior";
    private int mTargetY = -1;
    private Runnable mFlingRunnable;
    private ScrollerCompat mScroller;

    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private int mLastMotionY;
    private int mTouchSlop = -1;
    private VelocityTracker mVelocityTracker;

    public PullDownRefreshBehavior() {
    }

    public PullDownRefreshBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, ViewGroup child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View directTargetChild, View target, int
            nestedScrollAxes) {
        LogUtil.d(TAG, "onStartNestedScroll: child = %s, directTargetChild = %s, target = %s, nestScrollAxes = %d ", child.toString(),
                directTargetChild.toString(), target.toString(), nestedScrollAxes);
        if (mTargetY == -1) {
            mTargetY = target.getScrollY();
        }
        boolean start = false;
        start = ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 //响应垂直方法的滚动
                //coordinatorLayout是全屏，只有当可滚动的View的高度 + 协同滚动的View高度大于全屏，才响应嵌套滚动事件。否则所有View的高度和小于CoordinatorLayout，滚动没有意。
                && coordinatorLayout.getHeight() - directTargetChild.getHeight() <= child.getHeight()
        );
        LogUtil.d(TAG, "onStartNestedScroll: start = %b, getTop = %d, getHeight = %d", start, child.getTop(), child.getHeight());
        return start;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, ViewGroup child, View directTargetChild, View target, int
            nestedScrollAxes) {
        LogUtil.d(TAG, "onNestedScrollAccepted: child = %s, directTargetChild = %s, target = %s, nestScrollAxes = %d ", child.toString(),
                directTargetChild.toString(), target.toString(), nestedScrollAxes);
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target) {
        LogUtil.d(TAG, "onStopNestedScroll: child = %s, target = %s", child.toString(), target.toString());
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        LogUtil.d(TAG, "onNestedScroll: child = %s, target = %s, dxConsumed = %d, dyConsumed = %d, dxUnconsumed = %d, dyUnconsumed = %d", child
                .toString(), target.toString(), dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed < 0) {

        }

    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target, int dx, int dy, int[] consumed) {
        LogUtil.d(TAG, "onNestedPreScroll: child = %s, target = %s, dx = %d, dy = %d, consumed[0] = %d, consumed[1] = %d", child.toString(), target
                .toString(), dx, dy, consumed[0], consumed[1]);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if (dy < 0) {
            //向下滚

        } else {
            //向上滚

        }

        ViewCompat.offsetTopAndBottom(child, -dy);
    }

//    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, ViewGroup child, MotionEvent ev) {
//        if (mTouchSlop < 0) {
//            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
//        }
//
//        switch (MotionEventCompat.getActionMasked(ev)) {
//            case MotionEvent.ACTION_DOWN: {
//                final int x = (int) ev.getX();
//                final int y = (int) ev.getY();
//
//                if (parent.isPointInChildBounds(child, x, y)) {
//                    mLastMotionY = y;
//                    mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                    ensureVelocityTracker();
//                } else {
//                    return false;
//                }
//                break;
//            }
//
//            case MotionEvent.ACTION_MOVE: {
//                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
//                        mActivePointerId);
//                if (activePointerIndex == -1) {
//                    return false;
//                }
//
//                final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
//                int dy = mLastMotionY - y;
//
//                if (!mIsBeingDragged && Math.abs(dy) > mTouchSlop) {
//                    mIsBeingDragged = true;
//                    if (dy > 0) {
//                        dy -= mTouchSlop;
//                    } else {
//                        dy += mTouchSlop;
//                    }
//                }
//
//                if (mIsBeingDragged) {
//                    mLastMotionY = y;
//                    // We're being dragged so scroll the ABL
//                    scroll(parent, child, dy, getMaxDragOffset(child), 0);
//                }
//                break;
//            }
//
//            case MotionEvent.ACTION_UP:
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.addMovement(ev);
//                    mVelocityTracker.computeCurrentVelocity(1000);
//                    float yvel = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
//                            mActivePointerId);
//                    fling(parent, child, -getScrollRangeForDragFling(child), 0, yvel);
//                }
//                // $FALLTHROUGH
//            case MotionEvent.ACTION_CANCEL: {
//                mIsBeingDragged = false;
//                mActivePointerId = INVALID_POINTER;
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }
//                break;
//            }
//        }
//
//        if (mVelocityTracker != null) {
//            mVelocityTracker.addMovement(ev);
//        }
//
//        return true;
//    }
//
//    private void ensureVelocityTracker() {
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//    }
//
//    /**
//     * Returns the maximum px offset when {@code view} is being dragged.
//     */
//    int getMaxDragOffset(View view) {
//        return -view.getHeight();
//    }
}
