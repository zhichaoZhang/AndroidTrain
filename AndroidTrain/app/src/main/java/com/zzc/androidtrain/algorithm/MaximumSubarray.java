package com.zzc.androidtrain.algorithm;

/**
 * 最大连续子序列和
 * <p>
 * 利用最大子序列的首尾值肯定大于零的特性
 * <p>
 * <p/>
 * <p>2 分治策略（递归法）</p>
 * <p>对这个问题，有一个相对复杂的O(NlogN)的解法，就是使用递归。
 * 如果要是求出序列的位置的话，这将是最好的算法了（因为我们后面还会有个O(N)的算法，但是不能求出最大子序列的位置）。该方法我们采用“分治策略”（divide-and-conquer）。<br>
 * <br>
 * <br>
 * 在我们例子中，最大子序列可能在三个地方出现，或者在左半部，或者在右半部，或者跨越输入数据的中部而占据左右两部分。
 * 前两种情况递归求解，第三种情况的最大和可以通过求出前半部分最大和（包含前半部分最后一个元素）以及后半部分最大和（包含后半部分的第一个元素）相加而得到。<br>
 * Created by zczhang on 16/9/21.
 */
public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        if (nums == null) {
            return 0;
        }
        int len = nums.length;
        if (len == 0) {
            return 0;
        }

        int tempMaxSum = 0;
        int finalMaxSum = 0;

        for (int i = 0; i < len; i++) {
            tempMaxSum += nums[i];
            if (tempMaxSum > finalMaxSum) {
                finalMaxSum = tempMaxSum;
            } else if (tempMaxSum < 0) {
                tempMaxSum = 0;
            }
        }
        return finalMaxSum;
    }

    public int maxSubArrayHelperFunction(int A[], int left, int right) {
        if (right == left) return A[left];
        int middle = (left + right) / 2;
        int leftans = maxSubArrayHelperFunction(A, left, middle);
        int rightans = maxSubArrayHelperFunction(A, middle + 1, right);
        int leftmax = A[middle];
        int rightmax = A[middle + 1];
        int temp = 0;
        for (int i = middle; i >= left; i--) {
            temp += A[i];
            if (temp > leftmax) leftmax = temp;
        }
        temp = 0;
        for (int i = middle + 1; i <= right; i++) {
            temp += A[i];
            if (temp > rightmax) rightmax = temp;
        }
        return Math.max(Math.max(leftans, rightans), leftmax + rightmax);
    }

    public static void main(String[] args) {
        MaximumSubarray maximumSubarray = new MaximumSubarray();
        int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
//        int result = maximumSubarray.maxSubArray(array);
        int result = maximumSubarray.maxSubArrayHelperFunction(array, 0, array.length - 1);
        System.out.println("最大连续子序列和---->" + result);
    }
}
