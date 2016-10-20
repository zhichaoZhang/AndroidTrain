package com.zzc.androidtrain.multi_process_keep_alive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zzc.androidtrain.KeepAliveBinder;

/**
 * 远程服务
 * Created by zczhang on 16/9/8.
 */
public class RemoteService extends Service{
    private static final String TAG = "RemoteService";
    private MyBinder myBinder;
    private LocalServiceConnection serviceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        serviceConnection = new LocalServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + "远程服务启动");
        bindLocalService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    private class MyBinder extends KeepAliveBinder.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "LocalService";
        }
    }

    private class LocalServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + "连接本地服务成功!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + "断开连接");
            startLocalService();
            bindLocalService();
        }
    }

    private void bindLocalService() {
        RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), serviceConnection, Context.BIND_IMPORTANT);
    }

    private void startLocalService() {
        RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
    }

    private void unbindService() {
        RemoteService.this.unbindService(serviceConnection);
    }
}
