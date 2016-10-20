package com.zzc.androidtrain.apk_patch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.util.ApkUtil;
import com.zzc.androidtrain.util.Toaster;

import java.io.File;

public class ApkPatchActivity extends AppCompatActivity {

    private static final String SDCARD_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String PATCHER_PATH = SDCARD_ROOT_DIR + File.separator + "apk.patch";
    private static final String NEW_VERSION_PATH = SDCARD_ROOT_DIR + File.separator + "new_version.apk";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ApkPatchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_patch);
    }

    //显示版本号
    public void onCheckVersionBtnClick(View view) {
        Toaster.showLongToast(this, "Version 1.0");
    }

    //升级版本
    public void onUpdateVersionBtnClick(View view) {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
            String sourceDir = applicationInfo.sourceDir;
            System.out.println("sourceDir : " + sourceDir);
            System.out.println("newPath : " + NEW_VERSION_PATH);
            System.out.println("patchPath : " + PATCHER_PATH);
            applyPatch(sourceDir, NEW_VERSION_PATH, PATCHER_PATH);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void applyPatch(String oldPath, String newPath, String patchPath) {

        new LoadPatchFileTask().execute(oldPath, newPath, patchPath);
    }

    class LoadPatchFileTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("---开始合并增量包！----");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            System.out.println("---合并增量包结束！开始安装----");
            ApkUtil.installApk(getBaseContext(), NEW_VERSION_PATH);
        }

        @Override
        protected Integer doInBackground(String... params) {
            return new FileDiffer().fileCombine(params[0], params[1], params[2]);
        }
    }

}
