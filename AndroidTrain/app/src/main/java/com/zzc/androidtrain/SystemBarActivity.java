package com.zzc.androidtrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

import com.zzc.androidtrain.app.BaseActivity;

/**
 * 系统状态栏操作
 */
public class SystemBarActivity extends BaseActivity {

    Button animBtn = null;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SystemBarActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_bar);
        animBtn = (Button) findViewById(R.id.btn_anim);
    }

    public void onClickLowProfileBtn(View view) {
        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOption);
    }

    public void onClickRecoveryBtn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
    }

    public void onClickFullScreenBtn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public void onClickLayoutFullScreenBtn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void onClickHideNavBtn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void onClickHideNav2Btn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void onClickHideNav3Btn(View view) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onClickRevealEffectAnimBtn(View view) {
        showRevealAnim(animBtn);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showRevealAnim(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            //hide
            // previously visible view
            final View myView = view;

// get the center for the clipping circle
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;

// get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

// create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

// make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

// start the animation
            anim.start();
        } else {
            //show
            // previously invisible view
            View myView = view;
            myView.setVisibility(View.VISIBLE);
// get the center for the clipping circle
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;

// get the final radius for the clipping circle
            int finalRadius = myView.getWidth();

// create and start the animator for this view
// (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            anim.start();
        }
    }
}


