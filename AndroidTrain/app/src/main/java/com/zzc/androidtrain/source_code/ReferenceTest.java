package com.zzc.androidtrain.source_code;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Java引用
 * 软引用不应当作为缓存手段，只有当内存不足才会被回收并入队列
 * 弱引用，垃圾回收线程扫描内存区域时，发现某对象只有弱引用时，就会回收并入队列
 * 虚引用，当垃圾回收器认为某对象是虚可达的时候，就会入队列，但不会它对对象的引用不会被清除，而是等到开发者手动处理引用队列中的虚引用
 * <p>
 * ReferenceQueue 引用队列，如果某引用所引用的对象被回收，虚拟机会把这个引用加入到引用队列中
 * 可以用作"回收前清理工作"的工具
 * <p>
 * <p>
 * Created by zczhang on 17/1/8.
 */

public class ReferenceTest {
    private static ReferenceQueue<VeryBig> referenceQueue = new ReferenceQueue<>();

    public static void checkQueue() {
        Reference<? extends VeryBig> inq = referenceQueue.poll();
        if (inq != null) {
            System.out.println("Reference type : " + inq.getClass().getSimpleName());
            System.out.println("In Queue: " + inq.get());
        }
    }

    static class VeryBig {
        private static final int SIZE = 10000;
        private long[] la = new long[SIZE];
        private String ident;

        public VeryBig(String id) {
            this.ident = id;
        }

        @Override
        public String toString() {
            return ident;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("finalize " + ident);
        }
    }

    public static void main(String[] args) {
        int size = 10;
        //软引用 列表
        LinkedList<SoftReference<VeryBig>> sa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            sa.add(new SoftReference<VeryBig>(new VeryBig("soft" + i), referenceQueue));
            System.out.println("just created: " + sa.getLast());
            checkQueue();
        }

        //弱引用 列表
        LinkedList<WeakReference<VeryBig>> wa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            wa.add(new WeakReference<VeryBig>(new VeryBig("weak " + i), referenceQueue));
            System.out.println("just created: " + wa.getLast());
            checkQueue();
        }

        //单个软引用
        SoftReference<VeryBig> s = new SoftReference<VeryBig>(new VeryBig("soft"));
        //单个弱引用
        WeakReference<VeryBig> w = new WeakReference<VeryBig>(new VeryBig("weak"));

        //触发虚拟机垃圾回收
        System.gc();

        //虚引用列表 虚引用必须和引用队列协同使用
        LinkedList<PhantomReference<VeryBig>> pa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            pa.add(new PhantomReference<VeryBig>(new VeryBig("phantom" + i), referenceQueue));
            System.out.println("just created: " + pa.getLast());
        }

        for (int i = 0; i < size + size; i++) {
            checkQueue();
        }

        System.gc();
        for (int i = 0; i < size; i++) {
            checkQueue();
        }
    }

    /**
     * 执行结果说明
     *
     * //新建10个软引用存入队列和10个弱引用队列，一个单独的软引用和弱引用
     * //并没有把它们放入引用队列
     just created: java.lang.ref.SoftReference@66d3c617
     just created: java.lang.ref.SoftReference@63947c6b
     just created: java.lang.ref.SoftReference@2b193f2d
     just created: java.lang.ref.SoftReference@355da254
     just created: java.lang.ref.SoftReference@4dc63996
     just created: java.lang.ref.SoftReference@d716361
     just created: java.lang.ref.SoftReference@6ff3c5b5
     just created: java.lang.ref.SoftReference@3764951d
     just created: java.lang.ref.SoftReference@4b1210ee
     just created: java.lang.ref.SoftReference@4d7e1886
     just created: java.lang.ref.WeakReference@3cd1a2f1
     just created: java.lang.ref.WeakReference@2f0e140b
     just created: java.lang.ref.WeakReference@7440e464
     just created: java.lang.ref.WeakReference@49476842
     just created: java.lang.ref.WeakReference@78308db1
     just created: java.lang.ref.WeakReference@27c170f0
     just created: java.lang.ref.WeakReference@5451c3a8
     just created: java.lang.ref.WeakReference@2626b418
     just created: java.lang.ref.WeakReference@5a07e868
     just created: java.lang.ref.WeakReference@76ed5528
     //此处触发gc
     //单个弱引用被回收
     finalize weak
     //十个弱引用被回收
     finalize weak 9
     finalize weak 8
     finalize weak 7
     finalize weak 6
     finalize weak 5
     finalize weak 4
     finalize weak 3
     finalize weak 2
     finalize weak 1
     finalize weak 0
     //新建10个虚引用存入队列，并存入引用队列
     //未触发gc，从引用队列拿到的虚引用的引用对象为空，说明已经被回收
     just created: java.lang.ref.PhantomReference@2c7b84de
     In Queue: null
     just created: java.lang.ref.PhantomReference@3fee733d
     In Queue: null
     just created: java.lang.ref.PhantomReference@5acf9800
     In Queue: null
     just created: java.lang.ref.PhantomReference@4617c264
     In Queue: null
     just created: java.lang.ref.PhantomReference@36baf30c
     In Queue: null
     just created: java.lang.ref.PhantomReference@7a81197d
     In Queue: null
     just created: java.lang.ref.PhantomReference@5ca881b5
     In Queue: null
     just created: java.lang.ref.PhantomReference@24d46ca6
     In Queue: null
     just created: java.lang.ref.PhantomReference@4517d9a3
     In Queue: null
     just created: java.lang.ref.PhantomReference@372f7a8d
     In Queue: null

     */
}

