package com.zzc.androidtrain.qrcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.qrcode.zbar.QrcodeScanZbarActivity;

/**
 * Created by zczhang on 16/12/11.
 */

public class QrcodeLibCompareActivity extends BaseActivity{

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, QrcodeLibCompareActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_lib_compare);
    }

    public void onClickZbarTest(View view) {
        startActivity(QrcodeScanZbarActivity.getCallingIntent(this));
    }

    public void onClickZXingTest(View view) {

    }
}
