package com.zzc.androidtrain.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 读锁为共享锁
 * 写锁为排他锁
 * <p>
 * Created by zczhang on 16/9/26.
 */
public class ReentrantReadWriteLockTest {
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    private int resource = 0;

    public int read() {
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取读锁,尝试读取");
            return resource;
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放读锁");
        }
    }

    public void write(int newValue) {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取写锁,尝试写入");
            this.resource = newValue;
            System.out.println(Thread.currentThread().getName() + "写入值 " + newValue);
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放写锁");
        }
    }

    public void testReadWriteLock() {

        Thread readThread1 = new Thread(new ReadRun(this));
        Thread readThread2 = new Thread(new ReadRun(this));
        Thread readThread3 = new Thread(new ReadRun(this));
        Thread readThread4 = new Thread(new ReadRun(this));
        Thread writeThread1 = new Thread(new WriteRun(this));
        readThread1.start();
        readThread2.start();
        readThread3.start();
        readThread4.start();
        writeThread1.start();
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTest reentrantReadWriteLockTest = new ReentrantReadWriteLockTest();
        reentrantReadWriteLockTest.testReadWriteLock();
    }

    static class ReadRun implements Runnable {
        private ReentrantReadWriteLockTest reentrantReadWriteLockTest;

        public ReadRun(ReentrantReadWriteLockTest reentrantReadWriteLockTest) {
            this.reentrantReadWriteLockTest = reentrantReadWriteLockTest;
        }

        @Override
        public void run() {
            while (true) {
                int result = reentrantReadWriteLockTest.read();
                System.out.println(Thread.currentThread().getName() + "读取到值 " + result);
            }
        }
    }

    static class WriteRun implements Runnable {

        private ReentrantReadWriteLockTest reentrantReadWriteLockTest;

        public WriteRun(ReentrantReadWriteLockTest reentrantReadWriteLockTest) {
            this.reentrantReadWriteLockTest = reentrantReadWriteLockTest;
        }

        @Override
        public void run() {
            while (true) {
                reentrantReadWriteLockTest.write((int) (Math.random() * 10));
            }
        }
    }
}
