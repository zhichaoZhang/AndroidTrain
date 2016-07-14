package com.zzc.androidtrain.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zzc.androidtrain.util.DexUtil;

import java.io.File;
import java.lang.reflect.Proxy;

/**
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
        init(base);
    }

    private void init(Context context) {
        Log.i(TAG, "init: ");
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        int usableHeapSize = activityManager.getMemoryClass();//可用堆内存
        int largeUsableHeapSize = activityManager.getLargeMemoryClass();//通过在Manifest文件中设置largeHeap = true,可以设置一个更大内存请求,但有的手机这两个值是一样的
        Log.d(TAG, "init: 最大可用堆内存---->" + usableHeapSize + "M");
        Log.d(TAG, "init: 更大可用堆内存---->" + largeUsableHeapSize + "M");

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            //下面三种状态情况,你的app均处于运行并不会被列为可杀死状态

            //设备处于低内存状态,开始触发杀死LRU Cache中的process的机制
            case TRIM_MEMORY_RUNNING_MODERATE:
                break;
            //设备处于更低内存状态,应该释放不用的资源来提升系统性能
            case TRIM_MEMORY_RUNNING_LOW:
                break;
            //设备内存严重不足,缓存的大多数进程已被杀死,这时应该释放所有非必须的资源.如果系统仍不能回收到足够的内存,系统将会杀死那些之前被认为不应该杀死的进程
            case TRIM_MEMORY_RUNNING_CRITICAL:
                break;

            //下面三种情况,你的app均处于缓存状态

            //系统低内存,app处于最不容易杀死的位置,此时应该释放那些容易恢复的资源,以便你的进程被保留下来
            case TRIM_MEMORY_BACKGROUND:
                break;
            //系统低内存,app处于LRU名单的中部位置,此时你的进程有可能被杀死
            case TRIM_MEMORY_MODERATE:
                break;
            //系统低内存,app处于LRU名单中最容易被杀掉的位置,此时应该释放任何不影响你的app恢复状态的资源
            case TRIM_MEMORY_COMPLETE:
                break;

            //与onStop不同,你的应用仅仅会在所有UI组件被隐藏的时候接受到此回调,这时应该释放仅被你的UI使用的资源
            case TRIM_MEMORY_UI_HIDDEN:
                break;
        }
    }

    /**
     * 检查是否有热更新文件,有的话则加载
     */
    public void checkHotFix() {
        Log.e(TAG, "checkHotFix: " + "检查是否有更新文件");
        dirNewDex = getFilesDir().getAbsolutePath() + File.separator + "newdex";
        File newDexDir = new File(dirNewDex);
        if (!newDexDir.exists()) {
            Log.e(TAG, "checkHotFix: " + "更新文件目录不存在!");
            return;
        }

        int fileSize = newDexDir.listFiles().length;
        if (fileSize == 0) {
            Log.e(TAG, "checkHotFix: " + "更新文件目录内不存在更新文件!");
            return;
        }

        System.out.println("---------存在需要更新的DEX----------");
        DexUtil.loadPatch(this, dirNewDex.concat("/patch.apk"));
    }
}
