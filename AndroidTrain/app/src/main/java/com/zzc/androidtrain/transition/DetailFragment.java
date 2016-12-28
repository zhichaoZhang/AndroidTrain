package com.zzc.androidtrain.transition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * Created by zczhang on 16/11/17.
 */

public class DetailFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_transition_detail, container, false);
    }
}
