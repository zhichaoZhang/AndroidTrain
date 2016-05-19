package com.zzc.androidtrain.hotfix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zzc.androidtrain.DrawerNavigationActivity;
import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.app.BaseApplication;
import com.zzc.androidtrain.util.BugClassUtil;
import com.zzc.androidtrain.util.FileUtil;

import java.io.IOException;

/**
 * 有bug的类
 *
 * Created by zczhang on 16/4/10.
 */
public class BugClass extends BaseActivity{
    TextView tvTip = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        tvTip = (TextView)findViewById(R.id.tv_tip);
    }

    public void bugMethod() {
//        Toast.makeText(this, "这是一个有bug的方法", Toast.LENGTH_SHORT).show();
//        System.out.println("this is one bug method in a bug class");

        startActivity(new Intent(this, DrawerNavigationActivity.class));

//        Toast.makeText(this, "这个有bug的方法已被修复", Toast.LENGTH_SHORT).show();
//        System.out.println("this bug method has been fixed");
//        BugClassUtil.bugMethod(this);
    }

    public void onBugBtnClick(View view) {
        bugMethod();
    }

    public void onBugFixBtnClick(View view) {
        try {
            FileUtil.copyAssetFile2SdCard(this, "patch.apk", ((BaseApplication)getApplicationContext()).dirNewDex);
            tvTip.setText("更新文件下载成功,请杀死App重启!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRestartBtnClick(View view) {
       BugClassUtil.bugMethod(this);
    }
}
