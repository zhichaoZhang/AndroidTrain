package com.zzc.androidtrain.anim.view_anim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

/**
 * 贝塞尔曲线动画
 *
 * Created by zczhang on 17/1/6.
 */

public class QuadBezierAnimActivity extends BaseActivity{
    NearMsgView mView;
    BezierPointFloatUpView bezierPointFloatUpView;
    NotifyPointView notifyPointView;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, QuadBezierAnimActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_anim);
        mView = (NearMsgView)findViewById(R.id.view_near_msg);
        bezierPointFloatUpView = (BezierPointFloatUpView)findViewById(R.id.bezier_anim_view);
        notifyPointView = (NotifyPointView)findViewById(R.id.notify_point_view);
    }

    public void onClickStartBtn(View view) {
        mView.setText("10");
        mView.show();
    }

    public void onClickStartBtn2(View view) {
        notifyPointView.show("10");
    }

    public void onClickPauseAnimBtn(View view) {
        notifyPointView.stopAnim();
    }
}
