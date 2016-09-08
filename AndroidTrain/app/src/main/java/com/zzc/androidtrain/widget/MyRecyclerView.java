package com.zzc.androidtrain.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 *
 * Created by zczhang on 16/8/6.
 */
public class MyRecyclerView extends RecyclerView{
    private static final String TAG = "MyRecyclerView";
    private int i = 0;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        Log.e(TAG, "onMeasure: " + i++);
        super.onMeasure(widthSpec, heightSpec);
    }
}
