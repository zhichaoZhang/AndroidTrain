package com.zzc.androidtrain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * 测试Fragment
 * Created by zczhang on 16/7/7.
 */
public class TestFragment3 extends BaseFragment {

    public static TestFragment3 newInstance() {
        return new TestFragment3();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test3, container, false);
    }
}
