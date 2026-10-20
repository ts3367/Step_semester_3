package data_structures.class_problems;

// Maximum Sum Subarray of Fixed Size K. Sliding window of width k where the
// window sum is updated by adding the entering value and removing the leaving one.
// Complexity: O(n) time, O(1) space. Recomputing each window would cost O(n * k).
public class P5MaxSumSubarrayFixedSize {

    public static int maxSumSubarray(int[] sales, int k) {
        if (k <= 0 || k > sales.length) {
            throw new IllegalArgumentException("k must be between 1 and the array length.");
        }
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += sales[i];
        }
        int maxSum = windowSum;
        for (int i = k; i < sales.length; i++) {
            windowSum += sales[i] - sales[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[] sales = {2, 1, 5, 1, 3, 2};
        System.out.println(maxSumSubarray(sales, 3));
    }
}
