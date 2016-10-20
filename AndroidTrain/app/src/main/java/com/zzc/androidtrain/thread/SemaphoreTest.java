package com.zzc.androidtrain.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 信号量许可集  本质为共享锁
 * 用来限制多个线程对有限的资源访问进行控制
 * <p/>
 * 正整数,默认是1
 * 分为公平(先进先出)和非公平模式(随机)
 * <p/>
 * Created by zczhang on 16/9/26.
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
//        final Semaphore semaphore = new Semaphore(5, true);//公平信号量
        final Semaphore semaphore = new Semaphore(5, false);//非公平信号量

        //开启20个线程
        for (int i = 0; i < 20; i++) {
            final int Num = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        //线程开始时,获取许可
                        semaphore.acquire();
                        System.out.println("Thread " + Num + " accessing");
                        Thread.sleep((long) (Math.random() * 10000));
                        //访问完后释放
                        semaphore.release();
                        System.out.println("available permit = " + semaphore.availablePermits());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            executorService.submit(runnable);
        }

        executorService.shutdown();
    }
}
