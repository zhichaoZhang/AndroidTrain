package com.zzc.androidtrain.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 回环栅栏 同步辅助类
 * 可实现让一组线程等待至某个状态之后再全部同时执行,这个状态就叫做一个栅栏.
 * 当所有线程都被释放后,该栅栏可以重用
 * <p/>
 * 使用场景:开启多个子线程处理同步任务,当所有子任务都处理完成后,各个子线程再处理其他任务.
 * 同时主线程可插入任务,当最后一个子线程完成同步任务后,执行主线程插入任务,然后子线程再处理其他任务
 * <p/>
 * Created by zczhang on 16/9/28.
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        int childTaskNum = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(childTaskNum, new Runnable() {
            @Override
            public void run() {
                System.out.println("当最后一个线程 " + Thread.currentThread().getName() + "完成同步任务时,做一些事情...");
            }
        });
        for (int i = 0; i < childTaskNum; i++) {
            Thread thread = new Thread(new ChildTaskRun(cyclicBarrier));
            thread.start();
        }

    }

    public static class ChildTaskRun implements Runnable {

        private CyclicBarrier cyclicBarrier;

        public ChildTaskRun(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("子线程" + Thread.currentThread().getName() + "开始处理任务...");
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程" + Thread.currentThread().getName() + "处理任务完成,等待其他线程...");

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("所有子线程完成同步任务, 继续执行其他任务...");


        }
    }
}
