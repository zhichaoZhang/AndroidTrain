package com.zzc.androidtrain.thread;

/**
 * Java多线程特性
 * Created by zczhang on 16/9/23.
 */
public class MultiThreadFeatures {
    static boolean isRun = true;
    static int num = 0;
    static volatile boolean tag = false;
    int i = 0;

    public static void main(String[] args) {
        MultiThreadFeatures multiThreadFeatures = new MultiThreadFeatures();
        multiThreadFeatures.testVisibility();
    }

    void testVisibility() {
        Thread test = new Thread() {
            @Override
            public void run() {
                super.run();
                boolean threadTag = tag;
                while (isRun || num == 0) {
                    i++;
                    threadTag = tag;
//                    System.out.println("thread: isRun = " + isRun);
                }
            }
        };
        test.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("i---->" + i);

        isRun = false;
        num = 1;
        tag = true;
        System.out.println("isRun2--->" + isRun);
        System.out.println("tag2--->" + tag);
//        System.out.println("i---->" + i);
    }
//
//    public void syncBlockImpl() {
//        synchronized (this)  {
//            System.out.println("hello world");
//        }
//    }
//
//    public synchronized void syncMethodImpl() {
//        System.out.println("hello world");
//    }
}
