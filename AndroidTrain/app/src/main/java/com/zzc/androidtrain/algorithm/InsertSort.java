package com.zzc.androidtrain.algorithm;

/**
 * 插入排序
 * 降序
 * <p/>
 * Created by zczhang on 16/9/9.
 */
public class InsertSort {
    //降序
    public void sort(int[] input, int startIndex, int endIndex) {
        if (input == null || input.length <= 1) {
            return;
        }
        for (int i = startIndex + 1; i <= endIndex; i++) {
            int temp = input[i];
            for (int j = i; j >= 0; j--) {

                if (j > 0 && temp > input[j - 1]) {
                    input[j] = input[j - 1];
                } else {
                    input[j] = temp;
                    break;
                }
            }
        }
    }

    //升序
    public void ascendingSort(int[] input, int startIndex, int endIndex) {
        if (input == null || input.length <= 1) {
            return;
        }
        for (int i = startIndex + 1; i <= endIndex; i++) {
            int temp = input[i];
            for (int j = i; j >= 0; j--) {

                if (j > 0 && temp < input[j - 1]) {
                    input[j] = input[j - 1];
                } else {
                    input[j] = temp;
                    break;
                }
            }
        }
    }


    public static void main(String[] args) {
        InsertSort insertSort = new InsertSort();
        int[] data = {1, 2, 7, 3, 4, 5, 10, 2, 3, 8, 11, 6, 9};
        insertSort.ascendingSort(data, 0, data.length - 1);

        String sorted = "";
        for (int i = 0; i < data.length; i++) {
            sorted += " " + data[i];
        }
        System.out.println("排序后的结果--->" + sorted);
    }
}
