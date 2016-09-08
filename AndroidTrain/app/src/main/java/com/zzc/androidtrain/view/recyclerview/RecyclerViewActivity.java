package com.zzc.androidtrain.view.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.widget.MyRecyclerView;

import java.util.List;

/**
 * RecyclerView列表页面
 * <p/>
 * Created by zczhang on 16/9/7.
 */
public class RecyclerViewActivity extends BaseActivity {
    MyRecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();
    }

    private void initView() {
        recyclerView = (MyRecyclerView)findViewById(R.id.recycler_view);

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Context mContext;
        private List<String> mData;

        public MyAdapter(Context context) {
            this.mContext = context;
        }

        public void setData(List<String> data) {
            this.mData = data;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
