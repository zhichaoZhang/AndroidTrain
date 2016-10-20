package com.zzc.androidtrain.algorithm;

/**
 * 求两个有序数组的中位数
 * <p/>
 * 利用数组有序的特性，比较两个数组A和B的 k/2 处的值，即比较A[k/2 - 1]和B[k/2 - 1]。如果两者相等，则此处即为第K大的值。
 * 如果A[k/2 - 1] 小于 B[k/2 -1]，说明第K大的值肯定不在A[0] - A[k/2 -1] 之间，则可以删除数组A中A[0] - A[k/2 -1]这个区间的值。之后就是查找新数组A和数组B中第k/2大的数。
 * 反之，如果A[k/2 -1] 大于 B[k/2 -1]，则第K大的值肯定不在B[0] - B[k/2 - 1]区间内。
 * <p/>
 * Created by zczhang on 16/9/20.
 */
public class MedianOfTwoSortedArrays {

    /**
     * 执行查找
     * @param a         第一个数组
     * @param startA    查询起始位置
     * @param endA      查询结束位置
     * @param b         第二个数组
     * @param startB    查询起始位置
     * @param endB      查询结束位置
     * @param pos       第pos大的数
     * @return          第pos大的数
     */
    public int find(int[] a, int startA, int endA, int[] b, int startB, int endB, int pos) {

        if (a == null || b == null || pos < 0) {
            throw new IllegalArgumentException("params are illegal");
        }

        int lenA = endA - startA + 1;
        int lenB = endB - startB + 1;

        //保证a的长度小于b
        if (lenA > lenB) {
            return find(b, startB, endB, a, startA, endA, pos);
        }

        //如果小数组的长度为0,pos位置肯定在b数组中
        if (lenA == 0) {
            return b[pos - 1];
        }

        //如果找第一个大的数,返回两个数组0位置出较小那个
        if (pos == 1) {
            return Math.min(a[startA], b[startB]);
        }

        //将数组分成两部分
        int tempPosA = Math.min((pos / 2), lenA);
        int tempPosB = pos - tempPosA;
        //较小的部分可以忽略,继续查找剩下部分,当然查找位置也要减去忽略部分的大小
        if (a[startA + tempPosA - 1] < b[startB + tempPosB - 1]) {
            return find(a, startA + tempPosA, endA, b, startB, endB, pos - tempPosA);
        } else if (a[startA + tempPosA - 1] > b[startB + tempPosB - 1]) {
            return find(a, startA, endA, b, startB + tempPosB, endB, pos - tempPosB);
        } else {
            return a[startA + tempPosA - 1];
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 5, 7, 9, 11};
        int[] b = {2, 4, 6, 8, 10};
        MedianOfTwoSortedArrays medianOfTwoSortedArrays = new MedianOfTwoSortedArrays();
        int result = medianOfTwoSortedArrays.find(a, 0, a.length - 1, b, 0, b.length - 1, (a.length + b.length + 1) / 2);

        System.out.println("两个有序数组中位数---->" + result);
    }

}
