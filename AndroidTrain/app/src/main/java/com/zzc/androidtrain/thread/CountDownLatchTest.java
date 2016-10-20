package com.zzc.androidtrain.thread;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 同步辅助类
 * 指定数字count初始化,然后调用await方法的线程会一直阻塞,直到count变为0
 * <p>
 * 一个同步辅助类，允许一个或多个线程等待，一直到其他线程完成任务
 * 使用场景：1.主线程开启多个子线程去执行分解的任务，当所有子线程都完成后，主线程再继续执行。
 * 2.主线程先运行,子线程再运行,主线程等待子线程完成再运行
 * <p>
 * Created by zczhang on 16/9/28.
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        int childTaskNum = 3;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch childEndLatch = new CountDownLatch(childTaskNum);

        for (int i = 0; i < childTaskNum; i++) {
            Thread child = new Thread(new ChildTaskRun(startLatch,childEndLatch));
            child.start();
        }

        System.out.println("主线程处理一些任务...");
        Thread.sleep(2*1000);
        System.out.println("主线程处理一些任务完成");

        startLatch.countDown();

        System.out.println("主线程等待子线程处理任务完成...");
        childEndLatch.await();
        System.out.println("主线程处理任务结果");
    }

    private static class ChildTaskRun implements Runnable {
        private CountDownLatch mainStartLatch;
        private CountDownLatch childEndLatch;

        public ChildTaskRun(CountDownLatch mainStartLatch, CountDownLatch childEndLatch) {
            this.mainStartLatch = mainStartLatch;
            this.childEndLatch = childEndLatch;
        }

        @Override
        public void run() {
            try {
                mainStartLatch.await();
                doSomeThing();
                System.out.println("子线程 " + Thread.currentThread().getName() + "处理任务完成");
                childEndLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doSomeThing() {
            System.out.println("子线程 " + Thread.currentThread().getName() + "处理任务中");
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
