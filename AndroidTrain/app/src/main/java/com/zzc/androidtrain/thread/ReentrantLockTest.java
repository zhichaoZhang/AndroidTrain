package com.zzc.androidtrain.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 * <p/>
 * 比Synchronized高效 因为在高度争用情况下,处理器把大部分时间都花在任务处理,而不是线程调度上
 * <p/>
 * 特性:时间等候锁,多个条件变量或者锁投票,锁中断
 * <p/>
 * Created by zczhang on 16/9/26.
 */
public class ReentrantLockTest {

    ReentrantLock reentrantLock = new ReentrantLock();

    //获取锁和释放锁
    public void print(int str) {

        try {
            reentrantLock.lock();
            System.out.println(str + " 获取锁 ");
            doSomeThing(str);
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(str + " 释放锁 ");
            reentrantLock.unlock();
        }
    }

    public void doSomeThing(int str) {
        try {
            reentrantLock.lock();
            System.out.println(str + " do some thing");
        } finally {
            reentrantLock.unlock();
        }
    }

    int i = 0;

    public void plusMethod() {
        i++;
        System.out.println("plusMethod : i = " + i);
    }

    //获取不确定锁
    public void untimedLock() {
        //尝试获取锁
        boolean captured = reentrantLock.tryLock();
        try {
            System.out.println("tryLock() " + captured);
        } finally {
            if (captured) {
                reentrantLock.unlock();
            }
        }
    }

    //指定超时时间获取不确定锁
    public void timedLock() {
        boolean captured = false;
        try {
            //在2秒内尝试获取锁
            captured = reentrantLock.tryLock(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("tryLock(10, TimeUnit.SECONDS) " + captured);
        } finally {
            if (captured) {
                reentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        reentrantLockTest.testConditionLock();
//        reentrantLockTest.untimedLock();
//        reentrantLockTest.timedLock();
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 10; i++) {
//            final int Num = i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    reentrantLockTest.print(Num);
//                }
//            };
//            executorService.submit(runnable);
//        }
//
//        reentrantLockTest.untimedLock();
//        reentrantLockTest.timedLock();
//
//        executorService.shutdown();

    }


    //条件锁
    public void testConditionLock() {
        final ProductQueue<Integer> integerProductQueue = new ProductQueue<>();
        //生产者线程
        Runnable productRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((long) (Math.random() * 10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        int i = (int) (Math.random() * 10);
                        integerProductQueue.put(i);
                        System.out.println("生产者 " + Thread.currentThread().getName() + " 生产了" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("生产者线程被中断");
                        break;
                    }
                }
            }
        };

        //消费者线程
        Runnable consumeRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int i = integerProductQueue.take();
                        System.out.println("消费者 " + Thread.currentThread().getName() + " 消费了" + i);
                        Thread.sleep((long) (Math.random() * 10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("消费者线程被中断");
                        break;
                    }
                }
            }
        };

        Thread productThread = new Thread(productRun, "product-thread");
        Thread consumeThread1 = new Thread(consumeRun, "consume-thread1");
        Thread consumeThread2 = new Thread(consumeRun, "consume-thread2");


        productThread.start();
        consumeThread1.start();
        consumeThread2.start();
    }

    //可中断锁
    public void testInterruptiblyLock() {
        ReentrantLock reentrantLock = new ReentrantLock();
        ReentrantLockThread thread1 = new ReentrantLockThread(this, reentrantLock);
        thread1.setName("thread1");
        thread1.start();

        ReentrantLockThread thread2 = new ReentrantLockThread(this, reentrantLock);
        thread2.setName("thread2");
        thread2.start();

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ReentrantLockThread thread3 = new ReentrantLockThread(this, reentrantLock);
        thread3.setName("thread3");
        thread3.start();
        thread1.interrupt();
    }

    //时间等候锁
    public void testTimeLock() {
        untimedLock();
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timedLock();
    }


    //可中断锁
    static class ReentrantLockThread extends Thread {
        private ReentrantLockTest reentrantLockTest;
        private ReentrantLock reentrantLock;

        public ReentrantLockThread(ReentrantLockTest reentrantLockTest, ReentrantLock reentrantLock) {
            this.reentrantLockTest = reentrantLockTest;
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                reentrantLock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " 获取锁 ");
                try {
                    Thread.sleep(5 * 1000);
                    reentrantLockTest.plusMethod();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() + "被中断 ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + " 释放锁 ");
            }
        }
    }
}
