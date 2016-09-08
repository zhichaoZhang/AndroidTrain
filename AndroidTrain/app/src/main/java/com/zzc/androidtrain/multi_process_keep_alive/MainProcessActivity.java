package com.zzc.androidtrain.multi_process_keep_alive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

/**
 *
 * Created by zczhang on 16/9/8.
 */
public class MainProcessActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_process);
    }

    public void onStartBtnClick(View view) {
        startService(new Intent(MainProcessActivity.this, LocalService.class));
        startService(new Intent(MainProcessActivity.this, RemoteService.class));
    }
}
