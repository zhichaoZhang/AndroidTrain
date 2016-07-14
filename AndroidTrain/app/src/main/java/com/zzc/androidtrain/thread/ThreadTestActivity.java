package com.zzc.androidtrain.thread;

import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zzc.androidtrain.R;

public class ThreadTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_test);
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        System.out.println("主线程优先级(JavaApi)----->"+Thread.currentThread().getPriority());
        System.out.println("主线程优先级(Android Api)----->"+ Process.getThreadPriority(Process.myPid()));
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
        System.out.println("主线程优先级(Android Api)----->"+Process.getThreadPriority(Process.myPid()));
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println("默认异步线程优先级---->"+getPriority());
            }
        };
        thread.start();
    }
}
