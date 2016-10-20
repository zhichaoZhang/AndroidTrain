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
 * 本地服务
 * Created by zczhang on 16/9/8.
 */
public class LocalService extends Service {
    private static final String TAG = "LocalService";
    MyBinder myBinder;
    RemoteServiceConnection remoteServiceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        remoteServiceConnection = new RemoteServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + "本地服务启动");
        bindRemoteService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindRemoteService();
    }

    private class MyBinder extends KeepAliveBinder.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "LocalService";
        }
    }

    private class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + "连接远程服务成功!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + "断开连接");
            startRemoteService();
            bindRemoteService();
        }
    }

    private void bindRemoteService() {
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), remoteServiceConnection, Context.BIND_IMPORTANT);
    }

    private void startRemoteService() {
        LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
    }

    private void unbindRemoteService() {
        LocalService.this.unbindService(remoteServiceConnection);
    }
}
