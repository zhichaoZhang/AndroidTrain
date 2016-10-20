package com.zzc.androidtrain.image_filter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zzc.androidtrain.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 使用OpenGl渲染图片并添加滤镜
 * <p>
 * GLSurfaceView负责渲染图形内容
 * GLSurfaceView.Renderer负责控制显示什么图形
 */
public class OpenglFilterEffectActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    GLSurfaceView glSurfaceView;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, OpenglFilterEffectActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_filter_effect);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        //为GLSurfaceView设置渲染控制器
        glSurfaceView.setRenderer(this);
        //设置渲染模式 只有当渲染数据改变时才绘制视图
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /**
     * 渲染线程开始或EGL Context丢失（设置睡眠再次唤醒）后，调用此方法
     * 当Context丢失后，Context关联的OpenGL资源会自动删除
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //向GLSurfaceView上画一个颜色
        GLES20.glClearColor(0.0f, 255f, 0.0f, 1.0f);
    }

    /**
     * 当Surface尺寸改变时调用此方法
     * 如横竖屏切换
     *
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 由系统回调绘制当前帧
     *
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //重绘背景色
        GLES20.glClearColor(0.0f, 255f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Bitmap imageBmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_test);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, imageBmp, 0);
    }

    public void onOriginOperateClick(View view) {
        //请求绘制一帧
        glSurfaceView.requestRender();
    }
}
