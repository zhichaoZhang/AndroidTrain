package com.zzc.androidtrain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * FragmentManager回退栈监听
 *
 *
 * Created by zczhang on 16/7/7.
 */
public class FragmentActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener{
    private static final String TAG = "FragmentActivity";
    FrameLayout flContainer;
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        flContainer = (FrameLayout)findViewById(R.id.fl_container);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        initFragment(TestFragment1.newInstance());
    }

    public void initFragment(BaseFragment baseFragment) {
        replaceFragment(baseFragment);
    }

    public void addFragment(BaseFragment baseFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, baseFragment, baseFragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void replaceFragment(BaseFragment baseFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, baseFragment, baseFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged: getBackStackEntryCount---->" + mFragmentManager.getBackStackEntryCount());
        Log.i(TAG, "onBackStackChanged: getFragments---->" + mFragmentManager.getFragments().size());
        for(Fragment fragment : mFragmentManager.getFragments()) {
            Log.i(TAG, ("Fragment Back Stack's Fragment is "+fragment));
        }
    }
}
