package com.zzc.androidtrain.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;


/**
 * Created by yamlee on 15/8/6.
 */
public class ApkUtil {

    public static final String IS_FIRST_LAUNCH = "is_first_launch";

    /**
     * 判断当前渠道包是否显示首发字样
     *
     * @param context
     * @return
     */
    public static boolean isFirstPublish(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getBoolean("FIRST_LAUNCHER");
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 设置程序飞第一次安装启动
     *
     * @param context
     */
    public static void setAlredyInstalled(Context context) {
//        SPUtil.getInstance(context).save(IS_FIRST_LAUNCH, false);
    }

    /**
     * 获取程序包的metadata信息
     *
     * @param context
     * @return
     */
    public static String getMetaData(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if(bundle != null) {
                return bundle.getString("UMENG_CHANNEL", "");
            } else {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            if (info.versionName.startsWith("V") || info.versionName.startsWith("v")) {
                return info.versionName;
            } else {
                return "v" + info.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.x";
    }

    /**
     * 获取app版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
