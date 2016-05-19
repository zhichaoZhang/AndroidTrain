package com.zzc.androidtrain.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by yamlee on 15/11/19.
 */
public class DeviceUtil {
    private TelephonyManager telephonyManager;
    private Context context;
    private static String udid;

    public static DeviceUtil getInstance(Context context) {
        return new DeviceUtil(context);
    }

    private DeviceUtil(Context context) {
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取设备唯一标识号
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (TextUtils.isEmpty(udid)) {
            udid = tm.getDeviceId();
        }
        if (TextUtils.isEmpty(udid)) {
            udid = "0";
        }
        return udid;
    }

    public static String getDeviceName() {
        return Build.BRAND + " " + Build.MODEL;
    }

    public static String getOsVersionStr() {
        return Build.VERSION.RELEASE;
    }

    public static int getOsVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static void setDeviceId(String deviceId) {
        udid = deviceId;
    }

    public String getNetworkAccessMode() {
        try {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission("android.permission.ACCESS_NETWORK_STATE",
                    context.getPackageName()) != 0) {
                return "Unknown";
            }

            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return "Unknown";
            }

            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if (typeName.equals("wifi")) {
            } else {
                typeName = info.getExtraInfo().toLowerCase();
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }
            if (typeName != null) {
                return typeName;
            }

            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }


}
