package com.zzc.androidtrain.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.common.FontManger;
import com.zzc.androidtrain.util.StatusBarUtil;

/**
 * Created by zczhang on 16/4/5.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    /**
     * 当前页面是否响应系统资源配置的标志位
     */
    private boolean isNeedSystemResConfig = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public Resources getResources() {
        if (!isNeedSystemResConfig) {
            Resources resources = super.getResources();
            Configuration configuration = resources.getConfiguration();
            float fontScale = FontManger.getInstance(this).getFontSize();
            configuration.fontScale = fontScale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return resources;
        }
        return super.getResources();
    }

    public void setNeedSystemResConfig(boolean needSystemResConfig) {
        isNeedSystemResConfig = needSystemResConfig;
    }
}
