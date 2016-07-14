package com.zzc.androidtrain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * 测试Fragment
 * Created by zczhang on 16/7/7.
 */
public class TestFragment2 extends BaseFragment {
    Button btnAddNewFragment;
    public static TestFragment2 newInstance() {
        return new TestFragment2();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2, container, false);
        btnAddNewFragment = (Button)view.findViewById(R.id.btn_add_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAddNewFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof FragmentActivity) {
                    ((FragmentActivity)getActivity()).addFragment(TestFragment3.newInstance());
                }
            }
        });
    }
}
