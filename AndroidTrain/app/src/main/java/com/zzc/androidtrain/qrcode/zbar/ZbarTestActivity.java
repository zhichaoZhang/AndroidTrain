package com.zzc.androidtrain.qrcode.zbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zczhang on 16/12/4.
 */

public class ZbarTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbar_test);
    }

    public void analysisQrcode(View view) {
        Zbar zbar = new Zbar();
        String path = "barcode.png";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        long currentTime = System.currentTimeMillis();
        String result = zbar.decode(bitmap.getWidth(), bitmap.getHeight(), data);
        long nextTime = System.currentTimeMillis();
        System.out.println("二维码解析结果---->" + result);
        System.out.println("二维码解析耗时---->" + (nextTime - currentTime));
    }

    public byte[] getTestImageData(String path) {
        byte[] image = null;
        if (path == null || "".equals(path)) {
            Log.e("getTestImageData", "param path is error");
            return null;
        }
        File file = new File(path);
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream(1000);
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, i);
            }
            image = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return image;
    }

    public static void main(String[] args) {
        Zbar zbar = new Zbar();
        String path = "barcode.png";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        String result = zbar.decode(bitmap.getWidth(), bitmap.getHeight(), data);
        System.out.println("二维码解析结果---->" + result);
    }
}
