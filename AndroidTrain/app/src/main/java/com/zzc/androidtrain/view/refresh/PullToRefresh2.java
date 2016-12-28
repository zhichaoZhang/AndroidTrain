package com.zzc.androidtrain.view.refresh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.util.LogUtil;
import com.zzc.androidtrain.util.ScreenUtil;

public class PullToRefresh2 extends BaseActivity {
    private static final String TAG = "PullToRefresh2";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    AppBarLayout appBarLayout = null;
//    private DynamicAdapter<String> mAdapter;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, PullToRefresh2.class);
        return intent;
    }

    int lastScrollState = RecyclerView.SCROLL_STATE_IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh2);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
//        swipeRefreshLayout.setEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PullToRefreshActivity.MyAdapter(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtil.d(TAG, "onScrollStateChanged: newState = %d", newState);
                LogUtil.d(TAG, "onScrollStateChanged: recyclerView.getY() = %f", recyclerView.getY());
                if (lastScrollState == RecyclerView.SCROLL_STATE_SETTLING && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //上次状态是Fling，然后当前状态是停止
                    //检查recycleview是否滚动到顶部
                    //如果滚动到顶部，则展开头部
                    int verticalOffset = recyclerView.computeVerticalScrollOffset();

                    if (verticalOffset == 0 && recyclerView.getY() == 84) {
                        appBarLayout.setExpanded(true, true);
                    }
                }
                lastScrollState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.d(TAG, "onScrolled: dy = %d", dy);
            }
        });
//        mAdapter = new DynamicAdapter<>(Utils.getSampleData());
//        RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(mAdapter) {
//            @Override
//            public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
//                return null;
//            }
//
//            @Override
//            public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
//                return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing);
//            }
//        };
//
//        vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        vRecyclerView.setAdapter(adapter);
        final int headHeight = ScreenUtil.dip2px(this, 200);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                LogUtil.d(TAG, "onScroll: dy = %d", dy);
////
//                if (appBarLayout.getTop() >= -headHeight && appBarLayout.getTop() <= 0) {
//                    appBarLayout.offsetTopAndBottom(-dy);
//                    recyclerView.offsetTopAndBottom(-dy);
//                }
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
    }
}
