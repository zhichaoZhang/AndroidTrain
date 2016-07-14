package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zzc.androidtrain.util.StatusBarUtil;

public class StatusBarModelActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, StatusBarModelActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar_model);
//        getSupportActionBar().hide();
        StatusBarUtil.setTranslucent(this, 0);
    }
}
