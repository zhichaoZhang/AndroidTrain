package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class NewFeatureInSupportLibraryActivity extends AppCompatActivity {

    private ImageView mCpuAniImageView;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NewFeatureInSupportLibraryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feature_in_support_library);
        initView();
    }

    private void initView() {
        mCpuAniImageView = (ImageView) findViewById(R.id.vector_drawable_cpu_ani);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCpuAnim();
    }

    private void startCpuAnim() {
        Drawable drawable = mCpuAniImageView.getDrawable();
        if(drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }
}
