package com.zzc.androidtrain.view.refresh;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.*;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

public class PullToRefreshActivity extends BaseActivity{
    QfPullToRefreshLayout pullToRefreshLayout;
    RecyclerView recyclerView;
    AppBarLayout appBarLayout = null;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PullToRefreshActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
        pullToRefreshLayout = (QfPullToRefreshLayout)findViewById(R.id.refresh_layout);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this));

    }

    public void onCLickStartRefresh(View view) {
        pullToRefreshLayout.setRefreshing(true);
    }

    public void onCLickStopRefresh(View view) {
        pullToRefreshLayout.setRefreshing(false);
    }



    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        private Context mContext;


        public MyAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_gride_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            holder.textView.setText("嵌套滑动 " + position);
            holder.imageView.setImageURI(Uri.parse("http://near.m1img.com/op_upload/115/14703053173.png"));
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_text);
                imageView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
            }
        }
    }
}
