package com.zzc.androidtrain.image_filter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.zzc.androidtrain.R;

/**
 * 图片滤镜
 */
public class ImageFilterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivTest;
    Bitmap mBitmap;
    int bitmapWidth;
    int bitmapHeight;
    Button btnOrigin;
    Button btnOld;
    Button btnPic;
    Button btnFudiao;
    Button btnCustom;
    Button btnOpenGl;
    GridLayout gridLayout;
    EditText[] editTexts;
    private int mHeight;
    private int mWith;
    private float[] colorMatrix = new float[20];

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ImageFilterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        ivTest = (ImageView) findViewById(R.id.iv_test);
        btnOrigin = (Button) findViewById(R.id.btn_origin);
        btnOld = (Button) findViewById(R.id.btn_old);
        btnPic = (Button) findViewById(R.id.btn_pic);
        btnFudiao = (Button) findViewById(R.id.btn_fudiao);
        btnCustom = (Button) findViewById(R.id.btn_custom_change);
        btnOpenGl = (Button) findViewById(R.id.btn_opengl);
        gridLayout = (GridLayout) findViewById(R.id.gridview);

        btnOrigin.setOnClickListener(this);
        btnOld.setOnClickListener(this);
        btnPic.setOnClickListener(this);
        btnFudiao.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
        btnOpenGl.setOnClickListener(this);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_test);
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();

        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                mWith = gridLayout.getWidth() / 5;
                mHeight = gridLayout.getHeight() / 4;
                editTexts = new EditText[20];
                addEts();
                initMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i < 20; i++) {
            EditText et = new EditText(ImageFilterActivity.this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            editTexts[i] = et;
            gridLayout.addView(et, mWith, mHeight);
        }
    }

    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                editTexts[i].setText(1 + "");
                colorMatrix[i] = 1.0f;
            } else {
                editTexts[i].setText(0 + "");
                colorMatrix[i] = 0.0f;
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_origin:
                ivTest.setImageBitmap(mBitmap);
                break;
            case R.id.btn_old:
                ivTest.setImageBitmap(setOldBitmap(mBitmap));
                break;
            case R.id.btn_pic:
                ivTest.setImageBitmap(setPicBitmap(mBitmap));
                break;
            case R.id.btn_fudiao:
                ivTest.setImageBitmap(setFudiaoBitmap(mBitmap));
                break;
            case R.id.btn_custom_change:
                Bitmap bitmap = setCustomChange(mBitmap);
                if(bitmap != null) {
                    ivTest.setImageBitmap(bitmap);
                }
                break;
            case R.id.btn_opengl:
                startActivity(OpenglFilterEffectActivity.getCallingIntent(this));
                break;
        }
    }

    private Bitmap setCustomChange(Bitmap bitmap) {

        try {
            for (int i = 0; i < editTexts.length; i++) {
                colorMatrix[i] = Float.parseFloat(editTexts[i].getText().toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix matrix = new ColorMatrix(colorMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bmp;
    }

    private Bitmap setOldBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        int[] oldPixels = new int[bitmapWidth * bitmapHeight];
        int[] newPixels = new int[bitmapWidth * bitmapHeight];
        bitmap.getPixels(oldPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int color;
        int r = 0, g = 0, b = 0, a = 0;
        int rl = 0, gl = 0, bl = 0;
        for (int i = 0; i < oldPixels.length; i++) {
            color = oldPixels[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            rl = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            gl = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            bl = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            rl = Math.min(255, rl);
            gl = Math.min(255, gl);
            bl = Math.min(255, bl);

            newPixels[i] = Color.argb(a, rl, gl, bl);
        }
        newBitmap.setPixels(newPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        return newBitmap;
    }

    private Bitmap setPicBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        int[] oldPixels = new int[bitmapWidth * bitmapHeight];
        int[] newPixels = new int[bitmapWidth * bitmapHeight];
        bitmap.getPixels(oldPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int color;
        int r = 0, g = 0, b = 0, a = 0;
        int rl = 0, gl = 0, bl = 0;
        for (int i = 0; i < oldPixels.length; i++) {
            color = oldPixels[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            rl = 255 - r;
            gl = 255 - g;
            bl = 255 - b;

            newPixels[i] = Color.argb(a, rl, gl, bl);
        }
        newBitmap.setPixels(newPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        return newBitmap;
    }

    private Bitmap setFudiaoBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        int[] oldPixels = new int[bitmapWidth * bitmapHeight];
        int[] newPixels = new int[bitmapWidth * bitmapHeight];
        bitmap.getPixels(oldPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int color, oldColor;
        int r = 0, g = 0, b = 0, a = 0;
        int r0 = 0, g0 = 0, b0 = 0, a0 = 0;
        int rl = 0, gl = 0, bl = 0;
        for (int i = 1; i < oldPixels.length; i++) {
            oldColor = oldPixels[i - 1];
            r0 = Color.red(oldColor);
            g0 = Color.green(oldColor);
            b0 = Color.blue(oldColor);
            a0 = Color.alpha(oldColor);

            color = oldPixels[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            rl = r0 - r + 127;
            if (rl < 0) {
                rl = 0;
            } else if (rl > 255) {
                rl = 255;
            }

            gl = g0 - g + 127;
            if (gl < 0) {
                gl = 0;
            } else if (gl > 255) {
                gl = 255;
            }

            bl = b0 - b + 127;
            if (bl < 0) {
                bl = 0;
            } else if (bl > 255) {
                bl = 255;
            }

            newPixels[i] = Color.argb(a, rl, gl, bl);
        }
        newBitmap.setPixels(newPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        return newBitmap;
    }
}
