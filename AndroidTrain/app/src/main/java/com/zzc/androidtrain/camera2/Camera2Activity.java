package com.zzc.androidtrain.camera2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.util.Toaster;

/**
 * 相机操作
 * <p>
 * Created by zczhang on 16/7/12.
 * <p>
 * PackageManager.hasSystemFeature()方法检测系统支持特性
 * Camera.getNumberOfCameras()方法检测摄像头个数
 */
public class Camera2Activity extends BaseActivity {
    private static final String TAG = "Camera2Activity";

    /**
     * 检测手机是否支持相机
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取相机实例
     * @return
     */
    public static android.hardware.Camera getCameraInstance() {
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "getCameraInstance: open camera error");
        }
        return camera;
    }
}

