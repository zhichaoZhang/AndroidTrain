package com.zzc.androidtrain.pic_compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 图片压缩工具类
 * <p>
 * Created by zczhang on 16/12/27.
 */

public class PicCompressUtil {
    private static final String TAG = "PicCompressUtil";

    /**
     * 质量压缩
     * 100 -> 50
     *
     * @param bitmap
     * @param targetSize
     * @return
     */
    public static Bitmap qualityCompress(Bitmap bitmap, int targetSize) {
        int quality = 100;//100表示不压缩
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        while (outputStream.size() / 1024 > targetSize && quality > 10) {
            outputStream.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * 采样率压缩
     * eg:2068*1079 -> 960*480
     *
     * @param picPath   图片路径
     * @param maxHeight 最大高度
     * @param maxWidth  最大宽度
     * @return 处理好的图片
     */
    public static Bitmap sizeCompress(String picPath, int maxHeight, int maxWidth) {
        if (TextUtils.isEmpty(picPath)) {
            Log.e(TAG, "sizeCompress: the picPath is " + picPath);
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        //先查询图片信息而不加载图片到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, maxHeight, maxWidth);
        return BitmapFactory.decodeFile(picPath, options);
    }

    public static Bitmap sizeCompress(Context context, int resId, int maxHeight, int maxWidth) {
        if (context == null) {
            Log.e(TAG, "sizeCompress: the context is " + context);
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        //先查询图片信息而不加载图片到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, maxHeight, maxWidth);
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int maxHeight, int maxWidth) {
        int sampleSize = 1;
        int actHeight = options.outHeight;
        int actWidth = options.outWidth;
        if (actHeight > maxHeight || actWidth > maxWidth) {
            int heightRatio = Math.round((float) actHeight / (float) maxHeight);
            int widthRatio = Math.round((float) actWidth / (float) maxWidth);
            sampleSize = Math.max(heightRatio, widthRatio);
        }

        return sampleSize;
    }

    /**
     * 比例压缩
     *
     * @return
     */
    public static Bitmap scaleCompress(Bitmap bitmap, float heightScale, float widthScale) {
        int actHeight = bitmap.getHeight();
        int actWidth = bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        return Bitmap.createBitmap(bitmap, 0, 0, actWidth, actHeight, matrix, true);
    }
}
