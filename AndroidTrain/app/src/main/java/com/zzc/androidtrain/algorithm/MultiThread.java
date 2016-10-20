package com.zzc.androidtrain.algorithm;

/**
 * long double 类型的赋值操作在64位操作系统上是否是原子操作 是
 * Created by zczhang on 16/9/11.
 */
public class MultiThread {
    private static final String TAG = "MultiThread";
    public volatile long a = 0l;

    public synchronized void set1() {
        a = -1;
    }

    public synchronized void set2() {
        a = 0;
    }

    public synchronized void check() {
        System.out.println("a = " + a);
        if (a != -1 && a != 0) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {
        final MultiThread multiThread = new MultiThread();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    multiThread.set1();
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    multiThread.set2();
                }
            }
        });
        thread2.start();

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    multiThread.check();
                }
            }
        });
        thread3.start();
    }

}
