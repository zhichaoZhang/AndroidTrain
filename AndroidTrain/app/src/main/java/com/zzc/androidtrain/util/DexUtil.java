package com.zzc.androidtrain.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Dex文件操作工具类
 *
 * Created by zczhang on 16/4/10.
 */
public class DexUtil {
    private static final String TAG = "DexUtil";
    private static final String DEX_OPT_DIR = "optdir";

    public static void injectDexAtFirst(String dexPath, String defaultDexOptPath) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, defaultDexOptPath, dexPath, getPathClassLoader());
        Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));
        Object newDexElements = getDexElements(getPathList(dexClassLoader));
        Object allDexElements = combineArray(newDexElements, baseDexElements);
        Object pathList = getPathList(getPathClassLoader());
        ReflectionUtil.setField(pathList, pathList.getClass(), "dexElements", allDexElements);

        Log.e(TAG, "injectDexAtFirst: PathClassLoader----->" + getPathClassLoader().toString());
    }

    private static PathClassLoader getPathClassLoader() {
        PathClassLoader pathClassLoader = (PathClassLoader) DexUtil.class.getClassLoader();
        Log.e(TAG, "getPathClassLoader: PathClassLoader----->" + pathClassLoader.toString());
        return pathClassLoader;
    }

    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        return ReflectionUtil.getField(paramObject, paramObject.getClass(), "dexElements");
    }

    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return ReflectionUtil.getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        Log.e(TAG, "combineArray: " + "需要插入的Dex数量" + firstArrayLength);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Log.e(TAG, "combineArray: " + "插入后的Dex总量" + allLength);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }

    public static void loadPatch(Context context, String dexPath) {

        if (context == null) {
            Log.e(TAG, "context is null");
            return;
        }
        if (!new File(dexPath).exists()) {
            Log.e(TAG, dexPath + " is null");
            return;
        }
        File dexOptDir = new File(context.getFilesDir(), DEX_OPT_DIR);
        dexOptDir.mkdir();
        try {
            injectDexAtFirst(dexPath, dexOptDir.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "inject " + dexPath + " failed");
            e.printStackTrace();
        }
    }
}
