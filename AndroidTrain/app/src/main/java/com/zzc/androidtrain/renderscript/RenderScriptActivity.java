package com.zzc.androidtrain.renderscript;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.zzc.androidtrain.R;

import net.qiujuer.genius.blur.StackBlur;

public class RenderScriptActivity extends AppCompatActivity {
    ImageView ivRenderScript;
    Bitmap sourceBitmap;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, RenderScriptActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_script);
        initView();
    }

    private void initView() {
        ivRenderScript = (ImageView) findViewById(R.id.iv_renderscript);
        sourceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_test);
        ivRenderScript.setImageBitmap(sourceBitmap);
    }

    public void onBlurBtnClick(View view) {
        long startTime = System.currentTimeMillis();
        Bitmap resultBitmap = BlurBuilder.blur(this, sourceBitmap);
        ivRenderScript.setImageBitmap(resultBitmap);
        long endTime = System.currentTimeMillis();
        System.out.println("RenderScript模糊用时---->" + (endTime - startTime) + "mm");
    }

    public void onFastBlurBtnClick(View view) {
        long startTime = System.currentTimeMillis();
        Bitmap resultBitmap = BlurBuilder.fastBlur(sourceBitmap, 70);
        ivRenderScript.setImageBitmap(resultBitmap);
        long endTime = System.currentTimeMillis();
        System.out.println("快速模糊java实现用时---->" + (endTime - startTime) + "mm");
    }

    public void onFastBlurCPixels(View view) {
        long startTime = System.currentTimeMillis();
        Bitmap resultBitmap = StackBlur.blurNativelyPixels(sourceBitmap, 70, true);
        ivRenderScript.setImageBitmap(resultBitmap);
        long endTime = System.currentTimeMillis();
        System.out.println("快速模糊C实现处理像素用时---->" + (endTime - startTime) + "mm");
    }

    public void onFastBlurCBitmap(View view) {
        long startTime = System.currentTimeMillis();
        Bitmap resultBitmap = StackBlur.blurNatively(sourceBitmap, 70, true);
        ivRenderScript.setImageBitmap(resultBitmap);
        long endTime = System.currentTimeMillis();
        System.out.println("快速模糊java实现处理Bitmap用时---->" + (endTime - startTime) + "mm");
    }


}

