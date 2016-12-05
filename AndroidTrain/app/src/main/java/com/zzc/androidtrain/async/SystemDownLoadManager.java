package com.zzc.androidtrain.async;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.util.Toaster;

import java.io.File;

public class SystemDownLoadManager extends BaseActivity {
    private static final String TAG = "SystemDownLoadManager";
    private static final String downloadUrl = "http://42.81.5.32/qiniu-app-cdn.pgyer.com/503dc36257ce5db5c001d0cf38c30afb" +
            ".apk?e=1477042416&attname=Near_Merchant_v3.4.2_storeRelease_build3810" +
            ".apk&token=6fYeQ7_TVB5L0QSzosNFfw2HU8eJhAirMF5VxV9G:6h1AVRf92ObeZErha9drN-Q6x-Y=&wsiphost=ipdb";
    private String savePath = "/androidtrain/cache/file/android_train_new_version.apk";
    TextView tvDownloadProgress;
    private long downTaskId = 0;
    DownloadManager mDlManager;
    DownloadReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private static Handler mHandler = new Handler();
    private QueryProgressRunnable queryProgressRunnable;
    int queryProgressDelay = 1 * 1000;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SystemDownLoadManager.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_down_load_manager);
        tvDownloadProgress = (TextView) findViewById(R.id.tv_download_progress);
        mDlManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mReceiver = new DownloadReceiver();
        mIntentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        queryProgressRunnable = new QueryProgressRunnable(mDlManager);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    //开始下载
    public void onClickStartDownload(View view) {
        if (downTaskId == 0) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle("AndroidTrain");
            request.setDescription("新版本正在下载");
            request.setVisibleInDownloadsUi(true);
            //设置文件类型，可在下载完成后自动打开
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeType = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
            request.setMimeType(mimeType);
            //设置网络环境限制
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //设置存储路径
            request.setDestinationInExternalFilesDir(getBaseContext(), Environment.DIRECTORY_DOWNLOADS, savePath);
            downTaskId = mDlManager.enqueue(request);
            queryProgressRunnable.setTaskId(downTaskId);
            mHandler.postDelayed(queryProgressRunnable, queryProgressDelay);
            System.out.println("开始下载");
        } else {
            Toaster.showShortToast(this, "已经存在下载任务！");
        }
    }

    //取消下载
    public void onClickCancelDownload(View view) {
        if (downTaskId != 0) {
            mDlManager.remove(downTaskId);
            downTaskId = 0;
            mHandler.removeCallbacks(queryProgressRunnable);
            System.out.println("取消下载");
        } else {
            Toaster.showShortToast(this, "没有正在下载的任务！");
        }
    }

    public void setDownloadProgress(float progress) {
        tvDownloadProgress.setText("下载进度：" + progress);
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus(downTaskId);
        }
    }

    private void checkDownloadStatus(long taskId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(taskId);
        Cursor c = mDlManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i(TAG, "checkDownloadStatus: " + "下载暂停");
                    break;
                case DownloadManager.STATUS_PENDING:
                    Log.i(TAG, "checkDownloadStatus: " + "下载延迟");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.i(TAG, "checkDownloadStatus: " + "正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i(TAG, "checkDownloadStatus: " + "下载成功");
                    mHandler.removeCallbacks(queryProgressRunnable);
                    installApk(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + savePath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    mHandler.removeCallbacks(queryProgressRunnable);
                    Log.i(TAG, "checkDownloadStatus: " + "下载失败");
                    break;
            }
        }
    }

    //查询进度
    public class QueryProgressRunnable implements Runnable {

        private long mTaskId;
        private DownloadManager downloadManager;
        private DownloadManager.Query query;

        public QueryProgressRunnable(DownloadManager downloadManager) {
            this.downloadManager = downloadManager;
            this.query = new DownloadManager.Query();
        }

        public void setTaskId(long taskId) {
            this.mTaskId = taskId;
        }

        @Override
        public void run() {
            query.setFilterById(mTaskId);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                Log.i(TAG, "run: 已下载 = " + downloadedBytes);
                setDownloadProgress(downloadedBytes);
                mHandler.postDelayed(queryProgressRunnable, queryProgressDelay);
            }
        }
    }

    private void installApk(File file) {
        if (!file.exists()) {
            Log.e(TAG, "installApk: " + file.getAbsolutePath() + " 文件不存在");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
