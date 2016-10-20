package com.zzc.androidtrain.apk_patch;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 生成差分文件工具类
 *
 * Created by zczhang on 16/10/16.
 */

public class FileDifferUtil {

    private static String libs = "/Users/joye/develop/android/workspace/AndroidTrain/AndroidTrain/app/src/main/jniLibs/mips";

    public void genFileDiffer() {
        String oldPath = "/Users/joye/Desktop/patch_update/app-debug.apk";
        String newPath = "/Users/joye/Desktop/patch_update/app-debug_new.apk";
        String patchPath = "/Users/joye/Desktop/patch_update/apk.patch";
        new FileDiffer().genDifferFile(oldPath, newPath, patchPath);
    }

    public static void main(String[] args) {
        try {
            FileDifferUtil.addDir(libs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("java.library.path = " + System.getProperty("java.library.path"));
        new FileDifferUtil().genFileDiffer();
    }

    public static void addDir(String s) throws IOException {
        try {
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = s;
            field.set(null,tmp);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }
}
