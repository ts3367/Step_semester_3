package data_structures.assignment_problems;

import java.util.HashMap;
import java.util.Map;

// Net-Balance Period Counter. Prefix sums plus a frequency map of the running
// totals seen so far: a period ending at j sums to k exactly when an earlier
// running total was S - k.
// Complexity: O(n) time, O(n) extra space, against O(n^2) for summing every
// subarray. Negative values rule out a sliding window, so the map is what makes
// this work.
public class P3NetBalancePeriodCounter {

    public static int countPeriods(int[] transactions, long k) {
        Map<Long, Integer> frequency = new HashMap<>();
        frequency.put(0L, 1);
        long runningTotal = 0;
        int periods = 0;
        for (int transaction : transactions) {
            runningTotal += transaction;
            periods += frequency.getOrDefault(runningTotal - k, 0);
            frequency.merge(runningTotal, 1, Integer::sum);
        }
        return periods;
    }

    public static void main(String[] args) {
        int[] transactions = {3, 4, -7, 1, 3, 3, 1, -4};
        System.out.println(countPeriods(transactions, 7));
        System.out.println(countPeriods(new int[]{1, 2, 3}, 10));
    }
}
