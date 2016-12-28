package com.zzc.androidtrain.view.refresh;

/**
 * 下拉刷新头部操作接口
 * <p>
 * Created by zczhang on 16/11/17.
 */

public interface QfRefreshHeaderHandler {

    /**
     * 下拉刷新开始前或结束后回调
     */
    void onRefreshReset();

    /**
     * 准备刷新
     */
    void onRefreshPrepare();

    /**
     * 开始刷新
     */
    void onRefreshBegin();

    /**
     * 刷新完成后回调
     */
    void onRefreshComplete();

    /**
     * 下拉滑动距离变更时回调
     *
     * @param y 滑动距离
     */
    void onPullPositionChanged(float y);
}
