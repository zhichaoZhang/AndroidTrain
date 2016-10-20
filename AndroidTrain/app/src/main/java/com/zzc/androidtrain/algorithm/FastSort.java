package com.zzc.androidtrain.algorithm;

/**
 * 快速排序
 * <p>
 * Created by zczhang on 16/9/10.
 */
public class FastSort {
    private InsertSort insertSort;
    private int mUseInsertSortLimit = 10;

    public FastSort() {
        insertSort = new InsertSort();
    }

    //交换数组位置
    private void swapReference(int[] a, int left, int right) {
        int temp = a[left];
        a[left] = a[right];
        a[right] = temp;
    }

    //取数组中值,并将中值转移到倒数第二位置
    private int median3(int[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[left] > a[center]) {
            swapReference(a, left, center);
        }
        if (a[center] > a[right]) {
            swapReference(a, center, right);
        }
        if (a[left] > a[right]) {
            swapReference(a, left, right);
        }
        swapReference(a, center, right - 1);
        return a[right - 1];
    }

    public void sort(int[] input, int startIndex, int endIndex) {
        //参数检查
        if (input == null || input.length <= 1 || endIndex <= startIndex) {
            return;
        }

        //数组长度小于10的时候,使用插入排序
        if (startIndex + mUseInsertSortLimit > endIndex) {
            insertSort.ascendingSort(input, startIndex, endIndex);
            return;
        }

        int medianValue = median3(input, startIndex, endIndex);

        //开始分割
        int left = startIndex;
        int right = endIndex - 1;
        for (; ; ) {
            while (input[++left] < medianValue) {
            }
            while (input[--right] > medianValue) {
            }
            if (left < right) {
                swapReference(input, left, right);
            } else {
                break;
            }
        }
        swapReference(input, left, endIndex - 1);

        sort(input, startIndex, left - 1);
        sort(input, left + 1, endIndex);
    }

    public static void main(String[] args) {
        FastSort fastSort = new FastSort();
        int[] input = {5, 9, 7, 7, 1, 8, 3, 7, 4, 6, 2, 10};
        String output = "";
        fastSort.sort(input, 0, input.length - 1);
        for (int i = 0; i < input.length; i++) {
            output += " " + input[i];
        }
        System.out.println("快排输出结果---->" + output);
    }
}
