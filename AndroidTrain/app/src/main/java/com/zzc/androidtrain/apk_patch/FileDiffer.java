package com.zzc.androidtrain.apk_patch;

/**
 * 文件差分合并类
 *
 * Created by zczhang on 16/10/15.
 */

public class FileDiffer {

    static {
        System.loadLibrary("bspatch");
        System.loadLibrary("bsdiff");
    }

    public native int genDifferFile(String oldFile, String newFile, String differFile);

    public native int fileCombine(String oldFile, String newFile, String patchFile);

}
