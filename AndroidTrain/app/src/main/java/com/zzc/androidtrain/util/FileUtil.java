package com.zzc.androidtrain.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具类
 * <p>
 * Created by zczhang on 16/4/10.
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getAppPicDirPath() {
        return getRootPath();
    }

    public static boolean createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static File createFile(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "createFile: filePath = " + filePath);
            return null;
        }
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean copyAssetFile2SdCard(Context context, String assetFileName, String sdcardPath) throws IOException {

        File sdcardFile = new File(sdcardPath, assetFileName);
        if (!sdcardFile.exists()) {
            new File(sdcardPath).mkdirs();
            sdcardFile.createNewFile();
            InputStream inputStream = null;
            OutputStream fileOutputStream = null;
            try {
                inputStream = context.getAssets().open(assetFileName);

                fileOutputStream = new FileOutputStream(sdcardFile);
                copyFile(inputStream, fileOutputStream);
            } finally {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        }


        return true;
    }

    private static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }
}
