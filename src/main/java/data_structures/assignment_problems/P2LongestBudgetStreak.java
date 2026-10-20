package data_structures.assignment_problems;

// Longest Budget-Friendly Streak. Variable-size sliding window: the right end
// advances one day at a time and the left end moves forward only while the
// running total exceeds the budget.
// Complexity: O(n) time, O(1) space, against O(n^2) for checking every run. The
// non-negative values are what make the shrink step safe, since removing an
// element from the window can never increase its total.
public class P2LongestBudgetStreak {

    public record Streak(int length, int startIndex) {
    }

    public static Streak longestStreak(int[] costs, int budget) {
        int left = 0;
        int runningTotal = 0;
        int bestLength = 0;
        int bestStart = -1;
        for (int right = 0; right < costs.length; right++) {
            runningTotal += costs[right];
            while (runningTotal > budget) {
                runningTotal -= costs[left];
                left++;
            }
            int length = right - left + 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left;
            }
        }
        return new Streak(bestLength, bestStart);
    }

    public static String describe(Streak streak) {
        return "(" + streak.length() + ", " + streak.startIndex() + ")";
    }

    public static void main(String[] args) {
        int[] costs = {4, 2, 1, 7, 3, 1, 2, 1, 5};
        System.out.println(describe(longestStreak(costs, 8)));
        System.out.println(describe(longestStreak(new int[]{9, 10}, 8)));
    }
}
