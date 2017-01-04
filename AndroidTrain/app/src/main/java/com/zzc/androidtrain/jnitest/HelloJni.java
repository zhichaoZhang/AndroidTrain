package com.zzc.androidtrain.jnitest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.tencent.tinker.lib.tinker.TinkerApplicationHelper;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

/**
 * jni测试
 *
 * Created by zczhang on 16/6/4.
 */
public class HelloJni extends BaseActivity{

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HelloJni.class);
        return intent;
    }

    TextView tvStringFromNative;

    static {
        System.loadLibrary("jniTest");
    }

    /**
     * 调用jni方法,返回一个字符串
     *
     * @return
     */


    public native String stringFromJni();

//    public static void main(String[] args) {
//        HelloJni helloJni = new HelloJni();
//        String stringFromJni = helloJni.stringFromJni();
//        System.out.println("stringFromJni---->"+stringFromJni);
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_test);
        tvStringFromNative = (TextView)findViewById(R.id.tv_string_from_native);
    }

    public void onGetStringFromNative(View view) {
        String strFromNative = stringFromJni();
        tvStringFromNative.setText(strFromNative);

    }
}
