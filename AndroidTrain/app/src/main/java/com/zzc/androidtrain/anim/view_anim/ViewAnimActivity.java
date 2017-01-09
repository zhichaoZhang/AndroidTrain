package com.zzc.androidtrain.anim.view_anim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zzc.androidtrain.R;

public class ViewAnimActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ViewAnimActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_anim);
    }

    public void onClickBezierAnim(View view) {
        startActivity(QuadBezierAnimActivity.getCallingIntent(this));
    }
}
