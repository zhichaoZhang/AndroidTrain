package com.zzc.androidtrain.change_icon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zzc.androidtrain.R;

public class ChangeAppIconActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChangeAppIconActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_app_icon);
    }

    public void onClickChangeAppIconBtn(View view) {
        ComponentName componentName = new ComponentName(getBaseContext(), "com.zzc.androidtrain.MainActivity2");
        ComponentName defaultName = new ComponentName(getBaseContext(), "com.zzc.androidtrain.DrawerNavigationActivity");
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(defaultName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
