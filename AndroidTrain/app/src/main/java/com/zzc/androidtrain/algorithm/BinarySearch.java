package com.zzc.androidtrain.algorithm;

/**
 * 二分查找算法
 * <p>
 * 基于有序集合的前提
 * <p>
 * Created by zczhang on 16/9/12.
 */
public class BinarySearch {
    public static final int INVALID_POSITION = -1;

    /**
     * 二分查找算法
     *
     * @param input       有序数组
     * @param startIndex  检索开始位置
     * @param endIndex    检索结束位置
     * @param targetValue 查找的关键字
     * @return 关键字所在位置 -1 代表没有找到
     */
    public int search(int[] input, int startIndex, int endIndex, int targetValue) {
        //参数检查
        if (input == null || input.length == 0 || startIndex > endIndex || startIndex < 0 || endIndex < 0 || startIndex >= input.length || endIndex
                >= input.length) {
            return INVALID_POSITION;
        }

        int binaryIndex = (startIndex + endIndex) / 2;
        if (input[binaryIndex] == targetValue) {
            return binaryIndex;
        }

        if (startIndex == binaryIndex || endIndex == binaryIndex) {
            return INVALID_POSITION;
        }

        if (targetValue > input[binaryIndex]) {
            startIndex = binaryIndex + 1;
        } else {
            endIndex = binaryIndex - 1;
        }

        return search(input, startIndex, endIndex, targetValue);

    }

    public static void main(String[] args) {
        int[] data = {1, 2, 3, 4, 5, 6, 7,8, 8, 9, 10};
        BinarySearch binarySearch = new BinarySearch();
        int targetValue = 8;
        int result = binarySearch.search(data, 0, data.length - 1, targetValue);
        if (result == INVALID_POSITION) {
            System.out.println("未找到");
        } else {
            System.out.println(targetValue + " 在数组中的位置为 " + result);
        }
    }
}
