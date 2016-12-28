package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzc.androidtrain.view.SeekBarCustom;


public class CanvasExampleActivity extends AppCompatActivity {
    private static final String TAG = "CanvasExampleActivity";
    RelativeLayout rlContainer;
    ImageView ivCanvas;
    SeekBarCustom seekBarCustom;
    TextView tvProgress;

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
        seekBarCustom = (SeekBarCustom) findViewById(R.id.seek_bar);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
//        seekBarCustom.setMaxValue(10);
//        seekBarCustom.setMinValue(2);
//        seekBarCustom.setCurrentValue(1);
        seekBarCustom.setChangedListener(new SeekBarCustom.OnSeekBarChangListener() {
            @Override
            public void onProgressChanged(SeekBarCustom seekBarCustom, int progress) {
                super.onProgressChanged(seekBarCustom, progress);
                tvProgress.setText(String.valueOf(progress));
            }
        });
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

    public void paintRing(View view) {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(100, 100, 500, 500);
        float startAngle = 0f;
        float endAngle = 180f;
        canvas.drawColor(Color.WHITE);
        //0度是在表的三点钟位置
        canvas.drawArc(rectF, startAngle, endAngle, false, paint);
        canvas.save();
        ivCanvas.setImageBitmap(bitmap);
    }

    public void paintQuadBezier(View view) {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStrokeWidth(10);//画笔宽度
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);//画笔样式 实心/空心
//        paint.setShadowLayer(10, 5, 5, Color.RED);
        canvas.drawColor(Color.GREEN);
        Path path = new Path();
        Point startPoint = new Point(300,500);
        Point assistPoint = new Point(630, 400);
        Point endPoint = new Point(700, 10);
        path.moveTo(startPoint.x, startPoint.y);
        path.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(path, paint);
        canvas.drawPoint(assistPoint.x,assistPoint.y,paint);
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
