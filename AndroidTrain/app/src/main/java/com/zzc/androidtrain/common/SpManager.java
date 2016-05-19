package com.zzc.androidtrain.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharePreference工具类
 *
 * Created by zczhang on 16/4/6.
 */
public class SpManager {
    private static volatile SpManager mInstance;
    private SharedPreferences mSharedPreferences;

    private SpManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SpManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SpManager.class) {
                if(mInstance == null) {
                    mInstance = new SpManager(context);
                }
            }
        }
        return mInstance;
    }

    public void saveFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }
}
