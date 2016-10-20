package com.zzc.androidtrain.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者-消费者
 * 多条件锁
 * <p/>
 * Created by zczhang on 16/9/27.
 */
public class ProductQueue<T> {
    private T[] items;
    private final Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    private int head, tail, count;

    public ProductQueue() {
        this(10);
    }

    public ProductQueue(int maxSize) {
        items = (T[]) new Object[maxSize];
    }

    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == getCapacity()) {
                System.out.println("---------队列满,生产者等待------");
                notFull.await();
            }
            items[tail] = t;
            if (++tail == getCapacity()) {
                tail = 0;
            }
            ++count;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                System.out.println("---------队列空,消费者" + Thread.currentThread().getName() + "等待------");
                notEmpty.await();
            }
            T item = items[head];
            items[head] = null;
            if (++head == getCapacity()) {
                head = 0;
            }
            --count;
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return items.length;
    }
}
