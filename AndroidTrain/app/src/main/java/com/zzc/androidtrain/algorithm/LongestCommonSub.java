package com.zzc.androidtrain.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 最大公共子序列
 * <p/>
 * 利用二维数组 进行 动态规划
 * <p/>
 * Created by zczhang on 16/9/19.
 */
public class LongestCommonSub {

    public List<Integer> find(int[] array1, int[] array2) {
        int[][] doubleArray = buildDoubleArray(array1, array2);
        List<Integer> result = new ArrayList<>();

        int i = array1.length;
        int j = array2.length;
        while (i > 0 && j > 0) {
            if (array1[i - 1] == array2[j - 1]) {
                result.add(array1[i - 1]);
                --i;
                --j;
            } else {
                if (doubleArray[i - 1][j] >= doubleArray[i][j - 1]) {
                    --i;
                } else {
                    --j;
                }
            }
        }

        return result;
    }

    private int[][] buildDoubleArray(int[] array1, int[] array2) {
        int len1 = array1.length;
        int len2 = array2.length;
        int[][] doubleArray = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            doubleArray[i][0] = 0;
        }

        for (int j = 0; j <= len2; j++) {
            doubleArray[0][j] = 0;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (array1[i - 1] == array2[j - 1]) {
                    doubleArray[i][j] = doubleArray[i - 1][j - 1] + 1;
                } else {
                    doubleArray[i][j] = Math.max(doubleArray[i - 1][j], doubleArray[i][j - 1]);
                }
            }
        }

        return doubleArray;
    }

    public static void main(String[] args) {
        int[] array1 = {2, 1, 4, 0, 6, 9};
        int[] array2 = {3, 2, 4, 9, 6, 0};
        LongestCommonSub longestCommonSub = new LongestCommonSub();
        List<Integer> subs = longestCommonSub.find(array1, array2);
        String output = "";
        for (int i = subs.size() - 1; i >= 0; i--) {
            output += subs.get(i) + " ";
        }

        System.out.println("最大公共子序列为 " + output);
    }
}
