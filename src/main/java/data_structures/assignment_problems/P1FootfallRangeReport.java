package data_structures.assignment_problems;

import java.util.ArrayList;
import java.util.List;

// Mall Footfall Range Report. Prefix sums let every range query be answered
// from two stored values instead of re-adding the range.
// Complexity: O(n) preprocessing, O(1) per query, so O(n + q) overall with O(n)
// extra space. A naive per-query sum would cost O(n * q), and the preprocessing
// pays off as soon as more than a handful of queries hit the same array.
public class P1FootfallRangeReport {

    public static int[] buildPrefixSums(int[] visitors) {
        int[] prefix = new int[visitors.length + 1];
        for (int i = 0; i < visitors.length; i++) {
            prefix[i + 1] = prefix[i] + visitors[i];
        }
        return prefix;
    }

    public static List<Integer> footfallReport(int[] visitors, int[][] queries) {
        int[] prefix = buildPrefixSums(visitors);
        List<Integer> totals = new ArrayList<>();
        for (int[] query : queries) {
            int start = query[0];
            int end = query[1];
            totals.add(prefix[end + 1] - prefix[start]);
        }
        return totals;
    }

    public static void main(String[] args) {
        int[] visitors = {12, 7, 3, 9, 15, 4, 8};
        int[][] queries = {{0, 2}, {2, 5}, {4, 6}, {3, 3}};
        System.out.println(footfallReport(visitors, queries));
    }
}
