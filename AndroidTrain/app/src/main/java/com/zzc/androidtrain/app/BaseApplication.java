package com.zzc.androidtrain.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zzc.androidtrain.util.DexUtil;

import java.io.File;

/**
 *
 * Created by zczhang on 16/4/10.
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    /**
     * 修复的类dex文件存储路径
     */
    public String dirNewDex;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: " + "Application 初始化");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        checkHotFix();
    }

    /**
     * 检查是否有热更新文件,有的话则加载
     */
    public void checkHotFix() {
        Log.e(TAG, "checkHotFix: " + "检查是否有更新文件");
        dirNewDex = getFilesDir().getAbsolutePath() + File.separator + "newdex";
        File newDexDir = new File(dirNewDex);
        if(!newDexDir.exists()) {
            Log.e(TAG, "checkHotFix: " + "更新文件目录不存在!");
            return;
        }

        int fileSize = newDexDir.listFiles().length;
        if(fileSize == 0) {
            Log.e(TAG, "checkHotFix: " + "更新文件目录内不存在更新文件!");
            return;
        }

        System.out.println("---------存在需要更新的DEX----------");
        DexUtil.loadPatch(this, dirNewDex.concat("/patch.apk"));
    }
}
