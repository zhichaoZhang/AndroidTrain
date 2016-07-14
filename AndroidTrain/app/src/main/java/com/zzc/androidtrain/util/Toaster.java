package com.zzc.androidtrain.util;

import android.content.Context;
import android.widget.Toast;

/**
 *
 * Created by zczhang on 16/5/20.
 */
public class Toaster {
    private static Toast toast;

    public static void showLongToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showShortToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
