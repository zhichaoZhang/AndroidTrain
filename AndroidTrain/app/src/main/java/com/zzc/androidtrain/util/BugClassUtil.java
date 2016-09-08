package com.zzc.androidtrain.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zzc.androidtrain.LoginActivity;

/**
 * Created by zczhang on 16/4/11.
 */
public class BugClassUtil {
    public void bugMethod(Context context) {
        Toast.makeText(context, "这个有bug的静态方法已被修复", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "这是一个有bug的静态方法", Toast.LENGTH_SHORT).show();
//        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
