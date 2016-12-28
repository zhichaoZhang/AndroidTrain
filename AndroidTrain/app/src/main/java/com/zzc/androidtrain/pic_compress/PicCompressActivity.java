package com.zzc.androidtrain.pic_compress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicCompressActivity extends AppCompatActivity {
    ImageView ivQualityCompress;
    ImageView ivSizeCompress;
    ImageView ivScaleCompress;
    ImageView ivOriginCompress;
    TextView tvOriginDesc;
    TextView tvQualityDesc;
    TextView tvSizeDesc;
    TextView tvScaleDesc;
    private int targetSize = 60;//目标文件大小 80kb
    private int targetHeight = 0;
    private int targetWidth = 0;
    private int originPicHeight = 0;
    private int originPicWidth = 0;
    private final String ROOT_DIR = FileUtil.getRootPath();
    private final String mFilePathQuality = ROOT_DIR + File.separator + "quality_compress.jpeg";
    private final String mFilePathSize = ROOT_DIR + File.separator + "size_compress.jpeg";
    private final String mFilePathScale = ROOT_DIR + File.separator + "scale_compress.jpeg";
    Bitmap qualityCompressBmp;
    Bitmap sizeCompressBmp;
    Bitmap scaleCompressBmp;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PicCompressActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_compress);
        ivQualityCompress = (ImageView) findViewById(R.id.iv_quality_compress);
        ivSizeCompress = (ImageView) findViewById(R.id.iv_size_compress);
        ivScaleCompress = (ImageView) findViewById(R.id.iv_scale_compress);
        ivOriginCompress = (ImageView) findViewById(R.id.iv_origin);
        tvOriginDesc = (TextView) findViewById(R.id.tv_origin_desc);
        tvQualityDesc = (TextView) findViewById(R.id.tv_quality_desc);
        tvSizeDesc = (TextView) findViewById(R.id.tv_size_desc);
        tvScaleDesc = (TextView) findViewById(R.id.tv_scale_desc);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap originBmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_big_hor, options);
        originPicHeight = originBmp.getHeight();
        originPicWidth = originBmp.getWidth();
        tvOriginDesc.setText("宽：高 = " + originPicWidth + ":" + originPicHeight);
        ivOriginCompress.setImageBitmap(originBmp);

        //质量压缩
        qualityCompressBmp = PicCompressUtil.qualityCompress(originBmp, targetSize);
        if (qualityCompressBmp != null) {
            tvQualityDesc.setText("宽：高 = " + qualityCompressBmp.getWidth() + ":" + qualityCompressBmp.getHeight());
            ivQualityCompress.setImageBitmap(qualityCompressBmp);
        }

        //尺寸压缩
        targetHeight = originPicHeight / 2;
        targetWidth = originPicWidth / 2;
        sizeCompressBmp = PicCompressUtil.sizeCompress(this, R.drawable.img_big_hor, targetHeight, targetWidth);
        tvSizeDesc.setText("宽：高 = " + sizeCompressBmp.getWidth() + ":" + sizeCompressBmp.getHeight());
        ivSizeCompress.setImageBitmap(sizeCompressBmp);

        float scale = 0.2f;
        //比例压缩
        Bitmap originBmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.img_big_hor, options);
        scaleCompressBmp = PicCompressUtil.scaleCompress(originBmp2, scale, scale);
        tvScaleDesc.setText("宽：高 = " + scaleCompressBmp.getWidth() + ":" + scaleCompressBmp.getHeight());
        ivScaleCompress.setImageBitmap(scaleCompressBmp);
    }

    /**
     * 保存到文件
     *
     * @param view
     */
    public void onClickSaveBtn(View view) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                saveBitmapToFile(qualityCompressBmp, mFilePathQuality);
                saveBitmapToFile(sizeCompressBmp, mFilePathSize);
                saveBitmapToFile(scaleCompressBmp, mFilePathScale);
                System.out.println("写文件成功！");
            }
        };
        thread.start();
    }

    private boolean saveBitmapToFile(Bitmap bitmap, String filePath) {
        File file = FileUtil.createFile(filePath);
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
