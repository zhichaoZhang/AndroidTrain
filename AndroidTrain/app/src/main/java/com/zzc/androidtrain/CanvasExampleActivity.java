package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CanvasExampleActivity extends AppCompatActivity {
    private static final String TAG = "CanvasExampleActivity";
    RelativeLayout rlContainer;
    ImageView ivCanvas;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, CanvasExampleActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_example);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        ivCanvas = (ImageView) findViewById(R.id.iv_canvas);
    }

    public void paintLine(View view) {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setStrokeWidth(10);//画笔宽度
//        paint.setStrokeMiter(50);//画笔倾斜度
        paint.setStyle(Paint.Style.FILL_AND_STROKE);//画笔样式 实心/空心
        paint.setStrokeJoin(Paint.Join.ROUND);//画笔结合处样式 锐角/圆弧/直线
        paint.setStrokeCap(Paint.Cap.ROUND);//笔刷尾端样式
        canvas.drawLine(0, 0, 250, 250, paint);
        canvas.save();
        ivCanvas.setImageBitmap(bitmap);
    }

    public void paintRec(View view) {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setShadowLayer(5, 10, 10, Color.parseColor("#7f000000"));
        canvas.drawRoundRect(new RectF(0, 0, 300, 300), 20, 20, paint);
        canvas.save();
        ivCanvas.setImageBitmap(bitmap);
    }

    public void paintText(View view) {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(56);
//        paint.setStrikeThruText(true);//横线
        paint.setHinting(Paint.HINTING_ON);
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawColor(Color.GREEN);

        canvas.drawText("这是一行文字(正常)!", 20, 100, paint);

        paint.setFakeBoldText(true);
        canvas.drawText("这是一行文字(加粗)!", 20, 200, paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setFakeBoldText(false);
        paint.setStrokeWidth(1);
        canvas.drawText("这是一行文字(描边)!", 20, 300, paint);
        canvas.save();
        ivCanvas.setImageBitmap(bitmap);
    }

    public void paintTextWithPath(View view) {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextSize(70);
        paint.setShadowLayer(10, 5, 5, Color.RED);
        canvas.drawColor(Color.GREEN);
        Path path = new Path();
        path.moveTo(100, 100);
        path.lineTo(500, 600);
        path.lineTo(300, 1000);
        canvas.drawTextOnPath("这是一行文字!这是一行文字!", path, 0, 0, paint);
        canvas.save();
        ivCanvas.setImageBitmap(bitmap);

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                Log.d(TAG, "onTrimMemory: " + "UI对用户完全不可见");
                break;
        }
    }
}
